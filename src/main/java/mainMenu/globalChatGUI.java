package mainMenu;

//google sheets
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

//other
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * this program is the messaging aspect of the arcade, it allows users to join chatrooms and talk to eachother
 */

/**
 *
 * @author Parker Rowe
 */
public class globalChatGUI extends javax.swing.JFrame {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GOOGLE CODE, this code was taken from google's sheets API V4 website as a setup to use it in java. Can be found here: https://developers.google.com/sheets/api/quickstart/java   
    
    
    private static final String APPLICATION_NAME =
        "Google Sheets API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(SheetsScopes.SPREADSHEETS);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            arcadeMain.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public static ValueRange response;
    public static UpdateValuesResponse request;
    
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //START OF CODE WRITTEN BY PARKER
    
    /**
     * Creates new form globalChatGUI, this is the CONSTRUCTOR and will run when the messaging button is pressed
     */
    public globalChatGUI() throws IOException {
        initComponents();
        new Thread(r).start();
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   
        this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent event) {
            closeWindow();
        }
    });
        messagingPanel.setVisible(false);
    }

    
    
    /**
     * method setCell is used to send a message into a specified cell in the online spreadsheet.
     * 
     * the method is passed the cell the user wants to send it to, and the message they want to send. Both the cell and message are strings
     */
    public static void setCell(String cell, String msg) throws IOException{
        // Build a new authorized API client service. This tells the google code found above to start a new "sheets service" which will allow the program to talk to the spreadsheet
        Sheets service = getSheetsService();
        
        //a new string called spreadsheetId is intialized and set equal to the online spreadsheet's ID. This ID is taken from the URL of the online spreadsheet (this is the current one: https://docs.google.com/spreadsheets/d/1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k/edit#gid=0)
        String spreadsheetId = "1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k";
        
        //new string range is set equal to the cell:cell, is is possible to send a range of cells but for this program we do not need that, so we just set the range to be one cell
        //for example: if i pass setCell() the cell "B2", the range will look like "B2:B2" 
        String range = cell+":"+cell;
        
        
        //a list of objects called "row1" has the passed message added to it.
        List<Object> row1 = new ArrayList<Object>();
        row1.add(msg);
         
        //an arraylist of arralists of objects is created called arraydata is then created and the arraylist "row1" is added to it
        List<List<Object>> arrData = new ArrayList<>();
        arrData.add(row1);
        
        //a new valuerange is created, this code was supplied by google but I do know that it needs the range, and the array that contains the desired message
        ValueRange oRange = new ValueRange();
        oRange.setRange(range); // I NEED THE NUMBER OF THE LAST ROW
        oRange.setValues(arrData);
        
        //an arraylist of valueranges is created and the previously created valuerange is added as an element
        List<ValueRange> oList = new ArrayList<>();
        oList.add(oRange);
        
        //sends a request to google's sheets, sets the value type to RAW (so the sheet doesnt try to recognize the message as a number or something and attempt to do math with it for example)
        BatchUpdateValuesRequest oRequest = new BatchUpdateValuesRequest();
        oRequest.setValueInputOption("RAW");
        //sets the data using the range and the message into the sheet
        oRequest.setData(oList);
        
        //executes sending the data
        BatchUpdateValuesResponse oResp1 = service.spreadsheets().values().batchUpdate(spreadsheetId, oRequest).execute();
        
  }
    
    //getCell is a method that is passed the cell the user wants and it will return a string containing the information that can be found in that cell in the spreadsheet
    public static String getCell(String cell) throws IOException{
        // Build a new authorized API client service. This tells the google code found above to start a new "sheets service" which will allow the program to talk to the spreadsheet
        Sheets service = getSheetsService();
        
        //string range is the desired range that the program will use to try and read, this range actually also requires the spreadsheets name, and I have named the spreadsheet playerlist
        String range = String.format("Player List!%s:%s",cell,cell);
        
        //spreadsheetId is what identifies your google spreadsheet, it is part of the URL (this is the current one: https://docs.google.com/spreadsheets/d/1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k/edit#gid=0)
        String spreadsheetId = "1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k";
        
        //executes the read request to the sheet using the range
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        
        //the response received from the sheet is inputted into an arraylist of arraylists
        List<List<Object>> values = response.getValues();
        
        //String str is what is used as the return value for the method, it is initialized to be "" (nothing) and will have the received message added to it later
        String str = "";
        //print to console so the user can see what is actually happening (I, Parker, used this all the time while making the program)
        System.out.println("\nReading cell: "+cell);
        System.out.println("values: "+values);
        
        //check to see if the returned message is empty (ie, the cell the user chose has nothing in it)
        if (values == null || values.isEmpty()) {
            //let the user know that no data was found
            System.out.println("No data found.");
            
        //if the values array is NOT empty (ie there is a message!) then run the following code
        } else {
          //sorts through the array and will grab the very first element in the very first arrayList and sets string 'str' equal to that element. 
          for(List row : values){
              str = row.get(0).toString();
              break;
          }
        }
        
        //return string str
        return str;   
    }
    
    
    //boolean shutdown is set to false by default, itll be used later to shutdown and clocks constantly checking for new messages when the user closes the messaging window
    volatile boolean shutdown = false;
    
    //integer chatroom is created and set to 1 by default
    int chatRoom = 1;
    
    //string chatmessage is set to "" (essentially empty) by default
    String chatMessage = "";
    
    //string prevChatMessage is also created, this will be used to keep track of the previous chat message sent in the chatroom
    String prevChatMessage;
    
    //creates a new runnable that will be constantly running and scanning the google sheet for new messages
    Runnable r = new Runnable() {
        
        public void run() {
            try {
                 scan();
             } catch (IOException | InterruptedException ex) {
                 Logger.getLogger(globalChatGUI.class.getName()).log(Level.SEVERE, null, ex);
             }
            }  
     };
    
    
    //method scan is run constantly by the runnable r
    public void scan() throws IOException, InterruptedException{
        //while shutdown is false, run the following code
        while(shutdown == false){
            //chetmessage is equal to the cell of the current chatroom the user is in
            chatMessage = getCell("Z"+chatRoom)+"\n\n";
            
            /**
             * if the chatmessage is not equal to the previous chatmessage, then update the text area to show the new message, this check is 
             * so that it doesnt keep printing the same message into the text area over and over again
             */
            if(!chatMessage.equals(prevChatMessage)){
            chatArea.append(chatMessage);
            //this scrolls down automatically as the text area's content gets longer
            chatArea.setCaretPosition(chatArea.getText().length());
            //set the previous chat message equal to the current chat message
            prevChatMessage=chatMessage;
            }
            //WAIT FOR 1200 milliseconds
            Thread.sleep(1200);
        }   
    }
    
    //method close window is what will close the globalChatGUI safely
    public void closeWindow(){
        //set shutdown to true
        shutdown = true;
        //set text in the console to let the user know whats up
        System.out.print("\n\n...Closing messaging GUI...\n\n");
        //dispose the window
        this.dispose();
    }
    
    
    //send method is used to send a chat message to the sheet
    public void send(){
        //string message is set equal to a formatted version of the user's message. it adds the current user's name infront of the message so people know who sent it
        String msg = String.format("[%s]: %s",arcadeGUI.userName, messageText.getText());
        //set the textfield in the window to nothing once the message is sent
        messageText.setText("");
        try {
            //set the cell in the current chatroom to their message
            setCell("Z"+chatRoom,msg);
        } catch (IOException ex) {
            Logger.getLogger(globalChatGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //method chatroom is what determines which chatroom the user is in based on which one they select
    public void chatroom(int room){
        //let the user know which chatroom theyre in
        chatArea.setText("Chat Room "+(room - 1)+":\n\n");
        //make the global chatroom variable equal to the passed room variable in the method
        chatRoom = room;
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        chatRoomSelectionPanel = new javax.swing.JPanel();
        chat1Button = new javax.swing.JButton();
        chat2Button = new javax.swing.JButton();
        chat3Button = new javax.swing.JButton();
        chat5Button = new javax.swing.JButton();
        chat4Button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        messagingPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        messageText = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Messaging");

        chatRoomSelectionPanel.setBackground(new java.awt.Color(0, 0, 0));

        chat1Button.setBackground(new java.awt.Color(0, 0, 0));
        chat1Button.setForeground(new java.awt.Color(0, 0, 0));
        chat1Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat-Room-1 (2).png"))); // NOI18N
        chat1Button.setBorder(null);
        chat1Button.setOpaque(false);
        chat1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat1ButtonActionPerformed(evt);
            }
        });

        chat2Button.setBackground(new java.awt.Color(0, 0, 0));
        chat2Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat-Room-2.png"))); // NOI18N
        chat2Button.setBorder(null);
        chat2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat2ButtonActionPerformed(evt);
            }
        });

        chat3Button.setBackground(new java.awt.Color(0, 0, 0));
        chat3Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat-Room-3.png"))); // NOI18N
        chat3Button.setBorder(null);
        chat3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat3ButtonActionPerformed(evt);
            }
        });

        chat5Button.setBackground(new java.awt.Color(0, 0, 0));
        chat5Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat-Room-5.png"))); // NOI18N
        chat5Button.setBorder(null);
        chat5Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat5ButtonActionPerformed(evt);
            }
        });

        chat4Button.setBackground(new java.awt.Color(0, 0, 0));
        chat4Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat-Room-4.png"))); // NOI18N
        chat4Button.setBorder(null);
        chat4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chat4ButtonActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat-Room-Selection.png"))); // NOI18N

        javax.swing.GroupLayout chatRoomSelectionPanelLayout = new javax.swing.GroupLayout(chatRoomSelectionPanel);
        chatRoomSelectionPanel.setLayout(chatRoomSelectionPanelLayout);
        chatRoomSelectionPanelLayout.setHorizontalGroup(
            chatRoomSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatRoomSelectionPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(chatRoomSelectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(chatRoomSelectionPanelLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(chatRoomSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chat5Button)
                    .addComponent(chat4Button)
                    .addComponent(chat3Button)
                    .addComponent(chat2Button)
                    .addComponent(chat1Button))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        chatRoomSelectionPanelLayout.setVerticalGroup(
            chatRoomSelectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatRoomSelectionPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(chat1Button)
                .addGap(45, 45, 45)
                .addComponent(chat2Button)
                .addGap(45, 45, 45)
                .addComponent(chat3Button)
                .addGap(45, 45, 45)
                .addComponent(chat4Button)
                .addGap(45, 45, 45)
                .addComponent(chat5Button)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        messagingPanel.setBackground(new java.awt.Color(0, 0, 0));

        chatArea.setEditable(false);
        chatArea.setBackground(new java.awt.Color(0, 0, 0));
        chatArea.setColumns(20);
        chatArea.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        chatArea.setForeground(new java.awt.Color(255, 255, 255));
        chatArea.setLineWrap(true);
        chatArea.setRows(5);
        chatArea.setWrapStyleWord(true);
        chatArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane1.setViewportView(chatArea);

        messageText.setBackground(new java.awt.Color(0, 0, 0));
        messageText.setFont(new java.awt.Font("DialogInput", 0, 11)); // NOI18N
        messageText.setForeground(new java.awt.Color(255, 255, 255));
        messageText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        messageText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageTextActionPerformed(evt);
            }
        });
        messageText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageTextKeyPressed(evt);
            }
        });

        sendButton.setBackground(new java.awt.Color(0, 0, 0));
        sendButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send.png"))); // NOI18N
        sendButton.setBorder(null);
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        backButton.setBackground(new java.awt.Color(0, 0, 0));
        backButton.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("Back");
        backButton.setBorder(null);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout messagingPanelLayout = new javax.swing.GroupLayout(messagingPanel);
        messagingPanel.setLayout(messagingPanelLayout);
        messagingPanelLayout.setHorizontalGroup(
            messagingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(messagingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(messagingPanelLayout.createSequentialGroup()
                        .addComponent(messageText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(messagingPanelLayout.createSequentialGroup()
                        .addComponent(backButton)
                        .addGap(0, 366, Short.MAX_VALUE)))
                .addContainerGap())
        );
        messagingPanelLayout.setVerticalGroup(
            messagingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagingPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(messagingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(messageText, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLayeredPane1.setLayer(chatRoomSelectionPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(messagingPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chatRoomSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(messagingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chatRoomSelectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(messagingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void messageTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_messageTextActionPerformed

    private void messageTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageTextKeyPressed
        //if the user presses enter, run the send method
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            send();
        }
    }//GEN-LAST:event_messageTextKeyPressed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        //or if they press the the send button, run the send method.
        send();
    }//GEN-LAST:event_sendButtonActionPerformed

    
    //when the user presses the chatroom 1 button, run the following code
    private void chat1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat1ButtonActionPerformed
        //run the chatroom method, sending it the row of the chatroom, this is confusing but it has to be done this way because of how the spreadsheet is layed out
        chatroom(2);
        //make the chatroom selection screen to be false and the messaging panel to be visible
        chatRoomSelectionPanel.setVisible(false);
        messagingPanel.setVisible(true);
    }//GEN-LAST:event_chat1ButtonActionPerformed

    //same as chatroom 1 but for the following chatroom button, no need to comment
    private void chat2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat2ButtonActionPerformed
        chatroom(3);
        chatRoomSelectionPanel.setVisible(false);
        messagingPanel.setVisible(true);
    }//GEN-LAST:event_chat2ButtonActionPerformed

    //same as chatroom 1 but for the following chatroom button, no need to comment
    private void chat3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat3ButtonActionPerformed
        chatroom(4);
        chatRoomSelectionPanel.setVisible(false);
        messagingPanel.setVisible(true);
    }//GEN-LAST:event_chat3ButtonActionPerformed

    //same as chatroom 1 but for the following chatroom button, no need to comment
    private void chat4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat4ButtonActionPerformed
        chatroom(5);
        chatRoomSelectionPanel.setVisible(false);
        messagingPanel.setVisible(true);
    }//GEN-LAST:event_chat4ButtonActionPerformed

    //same as chatroom 1 but for the following chatroom button, no need to comment
    private void chat5ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chat5ButtonActionPerformed
        chatroom(6);
        chatRoomSelectionPanel.setVisible(false);
        messagingPanel.setVisible(true);
    }//GEN-LAST:event_chat5ButtonActionPerformed

    //when the user presses the back button on the messaging screen, go back to the chatroom selection screen
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        messagingPanel.setVisible(false);
        chatRoomSelectionPanel.setVisible(true);
    }//GEN-LAST:event_backButtonActionPerformed

   
    
    /**
     * @param args the command line arguments
     */
    public void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(globalChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(globalChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(globalChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(globalChatGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
             
                try {
                    new globalChatGUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(globalChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton chat1Button;
    private javax.swing.JButton chat2Button;
    private javax.swing.JButton chat3Button;
    private javax.swing.JButton chat4Button;
    private javax.swing.JButton chat5Button;
    private javax.swing.JTextArea chatArea;
    private javax.swing.JPanel chatRoomSelectionPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField messageText;
    private javax.swing.JPanel messagingPanel;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
