package mainMenu;

//import the games files
import checkers.Checkers;
import u2a5.tictactoe.TicTacGUI;
import spaceIntruders.Game.GameFrame;
import pong.Pong;
import snakes.and.ladders.gameMenu;
import snake.Snake;
import tetris.TetrisISU;


//reading/writing google sheets
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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

//other java imports
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import snake.Snake;
import whackamole.WhackAMole;


/*
 * This is an arcade program that incorporates multiple games created by Mr. Mah's ICS-4U1 class
 *
 * In the program, you can play multiple arcade style games, listen to music, message eachother in chat rooms, and check leaderboards and stats for 
 * everybody that has been playing the arcade games
 * 
 * the program's main backbone, and an essential tool for allowing online stats and usernames, is google's sheets program. 
 * 'Mahcrosoft Arcade' uses google sheets to read and write usernames, scores, and messages into a singular spreadsheet. This online spreadsheet is much more 
 * convenient than a conventional local spreadsheet because it allows anyone with an internet connection and google account to read/write their info.
 *
 * the spreadsheet uses "A1" notation to organize its cells. If i want the first row first column, then i would call it "A1". More info can be found here https://developers.google.com/sheets/api/guides/concepts
 *
 * Created in November, 2017
 * @author Parker Rowe
 */
public class arcadeGUI extends javax.swing.JFrame {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GOOGLE CODE, this code was taken from google's sheets API V4 website as a setup to use it in java. Can be found here: https://developers.google.com/sheets/api/quickstart/java
    private static final String APPLICATION_NAME
            = "Google Sheets API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES
            = Arrays.asList(SheetsScopes.SPREADSHEETS);

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
        InputStream in
                = arcadeMain.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //START OF CODE WRITTEN BY PARKER
    /**
     * method setCell is used to send a message into a specified cell in the
     * online spreadsheet.
     *
     * the method is passed the cell the user wants to send it to, and the
     * message they want to send. Both the cell and message are strings
     */
    public static void setCell(String cell, String msg) throws IOException {
        // Build a new authorized API client service. This tells the google code found above to start a new "sheets service" which will allow the program to talk to the spreadsheet
        Sheets service = getSheetsService();

        //a new string called spreadsheetId is intialized and set equal to the online spreadsheet's ID. This ID is taken from the URL of the online spreadsheet (this is the current one: https://docs.google.com/spreadsheets/d/1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k/edit#gid=0)
        String spreadsheetId = "1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k";

        //new string range is set equal to the cell:cell, is is possible to send a range of cells but for this program we do not need that, so we just set the range to be one cell
        //for example: if i pass setCell() the cell "B2", the range will look like "B2:B2" 
        String range = cell + ":" + cell;

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
    public static String getCell(String cell) throws IOException {
        // Build a new authorized API client service. This tells the google code found above to start a new "sheets service" which will allow the program to talk to the spreadsheet
        Sheets service = getSheetsService();

        //string range is the desired range that the program will use to try and read, this range actually also requires the spreadsheets name, and I have named the spreadsheet playerlist
        String range = String.format("Player List!%s:%s", cell, cell);

        //spreadsheetId is what identifies your google spreadsheet, it is part of the URL (this is the current one: https://docs.google.com/spreadsheets/d/1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k/edit#gid=0)
        String spreadsheetId = "1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k";

        //executes the read request to the sheet using the range
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();

        //the response received from the sheet is inputted into an arraylist of arraylists
        List<List<Object>> values = response.getValues();

        //String str is what is used as the return value for the method, it is initialized to be "" (nothing) and will have the received message added to it later
        String str = "";
        //print to console so the user can see what is actually happening (I, Parker, used this all the time while making the program)
        System.out.println("\nReading cell: " + cell);
        System.out.println("values: " + values);

        //check to see if the returned message is empty (ie, the cell the user chose has nothing in it)
        if (values == null || values.isEmpty()) {
            //let the user know that no data was found
            System.out.println("No data found.");

            //if the values array is NOT empty (ie there is a message!) then run the following code
        } else {
            //sorts through the array and will grab the very first element in the very first arrayList and sets string 'str' equal to that element. 
            for (List row : values) {
                str = row.get(0).toString();
                break;
            }
        }

        //return string str
        return str;
    }

    /**
     * getCellRange is almost identical to getCell, but it is instead passed the
     * variables rowStart and rowEnd, this allows the user to ask for a RANGE of
     * cells instead of just one (like the previous method!)
     *
     * this method also returns an ArrayList of strings because there will be
     * multiple messages
     */
    public static ArrayList<String> getCellRange(String RowStart, String RowEnd) throws IOException {
        // Build a new authorized API client service. This tells the google code found above to start a new "sheets service" which will allow the program to talk to the spreadsheet
        Sheets service = getSheetsService();

        //string range is the desired range that the program will use to try and read, this range actually also requires the spreadsheets name, and I have named the spreadsheet player list
        String range = String.format("Player List!%s:%s", RowStart, RowEnd);

        //spreadsheetId is what identifies your google spreadsheet, it is part of the URL (this is the current one: https://docs.google.com/spreadsheets/d/1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k/edit#gid=0)
        String spreadsheetId = "1E0QQJNhQhs48OnG9NqgHh9NK4LBvK6uUAo3UAkBk23k";

        //executes the read request to the sheet using the range
        ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();

        //the response received from the sheet is inputted into an arraylist of arraylists
        List<List<Object>> values = response.getValues();

        //ArrayList<String> arr is what is used as the return value for the method, it will have things added to it later
        ArrayList<String> arr = new ArrayList();

        //print to console
        System.out.println("\nReading cell: " + (RowStart + ":" + RowEnd));
        System.out.println("values: " + values);

        //checks if the returned message is empty
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");

            //if it is empty, set all the values in the array to be "" (essentially an empty string)
            for (int x = 0; x < arr.size(); x++) {
                arr.add("");
            }

            //if the arraylist is not empty, run the following code
        } else {

            /**
             * run through the arraylist using a NESTED forloop, where it will
             * go through and find each separate message from each separate cell
             * and add each message as a new element in the arraylist
             */
            for (List row : values) {
                for (int rowElement = 0; rowElement < row.size(); rowElement++) {
                    arr.add(row.get(rowElement).toString());
                }
            }
        }

        //return the arraylist
        return arr;
    }

    //method for setting the score of the current game in the sheets document for the current user, the method is passed the desired score (AS A STRING BECAUSE IT MAKES THINGS EASIER)
    public static void setScore(int score) throws IOException {
        /**
         * string cell is determined by finding the letter of the current game
         * (by running the getGameCell method passing it the current game
         * variable) and the current user's row for example: if my name in the
         * spreadsheet is 5 rows down, and im playing game 1 which is found at
         * column C, string cell will look like "C5"
         */
        String cell = getGameCell(currentGame) + currentUser;
        //then run the setcell method passing it cell and score as the range and message
        setCell(cell, score + "");
    }

    //method for getting the score of the current game in the sheets document for the current user
    public static int getScore() throws IOException {
        /**
         * string cell is determined by finding the letter of the current game
         * (by running the getGameCell method passing it the current game
         * variable) and the current user's row for example: if my name in the
         * spreadsheet is 5 rows down, and im playing game 1 which is found at
         * column C, string cell will look like "C5"
         */
        String cell = getGameCell(currentGame) + currentUser;
        //string score is set equal to getCell and is passed the cell string that it needs to find the correct information
        String score = getCell(cell);

        //print to console for debugging
        System.out.println(currentGame + "'s score is: " + score);

        //return the score as a string
        return Integer.parseInt(score);
    }
    
    
    //OVERLOADED method for setting the score of the current game in the sheets document for the specified user, the method is passed the desired score (AS A STRING BECAUSE IT MAKES THINGS EASIER)
    public static void setScore(int score, int userIndex) throws IOException {
        /**
         * string cell is determined by finding the letter of the current game
         * (by running the getGameCell method passing it the current game
         * variable) and the current user's row for example: if my name in the
         * spreadsheet is 5 rows down, and im playing game 1 which is found at
         * column C, string cell will look like "C5"
         */
        String cell = getGameCell(currentGame) + userIndex;
        //then run the setcell method passing it cell and score as the range and message
        setCell(cell, score + "");
    }

    //OVERLOADED method for getting the score of the current game in the sheets document for the specified user
    public static int getScore(int userIndex) throws IOException {
        /**
         * string cell is determined by finding the letter of the current game
         * (by running the getGameCell method passing it the current game
         * variable) and the current user's row for example: if my name in the
         * spreadsheet is 5 rows down, and im playing game 1 which is found at
         * column C, string cell will look like "C5"
         */
        String cell = getGameCell(currentGame) + userIndex;
        //string score is set equal to getCell and is passed the cell string that it needs to find the correct information
        String score = getCell(cell);

        //print to console for debugging
        System.out.println(currentGame + "'s score is: " + score);

        //return the score as a string
        return Integer.parseInt(score);
    }
    

    //method getLeaderboard is a method that is passed the game the user wants, and returns an arraylist of user objects that will contain the usernames and their corresponding scores for the passed game
    public ArrayList<user> getLeaderboard(String game) throws IOException {
        //string cell is set equal to the game's cell that the user passes to the method
        String cell = getGameCell(game);

        //an arraylist of user objects called leaderboard is created
        ArrayList<user> leaderboard = new ArrayList();

        /**
         * two arraylists of strings called scores and names is set equal to the
         * cell range from the spreadsheet
         *
         * scores is set equal to the entire column of the game's cell names is
         * set equal to the entire column of usernames (which is found at column
         * B in the spreadsheet)
         *
         * for example: if i pass "Game 3" to this method, it will go out and
         * set 'cell' to "E", then it goes out and gets the entire "E" column
         * starting at row2, this will make an arraylist of every score for Game
         * 3.
         */
        ArrayList<String> scores = getCellRange(cell + "2", cell);
        ArrayList<String> names = getCellRange("B2", "B");

        /**
         * then a forloop runs through and adds a new user as an element in the
         * leaderboard array
         *
         * an example user object looks like [name, gameScore], so an example
         * user object for me, where i have say 3500 points in a game, would
         * look like [Parker, 3500]
         */
        for (int i = 0; i < scores.size(); i++) {
            leaderboard.add(new user(names.get(i), Integer.parseInt(scores.get(i))));
        }

        //this sorts through the leaderboard arraylist and rearranges it using nested forloops
        for (int i = 0; i < leaderboard.size(); i++) {
            for (int j = 0; j < leaderboard.size() - 1; j++) {

                //this sorts through the leaderboard arraylist and rearrages it from the user with the highest score to the user with the lowest score
                if (leaderboard.get(j + 1).score > leaderboard.get(j).score) {
                    user temp = leaderboard.get(j + 1);
                    leaderboard.set(j + 1, leaderboard.get(j));
                    leaderboard.set(j, temp);
                }
            }
        }

        //returns the sorted leaderboard arraylist so the program can display it nicely in the stats screen
        return leaderboard;
    }

    //method getGameCell is what was used by setScore, getScore, and getLeaderboard to find the the Alphabetical column of the game that the method is passed
    public static String getGameCell(String game) {
        //string cell is intiallized to null to avoid having an error
        String cell = null;

        //it finds the cell by doing a simple switch statement on the passed variable, checking if the game that the method is passed is x, then the cell has to be y
        switch (game) {
            case "Game 1":
                cell = "C";
                break;
            case "Game 2":
                cell = "D";
                break;
            case "Game 3":
                cell = "E";
                break;
            case "Game 4":
                cell = "F";
                break;
            case "Game 5":
                cell = "G";
                break;
            case "Game 6":
                cell = "H";
                break;
            case "Game 7":
                cell = "I";
                break;
            case "Game 8":
                cell = "J";
                break;
            case "Game 9":
                cell = "K";
                break;
            case "Game 10":
                cell = "L";
                break;
            default:
                break;
        }
        //return the cell
        return cell;
    }

    /**
     * CONSTRUCTOR. This is the code that runs when the arcadeGUI java file is
     * run
     */
    public arcadeGUI() {
        //this creates the new custom fonts 'arcadeFont
        arcadeFont54 = loadFont("/arcadePix.ttf").deriveFont(Font.PLAIN, 54);
        arcadeFont14BOLD = loadFont("/arcadePix.ttf").deriveFont(Font.BOLD, 14);
        arcadeFont42 = loadFont("/arcadePix.ttf").deriveFont(Font.PLAIN, 42);

        //expand the program window to be maximized and the size of the screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //run the netbeans generated code to create the jframe
        initComponents();
        //run my hide panels method to hide the panels that the user's dont need to see
        hidePanels();

        try {

            /**
             * this is a check to see if the computer can connect to google
             * sheets. it checks cell z8 which contains the message "ONLINE" if
             * there is a message that is received then set onlineLabel on the
             * main menu to say "ONLINE", and set the color to green. The label
             * says OFFLINE in red by default
             */
            if (getCell("Z8").equals("ONLINE")) {
                onlineStatusLabel.setText("ONLINE");
                onlineStatusLabel.setForeground(new java.awt.Color(0, 255, 0));

                //send CONNECTED into the console for debugging
                System.out.println(". . .CONNECTED. . .\n\n\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //run the findUserID method, this method is what finds the current user's info, or creates a new user if they dont exist already in the spreadsheet
        findUserID();
    }

    //intialize new global variables
    //creates a new font called arcade font, this will import the custom font that the arcade uses so we don't have to install it on each computer
    Font arcadeFont54;
    public static Font arcadeFont14BOLD;
    Font arcadeFont42;

    //method loadFont will be used to load a custom font
    private static Font loadFont(String path) {
        try {
            InputStream is = arcadeGUI.class.getResourceAsStream(path);
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {

        }
        return null;
    }

    //new string currentGame, this variable will be used to store the current game that the user is playing. for example when i launch game 1 this variable will be set to "Game 1"
    static String currentGame;

    //string userID is intialized to be "Player 1" by default, if the user is offline or cant connect to google, then it will stay as player 1 by default, if there is connection it will be changed later
    public static String userID = "Player 1";
    //string userName is also intialized to be "Player 1" by default, if the user is offline or cant connect to google, then it will stay as player 1 by default, if there is connection it will be changed later
    public static String userName = "Player 1";
    //int currentUser will be used to store the row of the current user using the program. For example if i am found in row 5 of the spreadsheet, this variable will be equal to 5
    public static int currentUser;

    //string invadersGameMode will allow the user to change the gamemode of spaceinvaders from the original to spiderman
    String invadersGameMode = "spider";

    //intialization of all the imageicons the program needs, gives them a name and finds the file in the resources folder
    ImageIcon gifSpaceInvaders = new ImageIcon(getClass().getResource("/space-invaders-gif.gif"));
    ImageIcon imgSpaceInvaders = new ImageIcon(getClass().getResource("/space-invaders-img.png"));
    ImageIcon imgPong = new ImageIcon(getClass().getResource("/pong-img.png"));
    ImageIcon gifPong = new ImageIcon(getClass().getResource("/pong-gif.gif"));

    //method hidePanels() is what was run in the constructor; it just sets a bunch of panels to be invisible until it is needed later
    public void hidePanels() {
        settingsScreen.setVisible(false);
        gamesScreen.setVisible(false);
        statsScreen.setVisible(false);
        statsScreenGame1.setVisible(false);
        statsScreenGame2.setVisible(false);
        statsScreenGame3.setVisible(false);
        statsScreenGame4.setVisible(false);
        statsScreenGame5.setVisible(false);
        statsScreenGame6.setVisible(false);
        statsScreenGame7.setVisible(false);
        statsScreenGame8.setVisible(false);
        statsScreenGame9.setVisible(false);
        statsScreenGame10.setVisible(false);
    }

    //method findUserID is also what's run in the constructor, this will seek out and try to find the current user, and if they dont exist in the spreadsheet then crete a new user
    public void findUserID() {
        /**
         * userID is what identifies the user, it finds the user's windows login
         * name as their ID, this sets userID global variable to be equal to the
         * user's login name
         *
         * for example at school, the person's ID will be s+their student
         * number. This will allow people to have their progress saved if they
         * use a different computer throughout the school
         */
        userID = System.getProperty("user.name");

        try {
            //arraylist of strings called IDs is set equal to the entire A column in the spreadsheet starting at row 2.
            ArrayList<String> IDs = getCellRange("A2", "A");

            //check to see if the currentuser's ID actually exists in that IDs array, if they do then run the following code
            if (IDs.contains(userID)) {
                //set the current user's variable equal to the index of the user in the IDs arraylist (plus 2 just because of how the spreadsheet is layed out)
                currentUser = IDs.indexOf(userID) + 2;
                //set the userName variable equal to the current user's row in column B, because column B is where all of the usernames are stored
                userName = getCell("B" + currentUser);
                //send info to the console for debugging
                System.out.println("\nUser Exists . . . ID is: " + userID + ", and username is: " + userName + "\n");

                //if the ID arraylist does not contain the current user's login info, then create a new user
            } else {
                //output to console for debugging
                System.out.println("\nuser doesnt exist . . . creating new user\n");

                //set the currentUser variable equal to the ID arraylist size +2 because that will result in adding a new user to the very end of the list in the spreadsheet
                currentUser = IDs.size() + 2;

                //create a new user in the spreadsheet with their windows login name as their ID and nickname, they can change their nickname later, this will place the new user at the end of the userlsit in the spreadsheet
                setCell("A" + (IDs.size() + 2), userID);
                setCell("B" + (IDs.size() + 2), userID);

                //also set every game's score equal to 0 for a new user
                setCell("C" + currentUser, "0");
                setCell("D" + currentUser, "0");
                setCell("E" + currentUser, "0");
                setCell("F" + currentUser, "0");
                setCell("G" + currentUser, "0");
                setCell("H" + currentUser, "0");
                setCell("I" + currentUser, "0");
                setCell("J" + currentUser, "0");
                setCell("K" + currentUser, "0");
                setCell("L" + currentUser, "0");

                //sets the user's name to their ID for the time being, they can change their userName later in the settings
                userName = userID;

                //output to console for debugging
                System.out.println("ID is: " + userID + ", and username is: " + userName + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(globalChatGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //this method will be used if games wish to add a second (or multiple) player and want to save their score as another member of mahcrosoft arcade, it is passed the user whos index they wish to find and will return an integer
    public static int findUserIndex(String user) throws IOException {
        //new int userindex is by default set to -1
        int userIndex = -1;

        //arraylist of string called names is equal to the entire B column starting at row 2
        ArrayList<String> names = getCellRange("B2", "B");

        //if the arraylist contains the user then run this code
        if (names.contains(user)) {
            //userindex is set equal to the index of their name in the arraylist +2
            userIndex = names.indexOf(user) + 2;
            //print to console for debugging
            System.out.println("\nUser Exists . . . name is: " + user + " at row: " + userIndex + "\n");

            //if the user does not exist in the array
        } else {
            //then print to console letting the user know they cant do that
            System.out.println("User does not exist. . .  check spelling or make sure this user is actually a member of mahcrosoft arcade");
        }

        //return the userIndex integer
        return userIndex;
    }

    //method newUserName is a method used by the changeUserNameGUI and it is passed the new name the user wishes to have as their username, THIS DOES NOT CHANGE THEIR ID THOUGH
    public static void newUserName(String newName) throws IOException {
        //set the cell B+currentuser equal to the user's desired username by passing the newname as the message
        setCell(("B" + currentUser), newName);
        //global variable userName is set the user's new name
        userName = newName;
        //print to console for debugging
        System.out.println("\n\nchanged userName . . . userName = " + userName);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layeredPane = new javax.swing.JLayeredPane();
        homeScreen = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        holderPanel = new javax.swing.JPanel();
        buttonsPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        messagingButton = new javax.swing.JButton();
        statsButton = new javax.swing.JButton();
        musicButton = new javax.swing.JButton();
        settingExitHolder = new javax.swing.JPanel();
        settingsButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        onlineStatusLabel = new javax.swing.JLabel();
        settingsScreen = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        settingsBackButton = new javax.swing.JButton();
        changeUserNameButton = new javax.swing.JButton();
        invadersGameModeButton = new javax.swing.JButton();
        invadersGameModeLabel = new javax.swing.JLabel();
        gamesScreen = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        gamesBackButton = new javax.swing.JButton();
        gamesPanel = new javax.swing.JPanel();
        game1 = new javax.swing.JPanel();
        button1 = new javax.swing.JButton();
        image1 = new javax.swing.JPanel();
        gameIcon1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        game2 = new javax.swing.JPanel();
        button2 = new javax.swing.JButton();
        image2 = new javax.swing.JPanel();
        gameIcon2 = new javax.swing.JLabel();
        game3 = new javax.swing.JPanel();
        button3 = new javax.swing.JButton();
        image3 = new javax.swing.JPanel();
        gameIcon3 = new javax.swing.JLabel();
        game4 = new javax.swing.JPanel();
        button4 = new javax.swing.JButton();
        image4 = new javax.swing.JPanel();
        gameIcon4 = new javax.swing.JLabel();
        game5 = new javax.swing.JPanel();
        button5 = new javax.swing.JButton();
        image5 = new javax.swing.JPanel();
        gameIcon5 = new javax.swing.JLabel();
        game6 = new javax.swing.JPanel();
        button6 = new javax.swing.JButton();
        image6 = new javax.swing.JPanel();
        gameIcon6 = new javax.swing.JLabel();
        game7 = new javax.swing.JPanel();
        button7 = new javax.swing.JButton();
        image7 = new javax.swing.JPanel();
        gameIcon7 = new javax.swing.JLabel();
        game8 = new javax.swing.JPanel();
        button8 = new javax.swing.JButton();
        image8 = new javax.swing.JPanel();
        gameIcon8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        game9 = new javax.swing.JPanel();
        button9 = new javax.swing.JButton();
        image9 = new javax.swing.JPanel();
        gameIcon9 = new javax.swing.JLabel();
        game10 = new javax.swing.JPanel();
        button10 = new javax.swing.JButton();
        image10 = new javax.swing.JPanel();
        gameIcon10 = new javax.swing.JLabel();
        statsScreen = new javax.swing.JLayeredPane();
        statsScreenMain = new javax.swing.JPanel();
        statsBackButton = new javax.swing.JButton();
        statsLabel = new javax.swing.JLabel();
        statsGamesPanel = new javax.swing.JPanel();
        statsGame1 = new javax.swing.JPanel();
        statsGameButton1 = new javax.swing.JButton();
        statsGameImage1 = new javax.swing.JPanel();
        statsGameIcon1 = new javax.swing.JLabel();
        statsGame2 = new javax.swing.JPanel();
        statsGameButton2 = new javax.swing.JButton();
        statsGameImage2 = new javax.swing.JPanel();
        statsGameIcon2 = new javax.swing.JLabel();
        statsGame3 = new javax.swing.JPanel();
        statsGameButton3 = new javax.swing.JButton();
        statsGameImage3 = new javax.swing.JPanel();
        statsGameIcon3 = new javax.swing.JLabel();
        statsGame4 = new javax.swing.JPanel();
        statsGameButton4 = new javax.swing.JButton();
        statsGameImage4 = new javax.swing.JPanel();
        statsGameIcon4 = new javax.swing.JLabel();
        statsGame5 = new javax.swing.JPanel();
        statsGameButton5 = new javax.swing.JButton();
        statsGameImage5 = new javax.swing.JPanel();
        statsGameIcon5 = new javax.swing.JLabel();
        statsGame6 = new javax.swing.JPanel();
        statsGameButton6 = new javax.swing.JButton();
        statsGameImage6 = new javax.swing.JPanel();
        statsGameIcon6 = new javax.swing.JLabel();
        statsGame7 = new javax.swing.JPanel();
        statsGameButton7 = new javax.swing.JButton();
        statsGameImage7 = new javax.swing.JPanel();
        statsGameIcon7 = new javax.swing.JLabel();
        statsGame8 = new javax.swing.JPanel();
        statsGameButton8 = new javax.swing.JButton();
        statsGameImage8 = new javax.swing.JPanel();
        statsGameIcon8 = new javax.swing.JLabel();
        statsGame9 = new javax.swing.JPanel();
        statsGameButton9 = new javax.swing.JButton();
        statsGameImage9 = new javax.swing.JPanel();
        statsGameIcon9 = new javax.swing.JLabel();
        statsGame10 = new javax.swing.JPanel();
        statsGameButton10 = new javax.swing.JButton();
        statsGameImage10 = new javax.swing.JPanel();
        statsGameIcon10 = new javax.swing.JLabel();
        statsScreenGame1 = new javax.swing.JPanel();
        statsBackButtonGame1 = new javax.swing.JButton();
        statsLabelGame1 = new javax.swing.JLabel();
        showFullGame1 = new javax.swing.JButton();
        rankPanel1 = new javax.swing.JPanel();
        rankLabel1 = new javax.swing.JLabel();
        rankOne1 = new javax.swing.JLabel();
        rankTwo1 = new javax.swing.JLabel();
        rankThree1 = new javax.swing.JLabel();
        rankFour1 = new javax.swing.JLabel();
        rankFive1 = new javax.swing.JLabel();
        scorePanel1 = new javax.swing.JPanel();
        scoreLabel1 = new javax.swing.JLabel();
        scoreOne1 = new javax.swing.JLabel();
        scoreTwo1 = new javax.swing.JLabel();
        scoreThree1 = new javax.swing.JLabel();
        scoreFour1 = new javax.swing.JLabel();
        scoreFive1 = new javax.swing.JLabel();
        namePanel1 = new javax.swing.JPanel();
        nameLabel1 = new javax.swing.JLabel();
        userOne1 = new javax.swing.JLabel();
        userTwo1 = new javax.swing.JLabel();
        userThree1 = new javax.swing.JLabel();
        userFour1 = new javax.swing.JLabel();
        userFive1 = new javax.swing.JLabel();
        statsScreenGame2 = new javax.swing.JPanel();
        statsBackButtonGame2 = new javax.swing.JButton();
        statsLabelGame2 = new javax.swing.JLabel();
        showFullGame2 = new javax.swing.JButton();
        rankPanel2 = new javax.swing.JPanel();
        rankLabel2 = new javax.swing.JLabel();
        rankOne2 = new javax.swing.JLabel();
        rankTwo2 = new javax.swing.JLabel();
        rankThree2 = new javax.swing.JLabel();
        rankFour2 = new javax.swing.JLabel();
        rankFive2 = new javax.swing.JLabel();
        scorePanel2 = new javax.swing.JPanel();
        scoreLabel2 = new javax.swing.JLabel();
        scoreOne2 = new javax.swing.JLabel();
        scoreTwo2 = new javax.swing.JLabel();
        scoreThree2 = new javax.swing.JLabel();
        scoreFour2 = new javax.swing.JLabel();
        scoreFive2 = new javax.swing.JLabel();
        namePanel2 = new javax.swing.JPanel();
        nameLabel2 = new javax.swing.JLabel();
        userOne2 = new javax.swing.JLabel();
        userTwo2 = new javax.swing.JLabel();
        userThree2 = new javax.swing.JLabel();
        userFour2 = new javax.swing.JLabel();
        userFive2 = new javax.swing.JLabel();
        statsScreenGame3 = new javax.swing.JPanel();
        statsBackButtonGame3 = new javax.swing.JButton();
        statsLabelGame3 = new javax.swing.JLabel();
        showFullGame3 = new javax.swing.JButton();
        rankPanel3 = new javax.swing.JPanel();
        rankLabel3 = new javax.swing.JLabel();
        rankOne3 = new javax.swing.JLabel();
        rankTwo3 = new javax.swing.JLabel();
        rankThree3 = new javax.swing.JLabel();
        rankFour3 = new javax.swing.JLabel();
        rankFive3 = new javax.swing.JLabel();
        scorePanel3 = new javax.swing.JPanel();
        scoreLabel3 = new javax.swing.JLabel();
        scoreOne3 = new javax.swing.JLabel();
        scoreTwo3 = new javax.swing.JLabel();
        scoreThree3 = new javax.swing.JLabel();
        scoreFour3 = new javax.swing.JLabel();
        scoreFive3 = new javax.swing.JLabel();
        namePanel3 = new javax.swing.JPanel();
        nameLabel3 = new javax.swing.JLabel();
        userOne3 = new javax.swing.JLabel();
        userTwo3 = new javax.swing.JLabel();
        userThree3 = new javax.swing.JLabel();
        userFour3 = new javax.swing.JLabel();
        userFive3 = new javax.swing.JLabel();
        statsScreenGame4 = new javax.swing.JPanel();
        statsBackButtonGame4 = new javax.swing.JButton();
        statsLabelGame4 = new javax.swing.JLabel();
        showFullGame4 = new javax.swing.JButton();
        rankPanel4 = new javax.swing.JPanel();
        rankLabel4 = new javax.swing.JLabel();
        rankOne4 = new javax.swing.JLabel();
        rankTwo4 = new javax.swing.JLabel();
        rankThree4 = new javax.swing.JLabel();
        rankFour4 = new javax.swing.JLabel();
        rankFive4 = new javax.swing.JLabel();
        scorePanel4 = new javax.swing.JPanel();
        scoreLabel4 = new javax.swing.JLabel();
        scoreOne4 = new javax.swing.JLabel();
        scoreTwo4 = new javax.swing.JLabel();
        scoreThree4 = new javax.swing.JLabel();
        scoreFour4 = new javax.swing.JLabel();
        scoreFive4 = new javax.swing.JLabel();
        namePanel4 = new javax.swing.JPanel();
        nameLabel4 = new javax.swing.JLabel();
        userOne4 = new javax.swing.JLabel();
        userTwo4 = new javax.swing.JLabel();
        userThree4 = new javax.swing.JLabel();
        userFour4 = new javax.swing.JLabel();
        userFive4 = new javax.swing.JLabel();
        statsScreenGame5 = new javax.swing.JPanel();
        statsBackButtonGame5 = new javax.swing.JButton();
        statsLabelGame5 = new javax.swing.JLabel();
        showFullGame5 = new javax.swing.JButton();
        rankPanel5 = new javax.swing.JPanel();
        rankLabel5 = new javax.swing.JLabel();
        rankOne5 = new javax.swing.JLabel();
        rankTwo5 = new javax.swing.JLabel();
        rankThree5 = new javax.swing.JLabel();
        rankFour5 = new javax.swing.JLabel();
        rankFive5 = new javax.swing.JLabel();
        scorePanel5 = new javax.swing.JPanel();
        scoreLabel5 = new javax.swing.JLabel();
        scoreOne5 = new javax.swing.JLabel();
        scoreTwo5 = new javax.swing.JLabel();
        scoreThree5 = new javax.swing.JLabel();
        scoreFour5 = new javax.swing.JLabel();
        scoreFive5 = new javax.swing.JLabel();
        namePanel5 = new javax.swing.JPanel();
        nameLabel5 = new javax.swing.JLabel();
        userOne5 = new javax.swing.JLabel();
        userTwo5 = new javax.swing.JLabel();
        userThree5 = new javax.swing.JLabel();
        userFour5 = new javax.swing.JLabel();
        userFive5 = new javax.swing.JLabel();
        statsScreenGame6 = new javax.swing.JPanel();
        statsBackButtonGame6 = new javax.swing.JButton();
        statsLabelGame6 = new javax.swing.JLabel();
        showFullGame6 = new javax.swing.JButton();
        rankPanel6 = new javax.swing.JPanel();
        rankLabel6 = new javax.swing.JLabel();
        rankOne6 = new javax.swing.JLabel();
        rankTwo6 = new javax.swing.JLabel();
        rankThree6 = new javax.swing.JLabel();
        rankFour6 = new javax.swing.JLabel();
        rankFive6 = new javax.swing.JLabel();
        scorePanel6 = new javax.swing.JPanel();
        scoreLabel6 = new javax.swing.JLabel();
        scoreOne6 = new javax.swing.JLabel();
        scoreTwo6 = new javax.swing.JLabel();
        scoreThree6 = new javax.swing.JLabel();
        scoreFour6 = new javax.swing.JLabel();
        scoreFive6 = new javax.swing.JLabel();
        namePanel6 = new javax.swing.JPanel();
        nameLabel6 = new javax.swing.JLabel();
        userOne6 = new javax.swing.JLabel();
        userTwo6 = new javax.swing.JLabel();
        userThree6 = new javax.swing.JLabel();
        userFour6 = new javax.swing.JLabel();
        userFive6 = new javax.swing.JLabel();
        statsScreenGame7 = new javax.swing.JPanel();
        statsBackButtonGame7 = new javax.swing.JButton();
        statsLabelGame7 = new javax.swing.JLabel();
        showFullGame7 = new javax.swing.JButton();
        rankPanel7 = new javax.swing.JPanel();
        rankLabel7 = new javax.swing.JLabel();
        rankOne7 = new javax.swing.JLabel();
        rankTwo7 = new javax.swing.JLabel();
        rankThree7 = new javax.swing.JLabel();
        rankFour7 = new javax.swing.JLabel();
        rankFive7 = new javax.swing.JLabel();
        scorePanel7 = new javax.swing.JPanel();
        scoreLabel7 = new javax.swing.JLabel();
        scoreOne7 = new javax.swing.JLabel();
        scoreTwo7 = new javax.swing.JLabel();
        scoreThree7 = new javax.swing.JLabel();
        scoreFour7 = new javax.swing.JLabel();
        scoreFive7 = new javax.swing.JLabel();
        namePanel7 = new javax.swing.JPanel();
        nameLabel7 = new javax.swing.JLabel();
        userOne7 = new javax.swing.JLabel();
        userTwo7 = new javax.swing.JLabel();
        userThree7 = new javax.swing.JLabel();
        userFour7 = new javax.swing.JLabel();
        userFive7 = new javax.swing.JLabel();
        statsScreenGame8 = new javax.swing.JPanel();
        statsBackButtonGame8 = new javax.swing.JButton();
        statsLabelGame8 = new javax.swing.JLabel();
        showFullGame8 = new javax.swing.JButton();
        rankPanel8 = new javax.swing.JPanel();
        rankLabel8 = new javax.swing.JLabel();
        rankOne8 = new javax.swing.JLabel();
        rankTwo8 = new javax.swing.JLabel();
        rankThree8 = new javax.swing.JLabel();
        rankFour8 = new javax.swing.JLabel();
        rankFive8 = new javax.swing.JLabel();
        scorePanel8 = new javax.swing.JPanel();
        scoreLabel8 = new javax.swing.JLabel();
        scoreOne8 = new javax.swing.JLabel();
        scoreTwo8 = new javax.swing.JLabel();
        scoreThree8 = new javax.swing.JLabel();
        scoreFour8 = new javax.swing.JLabel();
        scoreFive8 = new javax.swing.JLabel();
        namePanel8 = new javax.swing.JPanel();
        nameLabel8 = new javax.swing.JLabel();
        userOne8 = new javax.swing.JLabel();
        userTwo8 = new javax.swing.JLabel();
        userThree8 = new javax.swing.JLabel();
        userFour8 = new javax.swing.JLabel();
        userFive8 = new javax.swing.JLabel();
        statsScreenGame9 = new javax.swing.JPanel();
        statsBackButtonGame9 = new javax.swing.JButton();
        statsLabelGame9 = new javax.swing.JLabel();
        showFullGame9 = new javax.swing.JButton();
        rankPanel9 = new javax.swing.JPanel();
        rankLabel9 = new javax.swing.JLabel();
        rankOne9 = new javax.swing.JLabel();
        rankTwo9 = new javax.swing.JLabel();
        rankThree9 = new javax.swing.JLabel();
        rankFour9 = new javax.swing.JLabel();
        rankFive9 = new javax.swing.JLabel();
        scorePanel9 = new javax.swing.JPanel();
        scoreLabel9 = new javax.swing.JLabel();
        scoreOne9 = new javax.swing.JLabel();
        scoreTwo9 = new javax.swing.JLabel();
        scoreThree9 = new javax.swing.JLabel();
        scoreFour9 = new javax.swing.JLabel();
        scoreFive9 = new javax.swing.JLabel();
        namePanel9 = new javax.swing.JPanel();
        nameLabel9 = new javax.swing.JLabel();
        userOne9 = new javax.swing.JLabel();
        userTwo9 = new javax.swing.JLabel();
        userThree9 = new javax.swing.JLabel();
        userFour9 = new javax.swing.JLabel();
        userFive9 = new javax.swing.JLabel();
        statsScreenGame10 = new javax.swing.JPanel();
        statsBackButtonGame10 = new javax.swing.JButton();
        statsLabelGame10 = new javax.swing.JLabel();
        showFullGame10 = new javax.swing.JButton();
        rankPanel10 = new javax.swing.JPanel();
        rankLabel10 = new javax.swing.JLabel();
        rankOne10 = new javax.swing.JLabel();
        rankTwo10 = new javax.swing.JLabel();
        rankThree10 = new javax.swing.JLabel();
        rankFour10 = new javax.swing.JLabel();
        rankFive10 = new javax.swing.JLabel();
        scorePanel10 = new javax.swing.JPanel();
        scoreLabel10 = new javax.swing.JLabel();
        scoreOne10 = new javax.swing.JLabel();
        scoreTwo10 = new javax.swing.JLabel();
        scoreThree10 = new javax.swing.JLabel();
        scoreFour10 = new javax.swing.JLabel();
        scoreFive10 = new javax.swing.JLabel();
        namePanel10 = new javax.swing.JPanel();
        nameLabel10 = new javax.swing.JLabel();
        userOne10 = new javax.swing.JLabel();
        userTwo10 = new javax.swing.JLabel();
        userThree10 = new javax.swing.JLabel();
        userFour10 = new javax.swing.JLabel();
        userFive10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mahcrosoft Arcade");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/MahIcon.png")));
        setSize(new java.awt.Dimension(1280, 1024));

        homeScreen.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo.png"))); // NOI18N

        holderPanel.setBackground(new java.awt.Color(0, 0, 0));

        buttonsPanel.setBackground(new java.awt.Color(0, 0, 0));
        buttonsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        playButton.setBackground(new java.awt.Color(0, 0, 0));
        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/play.png"))); // NOI18N
        playButton.setBorder(null);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        messagingButton.setBackground(new java.awt.Color(0, 0, 0));
        messagingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/messaging.png"))); // NOI18N
        messagingButton.setBorder(null);
        messagingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messagingButtonActionPerformed(evt);
            }
        });

        statsButton.setBackground(new java.awt.Color(0, 0, 0));
        statsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stats.png"))); // NOI18N
        statsButton.setBorder(null);
        statsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsButtonActionPerformed(evt);
            }
        });

        musicButton.setBackground(new java.awt.Color(0, 0, 0));
        musicButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/music-medium.png"))); // NOI18N
        musicButton.setBorder(null);
        musicButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                musicButtonActionPerformed(evt);
            }
        });

        settingExitHolder.setBackground(new java.awt.Color(0, 0, 0));

        settingsButton.setBackground(new java.awt.Color(0, 0, 0));
        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/settings.png"))); // NOI18N
        settingsButton.setBorder(null);
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });

        exitButton.setBackground(new java.awt.Color(0, 0, 0));
        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit-small.png"))); // NOI18N
        exitButton.setBorder(null);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingExitHolderLayout = new javax.swing.GroupLayout(settingExitHolder);
        settingExitHolder.setLayout(settingExitHolderLayout);
        settingExitHolderLayout.setHorizontalGroup(
            settingExitHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingExitHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exitButton)
                .addContainerGap())
        );
        settingExitHolderLayout.setVerticalGroup(
            settingExitHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingExitHolderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(settingExitHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(exitButton)
                    .addComponent(settingsButton))
                .addContainerGap())
        );

        onlineStatusLabel.setFont(new java.awt.Font("DialogInput", 0, 12)); // NOI18N
        onlineStatusLabel.setForeground(new java.awt.Color(204, 0, 0));
        onlineStatusLabel.setText("OFFLINE");

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                        .addGap(0, 38, Short.MAX_VALUE)
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(musicButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(playButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(statsButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(messagingButton, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(0, 39, Short.MAX_VALUE))
                    .addComponent(settingExitHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(onlineStatusLabel))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(playButton)
                .addGap(28, 28, 28)
                .addComponent(messagingButton)
                .addGap(18, 18, 18)
                .addComponent(statsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(musicButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(settingExitHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(onlineStatusLabel))
        );

        javax.swing.GroupLayout holderPanelLayout = new javax.swing.GroupLayout(holderPanel);
        holderPanel.setLayout(holderPanelLayout);
        holderPanelLayout.setHorizontalGroup(
            holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(holderPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        holderPanelLayout.setVerticalGroup(
            holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(holderPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout homeScreenLayout = new javax.swing.GroupLayout(homeScreen);
        homeScreen.setLayout(homeScreenLayout);
        homeScreenLayout.setHorizontalGroup(
            homeScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeScreenLayout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addGroup(homeScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(holderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        homeScreenLayout.setVerticalGroup(
            homeScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(78, 78, 78)
                .addComponent(holderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        settingsScreen.setBackground(new java.awt.Color(0, 0, 0));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/settingsLARGE.png"))); // NOI18N

        settingsBackButton.setBackground(new java.awt.Color(0, 0, 0));
        settingsBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        settingsBackButton.setBorder(null);
        settingsBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsBackButtonActionPerformed(evt);
            }
        });

        changeUserNameButton.setBackground(new java.awt.Color(0, 0, 0));
        changeUserNameButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/changeUsername.png"))); // NOI18N
        changeUserNameButton.setBorder(null);
        changeUserNameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeUserNameButtonActionPerformed(evt);
            }
        });

        invadersGameModeButton.setBackground(new java.awt.Color(0, 0, 0));
        invadersGameModeButton.setFont(arcadeFont42);
        invadersGameModeButton.setForeground(new java.awt.Color(255, 255, 255));
        invadersGameModeButton.setText("Invaders Game Mode:");
        invadersGameModeButton.setBorder(null);
        invadersGameModeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invadersGameModeButtonActionPerformed(evt);
            }
        });

        invadersGameModeLabel.setFont(arcadeFont14BOLD);
        invadersGameModeLabel.setForeground(new java.awt.Color(255, 255, 102));
        invadersGameModeLabel.setText("spider invaders [2P]");

        javax.swing.GroupLayout settingsScreenLayout = new javax.swing.GroupLayout(settingsScreen);
        settingsScreen.setLayout(settingsScreenLayout);
        settingsScreenLayout.setHorizontalGroup(
            settingsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsScreenLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(settingsBackButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(settingsScreenLayout.createSequentialGroup()
                .addContainerGap(403, Short.MAX_VALUE)
                .addGroup(settingsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsScreenLayout.createSequentialGroup()
                        .addComponent(invadersGameModeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(invadersGameModeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(changeUserNameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        settingsScreenLayout.setVerticalGroup(
            settingsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsScreenLayout.createSequentialGroup()
                .addGroup(settingsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsScreenLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(settingsBackButton))
                    .addGroup(settingsScreenLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel2)))
                .addGap(79, 79, 79)
                .addComponent(changeUserNameButton)
                .addGap(51, 51, 51)
                .addGroup(settingsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(invadersGameModeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invadersGameModeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1196, Short.MAX_VALUE))
        );

        gamesScreen.setBackground(new java.awt.Color(0, 0, 0));
        gamesScreen.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                gamesScreenMouseMoved(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GamesLarge.png"))); // NOI18N

        gamesBackButton.setBackground(new java.awt.Color(0, 0, 0));
        gamesBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        gamesBackButton.setBorder(null);
        gamesBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gamesBackButtonActionPerformed(evt);
            }
        });

        gamesPanel.setBackground(new java.awt.Color(0, 0, 0));

        game1.setBackground(new java.awt.Color(0, 0, 0));
        game1.setPreferredSize(new java.awt.Dimension(180, 200));
        game1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game1MouseMoved(evt);
            }
        });

        button1.setBackground(new java.awt.Color(0, 0, 0));
        button1.setFont(arcadeFont14BOLD);
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Space Invaders");
        button1.setBorder(null);
        button1.setPreferredSize(new java.awt.Dimension(141, 20));
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        image1.setBackground(new java.awt.Color(0, 0, 0));
        image1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image1.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/space-invaders-img.png"))); // NOI18N

        javax.swing.GroupLayout image1Layout = new javax.swing.GroupLayout(image1);
        image1.setLayout(image1Layout);
        image1Layout.setHorizontalGroup(
            image1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image1Layout.setVerticalGroup(
            image1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        jLabel5.setFont(arcadeFont14BOLD);
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("[1080p only]");

        javax.swing.GroupLayout game1Layout = new javax.swing.GroupLayout(game1);
        game1.setLayout(game1Layout);
        game1Layout.setHorizontalGroup(
            game1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(game1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(game1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(game1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(image1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game1Layout.setVerticalGroup(
            game1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(image1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        game2.setBackground(new java.awt.Color(0, 0, 0));
        game2.setPreferredSize(new java.awt.Dimension(180, 200));
        game2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game2MouseMoved(evt);
            }
        });

        button2.setBackground(new java.awt.Color(0, 0, 0));
        button2.setFont(arcadeFont14BOLD);
        button2.setForeground(new java.awt.Color(255, 255, 255));
        button2.setText("Tic Tac Toe");
        button2.setBorder(null);
        button2.setPreferredSize(new java.awt.Dimension(141, 20));
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        image2.setBackground(new java.awt.Color(0, 0, 0));
        image2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image2.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tictactoe-img.png"))); // NOI18N

        javax.swing.GroupLayout image2Layout = new javax.swing.GroupLayout(image2);
        image2.setLayout(image2Layout);
        image2Layout.setHorizontalGroup(
            image2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        image2Layout.setVerticalGroup(
            image2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game2Layout = new javax.swing.GroupLayout(game2);
        game2.setLayout(game2Layout);
        game2Layout.setHorizontalGroup(
            game2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(game2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(game2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(image2, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        game2Layout.setVerticalGroup(
            game2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game2Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        game3.setBackground(new java.awt.Color(0, 0, 0));
        game3.setPreferredSize(new java.awt.Dimension(180, 200));
        game3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game3MouseMoved(evt);
            }
        });

        button3.setBackground(new java.awt.Color(0, 0, 0));
        button3.setFont(arcadeFont14BOLD);
        button3.setForeground(new java.awt.Color(255, 255, 255));
        button3.setText("Pong");
        button3.setBorder(null);
        button3.setPreferredSize(new java.awt.Dimension(141, 20));
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        image3.setBackground(new java.awt.Color(0, 0, 0));
        image3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image3.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pong-img.png"))); // NOI18N

        javax.swing.GroupLayout image3Layout = new javax.swing.GroupLayout(image3);
        image3.setLayout(image3Layout);
        image3Layout.setHorizontalGroup(
            image3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image3Layout.setVerticalGroup(
            image3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game3Layout = new javax.swing.GroupLayout(game3);
        game3.setLayout(game3Layout);
        game3Layout.setHorizontalGroup(
            game3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game3Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(game3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game3Layout.setVerticalGroup(
            game3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game3Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        game4.setBackground(new java.awt.Color(0, 0, 0));
        game4.setPreferredSize(new java.awt.Dimension(180, 200));
        game4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game4MouseMoved(evt);
            }
        });

        button4.setBackground(new java.awt.Color(0, 0, 0));
        button4.setFont(arcadeFont14BOLD);
        button4.setForeground(new java.awt.Color(255, 255, 255));
        button4.setText("Snakes & Ladders");
        button4.setBorder(null);
        button4.setPreferredSize(new java.awt.Dimension(141, 20));
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        image4.setBackground(new java.awt.Color(0, 0, 0));
        image4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image4.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/snakesAndLadders-img.png"))); // NOI18N

        javax.swing.GroupLayout image4Layout = new javax.swing.GroupLayout(image4);
        image4.setLayout(image4Layout);
        image4Layout.setHorizontalGroup(
            image4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image4Layout.setVerticalGroup(
            image4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game4Layout = new javax.swing.GroupLayout(game4);
        game4.setLayout(game4Layout);
        game4Layout.setHorizontalGroup(
            game4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game4Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(game4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game4Layout.setVerticalGroup(
            game4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game4Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        game5.setBackground(new java.awt.Color(0, 0, 0));
        game5.setPreferredSize(new java.awt.Dimension(180, 200));
        game5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game5MouseMoved(evt);
            }
        });

        button5.setBackground(new java.awt.Color(0, 0, 0));
        button5.setFont(arcadeFont14BOLD);
        button5.setForeground(new java.awt.Color(255, 255, 255));
        button5.setText("Tanks A Lot");
        button5.setBorder(null);
        button5.setPreferredSize(new java.awt.Dimension(141, 20));
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        image5.setBackground(new java.awt.Color(0, 0, 0));
        image5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image5.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tanks-img.png"))); // NOI18N

        javax.swing.GroupLayout image5Layout = new javax.swing.GroupLayout(image5);
        image5.setLayout(image5Layout);
        image5Layout.setHorizontalGroup(
            image5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon5, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image5Layout.setVerticalGroup(
            image5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon5, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game5Layout = new javax.swing.GroupLayout(game5);
        game5.setLayout(game5Layout);
        game5Layout.setHorizontalGroup(
            game5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game5Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(game5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game5Layout.setVerticalGroup(
            game5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game5Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        game6.setBackground(new java.awt.Color(0, 0, 0));
        game6.setPreferredSize(new java.awt.Dimension(180, 200));
        game6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game6MouseMoved(evt);
            }
        });

        button6.setBackground(new java.awt.Color(0, 0, 0));
        button6.setFont(arcadeFont14BOLD);
        button6.setForeground(new java.awt.Color(255, 255, 255));
        button6.setText("Checkers");
        button6.setBorder(null);
        button6.setPreferredSize(new java.awt.Dimension(141, 20));
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button6ActionPerformed(evt);
            }
        });

        image6.setBackground(new java.awt.Color(0, 0, 0));
        image6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image6.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/checkers-img.png"))); // NOI18N

        javax.swing.GroupLayout image6Layout = new javax.swing.GroupLayout(image6);
        image6.setLayout(image6Layout);
        image6Layout.setHorizontalGroup(
            image6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image6Layout.setVerticalGroup(
            image6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game6Layout = new javax.swing.GroupLayout(game6);
        game6.setLayout(game6Layout);
        game6Layout.setHorizontalGroup(
            game6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game6Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(game6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game6Layout.setVerticalGroup(
            game6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game6Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        game7.setBackground(new java.awt.Color(0, 0, 0));
        game7.setPreferredSize(new java.awt.Dimension(180, 200));
        game7.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game7MouseMoved(evt);
            }
        });

        button7.setBackground(new java.awt.Color(0, 0, 0));
        button7.setFont(arcadeFont14BOLD);
        button7.setForeground(new java.awt.Color(255, 255, 255));
        button7.setText("Snake");
        button7.setBorder(null);
        button7.setPreferredSize(new java.awt.Dimension(141, 20));
        button7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button7ActionPerformed(evt);
            }
        });

        image7.setBackground(new java.awt.Color(0, 0, 0));
        image7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image7.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/snake-img.png"))); // NOI18N

        javax.swing.GroupLayout image7Layout = new javax.swing.GroupLayout(image7);
        image7.setLayout(image7Layout);
        image7Layout.setHorizontalGroup(
            image7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon7, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image7Layout.setVerticalGroup(
            image7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon7, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game7Layout = new javax.swing.GroupLayout(game7);
        game7.setLayout(game7Layout);
        game7Layout.setHorizontalGroup(
            game7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game7Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(game7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game7Layout.setVerticalGroup(
            game7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game7Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        game8.setBackground(new java.awt.Color(0, 0, 0));
        game8.setPreferredSize(new java.awt.Dimension(180, 200));
        game8.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game8MouseMoved(evt);
            }
        });

        button8.setBackground(new java.awt.Color(0, 0, 0));
        button8.setFont(arcadeFont14BOLD);
        button8.setForeground(new java.awt.Color(255, 255, 255));
        button8.setText("Tetris");
        button8.setBorder(null);
        button8.setPreferredSize(new java.awt.Dimension(141, 20));
        button8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button8ActionPerformed(evt);
            }
        });

        image8.setBackground(new java.awt.Color(0, 0, 0));
        image8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image8.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tetris-img.png"))); // NOI18N

        javax.swing.GroupLayout image8Layout = new javax.swing.GroupLayout(image8);
        image8.setLayout(image8Layout);
        image8Layout.setHorizontalGroup(
            image8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        image8Layout.setVerticalGroup(
            image8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon8, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        jLabel4.setFont(arcadeFont14BOLD);
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("[1080p only]");

        javax.swing.GroupLayout game8Layout = new javax.swing.GroupLayout(game8);
        game8.setLayout(game8Layout);
        game8Layout.setHorizontalGroup(
            game8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(game8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(image8, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(button8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        game8Layout.setVerticalGroup(
            game8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(image8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        game9.setBackground(new java.awt.Color(0, 0, 0));
        game9.setPreferredSize(new java.awt.Dimension(180, 200));
        game9.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game9MouseMoved(evt);
            }
        });

        button9.setBackground(new java.awt.Color(0, 0, 0));
        button9.setFont(arcadeFont14BOLD);
        button9.setForeground(new java.awt.Color(255, 255, 255));
        button9.setText("Hangman");
        button9.setBorder(null);
        button9.setPreferredSize(new java.awt.Dimension(141, 20));
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9ActionPerformed(evt);
            }
        });

        image9.setBackground(new java.awt.Color(0, 0, 0));
        image9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image9.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hangman-img.png"))); // NOI18N

        javax.swing.GroupLayout image9Layout = new javax.swing.GroupLayout(image9);
        image9.setLayout(image9Layout);
        image9Layout.setHorizontalGroup(
            image9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon9, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image9Layout.setVerticalGroup(
            image9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon9, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game9Layout = new javax.swing.GroupLayout(game9);
        game9.setLayout(game9Layout);
        game9Layout.setHorizontalGroup(
            game9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game9Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(game9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game9Layout.setVerticalGroup(
            game9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game9Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        game10.setBackground(new java.awt.Color(0, 0, 0));
        game10.setPreferredSize(new java.awt.Dimension(180, 200));
        game10.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                game10MouseMoved(evt);
            }
        });

        button10.setBackground(new java.awt.Color(0, 0, 0));
        button10.setFont(arcadeFont14BOLD);
        button10.setForeground(new java.awt.Color(255, 255, 255));
        button10.setText("Whack A Mole");
        button10.setBorder(null);
        button10.setPreferredSize(new java.awt.Dimension(141, 20));
        button10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button10ActionPerformed(evt);
            }
        });

        image10.setBackground(new java.awt.Color(0, 0, 0));
        image10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        image10.setPreferredSize(new java.awt.Dimension(180, 200));

        gameIcon10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/whackamole-img.png"))); // NOI18N

        javax.swing.GroupLayout image10Layout = new javax.swing.GroupLayout(image10);
        image10.setLayout(image10Layout);
        image10Layout.setHorizontalGroup(
            image10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon10, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        image10Layout.setVerticalGroup(
            image10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gameIcon10, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout game10Layout = new javax.swing.GroupLayout(game10);
        game10.setLayout(game10Layout);
        game10Layout.setHorizontalGroup(
            game10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(game10Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(game10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        game10Layout.setVerticalGroup(
            game10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, game10Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(image10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout gamesPanelLayout = new javax.swing.GroupLayout(gamesPanel);
        gamesPanel.setLayout(gamesPanelLayout);
        gamesPanelLayout.setHorizontalGroup(
            gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamesPanelLayout.createSequentialGroup()
                .addContainerGap(145, Short.MAX_VALUE)
                .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(game1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(game2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(game3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(game4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(game10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(146, Short.MAX_VALUE))
        );
        gamesPanelLayout.setVerticalGroup(
            gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamesPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(game5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game4, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(game1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(game6, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(game7, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(game9, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(game10, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                    .addComponent(game8, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout gamesScreenLayout = new javax.swing.GroupLayout(gamesScreen);
        gamesScreen.setLayout(gamesScreenLayout);
        gamesScreenLayout.setHorizontalGroup(
            gamesScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamesScreenLayout.createSequentialGroup()
                .addGroup(gamesScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamesScreenLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(gamesBackButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 467, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(0, 462, Short.MAX_VALUE))
                    .addGroup(gamesScreenLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(gamesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        gamesScreenLayout.setVerticalGroup(
            gamesScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamesScreenLayout.createSequentialGroup()
                .addGroup(gamesScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamesScreenLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(gamesBackButton))
                    .addGroup(gamesScreenLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)))
                .addGap(28, 28, 28)
                .addComponent(gamesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(328, Short.MAX_VALUE))
        );

        statsScreenMain.setBackground(new java.awt.Color(0, 0, 0));

        statsBackButton.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButton.setBorder(null);
        statsBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonActionPerformed(evt);
            }
        });

        statsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stats-large.png"))); // NOI18N

        statsGamesPanel.setBackground(new java.awt.Color(0, 0, 0));

        statsGame1.setBackground(new java.awt.Color(0, 0, 0));
        statsGame1.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame1MouseMoved(evt);
            }
        });
        statsGame1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statsGame1MouseClicked(evt);
            }
        });

        statsGameButton1.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton1.setFont(arcadeFont14BOLD);
        statsGameButton1.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton1.setText("Space Invaders");
        statsGameButton1.setBorder(null);
        statsGameButton1.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton1ActionPerformed(evt);
            }
        });

        statsGameImage1.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage1.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/space-invaders-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage1Layout = new javax.swing.GroupLayout(statsGameImage1);
        statsGameImage1.setLayout(statsGameImage1Layout);
        statsGameImage1Layout.setHorizontalGroup(
            statsGameImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)
        );
        statsGameImage1Layout.setVerticalGroup(
            statsGameImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame1Layout = new javax.swing.GroupLayout(statsGame1);
        statsGame1.setLayout(statsGame1Layout);
        statsGame1Layout.setHorizontalGroup(
            statsGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(statsGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(statsGameImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGameButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        statsGame1Layout.setVerticalGroup(
            statsGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame1Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame2.setBackground(new java.awt.Color(0, 0, 0));
        statsGame2.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame2MouseMoved(evt);
            }
        });

        statsGameButton2.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton2.setFont(arcadeFont14BOLD);
        statsGameButton2.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton2.setText("Tic Tac Toe");
        statsGameButton2.setBorder(null);
        statsGameButton2.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton2ActionPerformed(evt);
            }
        });

        statsGameImage2.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage2.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tictactoe-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage2Layout = new javax.swing.GroupLayout(statsGameImage2);
        statsGameImage2.setLayout(statsGameImage2Layout);
        statsGameImage2Layout.setHorizontalGroup(
            statsGameImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage2Layout.setVerticalGroup(
            statsGameImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame2Layout = new javax.swing.GroupLayout(statsGame2);
        statsGame2.setLayout(statsGame2Layout);
        statsGame2Layout.setHorizontalGroup(
            statsGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage2, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame2Layout.setVerticalGroup(
            statsGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame2Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame3.setBackground(new java.awt.Color(0, 0, 0));
        statsGame3.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame3MouseMoved(evt);
            }
        });

        statsGameButton3.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton3.setFont(arcadeFont14BOLD);
        statsGameButton3.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton3.setText("Pong");
        statsGameButton3.setBorder(null);
        statsGameButton3.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton3ActionPerformed(evt);
            }
        });

        statsGameImage3.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage3.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pong-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage3Layout = new javax.swing.GroupLayout(statsGameImage3);
        statsGameImage3.setLayout(statsGameImage3Layout);
        statsGameImage3Layout.setHorizontalGroup(
            statsGameImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon3, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );
        statsGameImage3Layout.setVerticalGroup(
            statsGameImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame3Layout = new javax.swing.GroupLayout(statsGame3);
        statsGame3.setLayout(statsGame3Layout);
        statsGame3Layout.setHorizontalGroup(
            statsGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage3, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame3Layout.setVerticalGroup(
            statsGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame3Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame4.setBackground(new java.awt.Color(0, 0, 0));
        statsGame4.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame4MouseMoved(evt);
            }
        });

        statsGameButton4.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton4.setFont(arcadeFont14BOLD);
        statsGameButton4.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton4.setText("Snakes & Ladders");
        statsGameButton4.setBorder(null);
        statsGameButton4.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton4ActionPerformed(evt);
            }
        });

        statsGameImage4.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage4.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/snakesAndLadders-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage4Layout = new javax.swing.GroupLayout(statsGameImage4);
        statsGameImage4.setLayout(statsGameImage4Layout);
        statsGameImage4Layout.setHorizontalGroup(
            statsGameImage4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage4Layout.setVerticalGroup(
            statsGameImage4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame4Layout = new javax.swing.GroupLayout(statsGame4);
        statsGame4.setLayout(statsGame4Layout);
        statsGame4Layout.setHorizontalGroup(
            statsGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage4, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame4Layout.setVerticalGroup(
            statsGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame4Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame5.setBackground(new java.awt.Color(0, 0, 0));
        statsGame5.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame5MouseMoved(evt);
            }
        });

        statsGameButton5.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton5.setFont(arcadeFont14BOLD);
        statsGameButton5.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton5.setText("Tanks A Lot");
        statsGameButton5.setBorder(null);
        statsGameButton5.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton5ActionPerformed(evt);
            }
        });

        statsGameImage5.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage5.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tanks-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage5Layout = new javax.swing.GroupLayout(statsGameImage5);
        statsGameImage5.setLayout(statsGameImage5Layout);
        statsGameImage5Layout.setHorizontalGroup(
            statsGameImage5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage5Layout.setVerticalGroup(
            statsGameImage5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon5, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame5Layout = new javax.swing.GroupLayout(statsGame5);
        statsGame5.setLayout(statsGame5Layout);
        statsGame5Layout.setHorizontalGroup(
            statsGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage5, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame5Layout.setVerticalGroup(
            statsGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame5Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame6.setBackground(new java.awt.Color(0, 0, 0));
        statsGame6.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame6MouseMoved(evt);
            }
        });

        statsGameButton6.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton6.setFont(arcadeFont14BOLD);
        statsGameButton6.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton6.setText("Checkers");
        statsGameButton6.setBorder(null);
        statsGameButton6.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton6ActionPerformed(evt);
            }
        });

        statsGameImage6.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage6.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/checkers-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage6Layout = new javax.swing.GroupLayout(statsGameImage6);
        statsGameImage6.setLayout(statsGameImage6Layout);
        statsGameImage6Layout.setHorizontalGroup(
            statsGameImage6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage6Layout.setVerticalGroup(
            statsGameImage6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon6, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame6Layout = new javax.swing.GroupLayout(statsGame6);
        statsGame6.setLayout(statsGame6Layout);
        statsGame6Layout.setHorizontalGroup(
            statsGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage6, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame6Layout.setVerticalGroup(
            statsGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame6Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame7.setBackground(new java.awt.Color(0, 0, 0));
        statsGame7.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame7.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame7MouseMoved(evt);
            }
        });

        statsGameButton7.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton7.setFont(arcadeFont14BOLD);
        statsGameButton7.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton7.setText("Snake");
        statsGameButton7.setBorder(null);
        statsGameButton7.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton7ActionPerformed(evt);
            }
        });

        statsGameImage7.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage7.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/snake-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage7Layout = new javax.swing.GroupLayout(statsGameImage7);
        statsGameImage7.setLayout(statsGameImage7Layout);
        statsGameImage7Layout.setHorizontalGroup(
            statsGameImage7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage7Layout.setVerticalGroup(
            statsGameImage7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon7, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame7Layout = new javax.swing.GroupLayout(statsGame7);
        statsGame7.setLayout(statsGame7Layout);
        statsGame7Layout.setHorizontalGroup(
            statsGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage7, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame7Layout.setVerticalGroup(
            statsGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame7Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame8.setBackground(new java.awt.Color(0, 0, 0));
        statsGame8.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame8.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame8MouseMoved(evt);
            }
        });

        statsGameButton8.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton8.setFont(arcadeFont14BOLD);
        statsGameButton8.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton8.setText("Tetris");
        statsGameButton8.setBorder(null);
        statsGameButton8.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton8ActionPerformed(evt);
            }
        });

        statsGameImage8.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage8.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tetris-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage8Layout = new javax.swing.GroupLayout(statsGameImage8);
        statsGameImage8.setLayout(statsGameImage8Layout);
        statsGameImage8Layout.setHorizontalGroup(
            statsGameImage8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage8Layout.setVerticalGroup(
            statsGameImage8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon8, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame8Layout = new javax.swing.GroupLayout(statsGame8);
        statsGame8.setLayout(statsGame8Layout);
        statsGame8Layout.setHorizontalGroup(
            statsGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage8, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame8Layout.setVerticalGroup(
            statsGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame8Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame9.setBackground(new java.awt.Color(0, 0, 0));
        statsGame9.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame9.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame9MouseMoved(evt);
            }
        });

        statsGameButton9.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton9.setFont(arcadeFont14BOLD);
        statsGameButton9.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton9.setText("Hangman");
        statsGameButton9.setBorder(null);
        statsGameButton9.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton9ActionPerformed(evt);
            }
        });

        statsGameImage9.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage9.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hangman-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage9Layout = new javax.swing.GroupLayout(statsGameImage9);
        statsGameImage9.setLayout(statsGameImage9Layout);
        statsGameImage9Layout.setHorizontalGroup(
            statsGameImage9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage9Layout.setVerticalGroup(
            statsGameImage9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon9, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame9Layout = new javax.swing.GroupLayout(statsGame9);
        statsGame9.setLayout(statsGame9Layout);
        statsGame9Layout.setHorizontalGroup(
            statsGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage9, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame9Layout.setVerticalGroup(
            statsGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame9Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        statsGame10.setBackground(new java.awt.Color(0, 0, 0));
        statsGame10.setPreferredSize(new java.awt.Dimension(180, 200));
        statsGame10.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                statsGame10MouseMoved(evt);
            }
        });

        statsGameButton10.setBackground(new java.awt.Color(0, 0, 0));
        statsGameButton10.setFont(arcadeFont14BOLD);
        statsGameButton10.setForeground(new java.awt.Color(255, 255, 255));
        statsGameButton10.setText("Whack A Mole");
        statsGameButton10.setBorder(null);
        statsGameButton10.setPreferredSize(new java.awt.Dimension(141, 20));
        statsGameButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsGameButton10ActionPerformed(evt);
            }
        });

        statsGameImage10.setBackground(new java.awt.Color(0, 0, 0));
        statsGameImage10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        statsGameImage10.setPreferredSize(new java.awt.Dimension(180, 200));

        statsGameIcon10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/whackamole-img.png"))); // NOI18N

        javax.swing.GroupLayout statsGameImage10Layout = new javax.swing.GroupLayout(statsGameImage10);
        statsGameImage10.setLayout(statsGameImage10Layout);
        statsGameImage10Layout.setHorizontalGroup(
            statsGameImage10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsGameImage10Layout.setVerticalGroup(
            statsGameImage10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statsGameIcon10, javax.swing.GroupLayout.PREFERRED_SIZE, 192, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout statsGame10Layout = new javax.swing.GroupLayout(statsGame10);
        statsGame10.setLayout(statsGame10Layout);
        statsGame10Layout.setHorizontalGroup(
            statsGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGame10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsGame10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsGameButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(statsGameImage10, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        statsGame10Layout.setVerticalGroup(
            statsGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsGame10Layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(statsGameImage10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsGameButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout statsGamesPanelLayout = new javax.swing.GroupLayout(statsGamesPanel);
        statsGamesPanel.setLayout(statsGamesPanelLayout);
        statsGamesPanelLayout.setHorizontalGroup(
            statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGamesPanelLayout.createSequentialGroup()
                .addContainerGap(149, Short.MAX_VALUE)
                .addGroup(statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsGame1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsGame2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsGame3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsGame4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statsGame10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(150, Short.MAX_VALUE))
        );
        statsGamesPanelLayout.setVerticalGroup(
            statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsGamesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(statsGame5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame4, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(statsGamesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(statsGame6, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame7, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame8, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame9, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statsGame10, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenMainLayout = new javax.swing.GroupLayout(statsScreenMain);
        statsScreenMain.setLayout(statsScreenMainLayout);
        statsScreenMainLayout.setHorizontalGroup(
            statsScreenMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenMainLayout.createSequentialGroup()
                .addGroup(statsScreenMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenMainLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(statsBackButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(statsLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsScreenMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsGamesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        statsScreenMainLayout.setVerticalGroup(
            statsScreenMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenMainLayout.createSequentialGroup()
                .addGroup(statsScreenMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenMainLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButton))
                    .addGroup(statsScreenMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabel)))
                .addGap(18, 18, 18)
                .addComponent(statsGamesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(806, Short.MAX_VALUE))
        );

        statsScreenGame1.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame1.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame1.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame1.setBorder(null);
        statsBackButtonGame1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame1ActionPerformed(evt);
            }
        });

        statsLabelGame1.setFont(arcadeFont54);
        statsLabelGame1.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame1.setText("Space Invaders");

        showFullGame1.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame1.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame1.setText("SHOW FULL LEADERBOARD");
        showFullGame1.setBorder(null);
        showFullGame1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame1ActionPerformed(evt);
            }
        });

        rankPanel1.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne1.setFont(arcadeFont42);
        rankOne1.setForeground(new java.awt.Color(255, 255, 255));
        rankOne1.setText("1.");

        rankTwo1.setFont(arcadeFont42);
        rankTwo1.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo1.setText("2.");

        rankThree1.setFont(arcadeFont42);
        rankThree1.setForeground(new java.awt.Color(255, 255, 255));
        rankThree1.setText("3.");

        rankFour1.setFont(arcadeFont42);
        rankFour1.setForeground(new java.awt.Color(255, 255, 255));
        rankFour1.setText("4.");

        rankFive1.setFont(arcadeFont42);
        rankFive1.setForeground(new java.awt.Color(255, 255, 255));
        rankFive1.setText("5.");

        javax.swing.GroupLayout rankPanel1Layout = new javax.swing.GroupLayout(rankPanel1);
        rankPanel1.setLayout(rankPanel1Layout);
        rankPanel1Layout.setHorizontalGroup(
            rankPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFour1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel1Layout.setVerticalGroup(
            rankPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel1)
                .addGap(43, 43, 43)
                .addComponent(rankOne1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scorePanel1.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne1.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne1.setFont(arcadeFont42);
        scoreOne1.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne1.setText("score 1");

        scoreTwo1.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo1.setFont(arcadeFont42);
        scoreTwo1.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo1.setText("score 2");

        scoreThree1.setFont(arcadeFont42);
        scoreThree1.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree1.setText("score 3");

        scoreFour1.setFont(arcadeFont42);
        scoreFour1.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour1.setText("score 4");

        scoreFive1.setFont(arcadeFont42);
        scoreFive1.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive1.setText("score 5");

        javax.swing.GroupLayout scorePanel1Layout = new javax.swing.GroupLayout(scorePanel1);
        scorePanel1.setLayout(scorePanel1Layout);
        scorePanel1Layout.setHorizontalGroup(
            scorePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel1Layout.setVerticalGroup(
            scorePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel1.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne1.setFont(arcadeFont42);
        userOne1.setForeground(new java.awt.Color(255, 255, 255));
        userOne1.setText("name 1");

        userTwo1.setFont(arcadeFont42);
        userTwo1.setForeground(new java.awt.Color(255, 255, 255));
        userTwo1.setText("name 2");

        userThree1.setFont(arcadeFont42);
        userThree1.setForeground(new java.awt.Color(255, 255, 255));
        userThree1.setText("name 3");

        userFour1.setFont(arcadeFont42);
        userFour1.setForeground(new java.awt.Color(255, 255, 255));
        userFour1.setText("name 4");

        userFive1.setFont(arcadeFont42);
        userFive1.setForeground(new java.awt.Color(255, 255, 255));
        userFive1.setText("name 5");

        javax.swing.GroupLayout namePanel1Layout = new javax.swing.GroupLayout(namePanel1);
        namePanel1.setLayout(namePanel1Layout);
        namePanel1Layout.setHorizontalGroup(
            namePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel1Layout.setVerticalGroup(
            namePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(userOne1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(userTwo1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(userThree1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(userFour1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(userFive1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame1Layout = new javax.swing.GroupLayout(statsScreenGame1);
        statsScreenGame1.setLayout(statsScreenGame1Layout);
        statsScreenGame1Layout.setHorizontalGroup(
            statsScreenGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame1, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame1Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(statsScreenGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame1Layout.createSequentialGroup()
                        .addComponent(rankPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        statsScreenGame1Layout.setVerticalGroup(
            statsScreenGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame1Layout.createSequentialGroup()
                .addGroup(statsScreenGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame1))
                    .addGroup(statsScreenGame1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        statsScreenGame2.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame2.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame2.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame2.setBorder(null);
        statsBackButtonGame2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame2ActionPerformed(evt);
            }
        });

        statsLabelGame2.setFont(arcadeFont54);
        statsLabelGame2.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame2.setText("Tic Tac Toe");

        showFullGame2.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame2.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame2.setText("SHOW FULL LEADERBOARD");
        showFullGame2.setBorder(null);
        showFullGame2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame2ActionPerformed(evt);
            }
        });

        rankPanel2.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne2.setFont(arcadeFont42);
        rankOne2.setForeground(new java.awt.Color(255, 255, 255));
        rankOne2.setText("1.");

        rankTwo2.setFont(arcadeFont42);
        rankTwo2.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo2.setText("2.");

        rankThree2.setFont(arcadeFont42);
        rankThree2.setForeground(new java.awt.Color(255, 255, 255));
        rankThree2.setText("3.");

        rankFour2.setFont(arcadeFont42);
        rankFour2.setForeground(new java.awt.Color(255, 255, 255));
        rankFour2.setText("4.");

        rankFive2.setFont(arcadeFont42);
        rankFive2.setForeground(new java.awt.Color(255, 255, 255));
        rankFive2.setText("5.");

        javax.swing.GroupLayout rankPanel2Layout = new javax.swing.GroupLayout(rankPanel2);
        rankPanel2.setLayout(rankPanel2Layout);
        rankPanel2Layout.setHorizontalGroup(
            rankPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel2Layout.setVerticalGroup(
            rankPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel2)
                .addGap(43, 43, 43)
                .addComponent(rankOne2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel2.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne2.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne2.setFont(arcadeFont42);
        scoreOne2.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne2.setText("score 1");

        scoreTwo2.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo2.setFont(arcadeFont42);
        scoreTwo2.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo2.setText("score 2");

        scoreThree2.setFont(arcadeFont42);
        scoreThree2.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree2.setText("score 3");

        scoreFour2.setFont(arcadeFont42);
        scoreFour2.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour2.setText("score 4");

        scoreFive2.setFont(arcadeFont42);
        scoreFive2.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive2.setText("score 5");

        javax.swing.GroupLayout scorePanel2Layout = new javax.swing.GroupLayout(scorePanel2);
        scorePanel2.setLayout(scorePanel2Layout);
        scorePanel2Layout.setHorizontalGroup(
            scorePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel2Layout.setVerticalGroup(
            scorePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel2.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne2.setFont(arcadeFont42);
        userOne2.setForeground(new java.awt.Color(255, 255, 255));
        userOne2.setText("name 1");

        userTwo2.setFont(arcadeFont42);
        userTwo2.setForeground(new java.awt.Color(255, 255, 255));
        userTwo2.setText("name 2");

        userThree2.setFont(arcadeFont42);
        userThree2.setForeground(new java.awt.Color(255, 255, 255));
        userThree2.setText("name 3");

        userFour2.setFont(arcadeFont42);
        userFour2.setForeground(new java.awt.Color(255, 255, 255));
        userFour2.setText("name 4");

        userFive2.setFont(arcadeFont42);
        userFive2.setForeground(new java.awt.Color(255, 255, 255));
        userFive2.setText("name 5");

        javax.swing.GroupLayout namePanel2Layout = new javax.swing.GroupLayout(namePanel2);
        namePanel2.setLayout(namePanel2Layout);
        namePanel2Layout.setHorizontalGroup(
            namePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel2Layout.setVerticalGroup(
            namePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame2Layout = new javax.swing.GroupLayout(statsScreenGame2);
        statsScreenGame2.setLayout(statsScreenGame2Layout);
        statsScreenGame2Layout.setHorizontalGroup(
            statsScreenGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame2, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame2Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(statsScreenGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame2Layout.createSequentialGroup()
                        .addComponent(rankPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showFullGame2, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        statsScreenGame2Layout.setVerticalGroup(
            statsScreenGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame2Layout.createSequentialGroup()
                .addGroup(statsScreenGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame2))
                    .addGroup(statsScreenGame2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        statsScreenGame3.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame3.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame3.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame3.setBorder(null);
        statsBackButtonGame3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame3ActionPerformed(evt);
            }
        });

        statsLabelGame3.setFont(arcadeFont54);
        statsLabelGame3.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame3.setText("Pong");

        showFullGame3.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame3.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame3.setText("SHOW FULL LEADERBOARD");
        showFullGame3.setBorder(null);
        showFullGame3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame3ActionPerformed(evt);
            }
        });

        rankPanel3.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne3.setFont(arcadeFont42);
        rankOne3.setForeground(new java.awt.Color(255, 255, 255));
        rankOne3.setText("1.");

        rankTwo3.setFont(arcadeFont42);
        rankTwo3.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo3.setText("2.");

        rankThree3.setFont(arcadeFont42);
        rankThree3.setForeground(new java.awt.Color(255, 255, 255));
        rankThree3.setText("3.");

        rankFour3.setFont(arcadeFont42);
        rankFour3.setForeground(new java.awt.Color(255, 255, 255));
        rankFour3.setText("4.");

        rankFive3.setFont(arcadeFont42);
        rankFive3.setForeground(new java.awt.Color(255, 255, 255));
        rankFive3.setText("5.");

        javax.swing.GroupLayout rankPanel3Layout = new javax.swing.GroupLayout(rankPanel3);
        rankPanel3.setLayout(rankPanel3Layout);
        rankPanel3Layout.setHorizontalGroup(
            rankPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel3Layout.setVerticalGroup(
            rankPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel3)
                .addGap(43, 43, 43)
                .addComponent(rankOne3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel3.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne3.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne3.setFont(arcadeFont42);
        scoreOne3.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne3.setText("score 1");

        scoreTwo3.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo3.setFont(arcadeFont42);
        scoreTwo3.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo3.setText("score 2");

        scoreThree3.setFont(arcadeFont42);
        scoreThree3.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree3.setText("score 3");

        scoreFour3.setFont(arcadeFont42);
        scoreFour3.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour3.setText("score 4");

        scoreFive3.setFont(arcadeFont42);
        scoreFive3.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive3.setText("score 5");

        javax.swing.GroupLayout scorePanel3Layout = new javax.swing.GroupLayout(scorePanel3);
        scorePanel3.setLayout(scorePanel3Layout);
        scorePanel3Layout.setHorizontalGroup(
            scorePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel3Layout.setVerticalGroup(
            scorePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel3.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne3.setFont(arcadeFont42);
        userOne3.setForeground(new java.awt.Color(255, 255, 255));
        userOne3.setText("name 1");

        userTwo3.setFont(arcadeFont42);
        userTwo3.setForeground(new java.awt.Color(255, 255, 255));
        userTwo3.setText("name 2");

        userThree3.setFont(arcadeFont42);
        userThree3.setForeground(new java.awt.Color(255, 255, 255));
        userThree3.setText("name 3");

        userFour3.setFont(arcadeFont42);
        userFour3.setForeground(new java.awt.Color(255, 255, 255));
        userFour3.setText("name 4");

        userFive3.setFont(arcadeFont42);
        userFive3.setForeground(new java.awt.Color(255, 255, 255));
        userFive3.setText("name 5");

        javax.swing.GroupLayout namePanel3Layout = new javax.swing.GroupLayout(namePanel3);
        namePanel3.setLayout(namePanel3Layout);
        namePanel3Layout.setHorizontalGroup(
            namePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel3Layout.setVerticalGroup(
            namePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame3Layout = new javax.swing.GroupLayout(statsScreenGame3);
        statsScreenGame3.setLayout(statsScreenGame3Layout);
        statsScreenGame3Layout.setHorizontalGroup(
            statsScreenGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame3, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame3Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(statsScreenGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame3, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame3Layout.createSequentialGroup()
                        .addComponent(rankPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        statsScreenGame3Layout.setVerticalGroup(
            statsScreenGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame3Layout.createSequentialGroup()
                .addGroup(statsScreenGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame3Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame3))
                    .addGroup(statsScreenGame3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        statsScreenGame4.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame4.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame4.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame4.setBorder(null);
        statsBackButtonGame4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame4ActionPerformed(evt);
            }
        });

        statsLabelGame4.setFont(arcadeFont54);
        statsLabelGame4.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame4.setText("Snakes & Ladders");

        showFullGame4.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame4.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame4.setText("SHOW FULL LEADERBOARD");
        showFullGame4.setBorder(null);
        showFullGame4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame4ActionPerformed(evt);
            }
        });

        rankPanel4.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne4.setFont(arcadeFont42);
        rankOne4.setForeground(new java.awt.Color(255, 255, 255));
        rankOne4.setText("1.");

        rankTwo4.setFont(arcadeFont42);
        rankTwo4.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo4.setText("2.");

        rankThree4.setFont(arcadeFont42);
        rankThree4.setForeground(new java.awt.Color(255, 255, 255));
        rankThree4.setText("3.");

        rankFour4.setFont(arcadeFont42);
        rankFour4.setForeground(new java.awt.Color(255, 255, 255));
        rankFour4.setText("4.");

        rankFive4.setFont(arcadeFont42);
        rankFive4.setForeground(new java.awt.Color(255, 255, 255));
        rankFive4.setText("5.");

        javax.swing.GroupLayout rankPanel4Layout = new javax.swing.GroupLayout(rankPanel4);
        rankPanel4.setLayout(rankPanel4Layout);
        rankPanel4Layout.setHorizontalGroup(
            rankPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel4Layout.setVerticalGroup(
            rankPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel4)
                .addGap(43, 43, 43)
                .addComponent(rankOne4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel4.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne4.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne4.setFont(arcadeFont42);
        scoreOne4.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne4.setText("score 1");

        scoreTwo4.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo4.setFont(arcadeFont42);
        scoreTwo4.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo4.setText("score 2");

        scoreThree4.setFont(arcadeFont42);
        scoreThree4.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree4.setText("score 3");

        scoreFour4.setFont(arcadeFont42);
        scoreFour4.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour4.setText("score 4");

        scoreFive4.setFont(arcadeFont42);
        scoreFive4.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive4.setText("score 5");

        javax.swing.GroupLayout scorePanel4Layout = new javax.swing.GroupLayout(scorePanel4);
        scorePanel4.setLayout(scorePanel4Layout);
        scorePanel4Layout.setHorizontalGroup(
            scorePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel4Layout.setVerticalGroup(
            scorePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel4.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne4.setFont(arcadeFont42);
        userOne4.setForeground(new java.awt.Color(255, 255, 255));
        userOne4.setText("name 1");

        userTwo4.setFont(arcadeFont42);
        userTwo4.setForeground(new java.awt.Color(255, 255, 255));
        userTwo4.setText("name 2");

        userThree4.setFont(arcadeFont42);
        userThree4.setForeground(new java.awt.Color(255, 255, 255));
        userThree4.setText("name 3");

        userFour4.setFont(arcadeFont42);
        userFour4.setForeground(new java.awt.Color(255, 255, 255));
        userFour4.setText("name 4");

        userFive4.setFont(arcadeFont42);
        userFive4.setForeground(new java.awt.Color(255, 255, 255));
        userFive4.setText("name 5");

        javax.swing.GroupLayout namePanel4Layout = new javax.swing.GroupLayout(namePanel4);
        namePanel4.setLayout(namePanel4Layout);
        namePanel4Layout.setHorizontalGroup(
            namePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel4Layout.setVerticalGroup(
            namePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame4Layout = new javax.swing.GroupLayout(statsScreenGame4);
        statsScreenGame4.setLayout(statsScreenGame4Layout);
        statsScreenGame4Layout.setHorizontalGroup(
            statsScreenGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame4, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame4Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(statsScreenGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame4, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame4Layout.createSequentialGroup()
                        .addComponent(rankPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        statsScreenGame4Layout.setVerticalGroup(
            statsScreenGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame4Layout.createSequentialGroup()
                .addGroup(statsScreenGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame4))
                    .addGroup(statsScreenGame4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        statsScreenGame5.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame5.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame5.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame5.setBorder(null);
        statsBackButtonGame5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame5ActionPerformed(evt);
            }
        });

        statsLabelGame5.setFont(arcadeFont54);
        statsLabelGame5.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame5.setText("Tanks A Lot");

        showFullGame5.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame5.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame5.setText("SHOW FULL LEADERBOARD");
        showFullGame5.setBorder(null);
        showFullGame5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame5ActionPerformed(evt);
            }
        });

        rankPanel5.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne5.setFont(arcadeFont42);
        rankOne5.setForeground(new java.awt.Color(255, 255, 255));
        rankOne5.setText("1.");

        rankTwo5.setFont(arcadeFont42);
        rankTwo5.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo5.setText("2.");

        rankThree5.setFont(arcadeFont42);
        rankThree5.setForeground(new java.awt.Color(255, 255, 255));
        rankThree5.setText("3.");

        rankFour5.setFont(arcadeFont42);
        rankFour5.setForeground(new java.awt.Color(255, 255, 255));
        rankFour5.setText("4.");

        rankFive5.setFont(arcadeFont42);
        rankFive5.setForeground(new java.awt.Color(255, 255, 255));
        rankFive5.setText("5.");

        javax.swing.GroupLayout rankPanel5Layout = new javax.swing.GroupLayout(rankPanel5);
        rankPanel5.setLayout(rankPanel5Layout);
        rankPanel5Layout.setHorizontalGroup(
            rankPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel5Layout.setVerticalGroup(
            rankPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel5)
                .addGap(43, 43, 43)
                .addComponent(rankOne5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel5.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne5.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne5.setFont(arcadeFont42);
        scoreOne5.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne5.setText("score 1");

        scoreTwo5.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo5.setFont(arcadeFont42);
        scoreTwo5.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo5.setText("score 2");

        scoreThree5.setFont(arcadeFont42);
        scoreThree5.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree5.setText("score 3");

        scoreFour5.setFont(arcadeFont42);
        scoreFour5.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour5.setText("score 4");

        scoreFive5.setFont(arcadeFont42);
        scoreFive5.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive5.setText("score 5");

        javax.swing.GroupLayout scorePanel5Layout = new javax.swing.GroupLayout(scorePanel5);
        scorePanel5.setLayout(scorePanel5Layout);
        scorePanel5Layout.setHorizontalGroup(
            scorePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel5Layout.setVerticalGroup(
            scorePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel5.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne5.setFont(arcadeFont42);
        userOne5.setForeground(new java.awt.Color(255, 255, 255));
        userOne5.setText("name 1");

        userTwo5.setFont(arcadeFont42);
        userTwo5.setForeground(new java.awt.Color(255, 255, 255));
        userTwo5.setText("name 2");

        userThree5.setFont(arcadeFont42);
        userThree5.setForeground(new java.awt.Color(255, 255, 255));
        userThree5.setText("name 3");

        userFour5.setFont(arcadeFont42);
        userFour5.setForeground(new java.awt.Color(255, 255, 255));
        userFour5.setText("name 4");

        userFive5.setFont(arcadeFont42);
        userFive5.setForeground(new java.awt.Color(255, 255, 255));
        userFive5.setText("name 5");

        javax.swing.GroupLayout namePanel5Layout = new javax.swing.GroupLayout(namePanel5);
        namePanel5.setLayout(namePanel5Layout);
        namePanel5Layout.setHorizontalGroup(
            namePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel5Layout.setVerticalGroup(
            namePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame5Layout = new javax.swing.GroupLayout(statsScreenGame5);
        statsScreenGame5.setLayout(statsScreenGame5Layout);
        statsScreenGame5Layout.setHorizontalGroup(
            statsScreenGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame5, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame5Layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addGroup(statsScreenGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame5, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame5Layout.createSequentialGroup()
                        .addComponent(rankPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        statsScreenGame5Layout.setVerticalGroup(
            statsScreenGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame5Layout.createSequentialGroup()
                .addGroup(statsScreenGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame5Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame5))
                    .addGroup(statsScreenGame5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(295, Short.MAX_VALUE))
        );

        statsScreenGame6.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame6.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame6.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame6.setBorder(null);
        statsBackButtonGame6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame6ActionPerformed(evt);
            }
        });

        statsLabelGame6.setFont(arcadeFont54);
        statsLabelGame6.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame6.setText("Checkers");

        showFullGame6.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame6.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame6.setText("SHOW FULL LEADERBOARD");
        showFullGame6.setBorder(null);
        showFullGame6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame6ActionPerformed(evt);
            }
        });

        rankPanel6.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne6.setFont(arcadeFont42);
        rankOne6.setForeground(new java.awt.Color(255, 255, 255));
        rankOne6.setText("1.");

        rankTwo6.setFont(arcadeFont42);
        rankTwo6.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo6.setText("2.");

        rankThree6.setFont(arcadeFont42);
        rankThree6.setForeground(new java.awt.Color(255, 255, 255));
        rankThree6.setText("3.");

        rankFour6.setFont(arcadeFont42);
        rankFour6.setForeground(new java.awt.Color(255, 255, 255));
        rankFour6.setText("4.");

        rankFive6.setFont(arcadeFont42);
        rankFive6.setForeground(new java.awt.Color(255, 255, 255));
        rankFive6.setText("5.");

        javax.swing.GroupLayout rankPanel6Layout = new javax.swing.GroupLayout(rankPanel6);
        rankPanel6.setLayout(rankPanel6Layout);
        rankPanel6Layout.setHorizontalGroup(
            rankPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel6Layout.setVerticalGroup(
            rankPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel6)
                .addGap(43, 43, 43)
                .addComponent(rankOne6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour6, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel6.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne6.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne6.setFont(arcadeFont42);
        scoreOne6.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne6.setText("score 1");

        scoreTwo6.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo6.setFont(arcadeFont42);
        scoreTwo6.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo6.setText("score 2");

        scoreThree6.setFont(arcadeFont42);
        scoreThree6.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree6.setText("score 3");

        scoreFour6.setFont(arcadeFont42);
        scoreFour6.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour6.setText("score 4");

        scoreFive6.setFont(arcadeFont42);
        scoreFive6.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive6.setText("score 5");

        javax.swing.GroupLayout scorePanel6Layout = new javax.swing.GroupLayout(scorePanel6);
        scorePanel6.setLayout(scorePanel6Layout);
        scorePanel6Layout.setHorizontalGroup(
            scorePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel6Layout.setVerticalGroup(
            scorePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel6.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne6.setFont(arcadeFont42);
        userOne6.setForeground(new java.awt.Color(255, 255, 255));
        userOne6.setText("name 1");

        userTwo6.setFont(arcadeFont42);
        userTwo6.setForeground(new java.awt.Color(255, 255, 255));
        userTwo6.setText("name 2");

        userThree6.setFont(arcadeFont42);
        userThree6.setForeground(new java.awt.Color(255, 255, 255));
        userThree6.setText("name 3");

        userFour6.setFont(arcadeFont42);
        userFour6.setForeground(new java.awt.Color(255, 255, 255));
        userFour6.setText("name 4");

        userFive6.setFont(arcadeFont42);
        userFive6.setForeground(new java.awt.Color(255, 255, 255));
        userFive6.setText("name 5");

        javax.swing.GroupLayout namePanel6Layout = new javax.swing.GroupLayout(namePanel6);
        namePanel6.setLayout(namePanel6Layout);
        namePanel6Layout.setHorizontalGroup(
            namePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel6Layout.setVerticalGroup(
            namePanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame6Layout = new javax.swing.GroupLayout(statsScreenGame6);
        statsScreenGame6.setLayout(statsScreenGame6Layout);
        statsScreenGame6Layout.setHorizontalGroup(
            statsScreenGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame6, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame6Layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addGroup(statsScreenGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame6, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame6Layout.createSequentialGroup()
                        .addComponent(rankPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        statsScreenGame6Layout.setVerticalGroup(
            statsScreenGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame6Layout.createSequentialGroup()
                .addGroup(statsScreenGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame6Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame6))
                    .addGroup(statsScreenGame6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(295, Short.MAX_VALUE))
        );

        statsScreenGame7.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame7.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame7.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame7.setBorder(null);
        statsBackButtonGame7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame7ActionPerformed(evt);
            }
        });

        statsLabelGame7.setFont(arcadeFont54);
        statsLabelGame7.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame7.setText("Snake");

        showFullGame7.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame7.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame7.setText("SHOW FULL LEADERBOARD");
        showFullGame7.setBorder(null);
        showFullGame7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame7ActionPerformed(evt);
            }
        });

        rankPanel7.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne7.setFont(arcadeFont42);
        rankOne7.setForeground(new java.awt.Color(255, 255, 255));
        rankOne7.setText("1.");

        rankTwo7.setFont(arcadeFont42);
        rankTwo7.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo7.setText("2.");

        rankThree7.setFont(arcadeFont42);
        rankThree7.setForeground(new java.awt.Color(255, 255, 255));
        rankThree7.setText("3.");

        rankFour7.setFont(arcadeFont42);
        rankFour7.setForeground(new java.awt.Color(255, 255, 255));
        rankFour7.setText("4.");

        rankFive7.setFont(arcadeFont42);
        rankFive7.setForeground(new java.awt.Color(255, 255, 255));
        rankFive7.setText("5.");

        javax.swing.GroupLayout rankPanel7Layout = new javax.swing.GroupLayout(rankPanel7);
        rankPanel7.setLayout(rankPanel7Layout);
        rankPanel7Layout.setHorizontalGroup(
            rankPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel7Layout.setVerticalGroup(
            rankPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel7)
                .addGap(43, 43, 43)
                .addComponent(rankOne7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel7.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne7.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne7.setFont(arcadeFont42);
        scoreOne7.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne7.setText("score 1");

        scoreTwo7.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo7.setFont(arcadeFont42);
        scoreTwo7.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo7.setText("score 2");

        scoreThree7.setFont(arcadeFont42);
        scoreThree7.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree7.setText("score 3");

        scoreFour7.setFont(arcadeFont42);
        scoreFour7.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour7.setText("score 4");

        scoreFive7.setFont(arcadeFont42);
        scoreFive7.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive7.setText("score 5");

        javax.swing.GroupLayout scorePanel7Layout = new javax.swing.GroupLayout(scorePanel7);
        scorePanel7.setLayout(scorePanel7Layout);
        scorePanel7Layout.setHorizontalGroup(
            scorePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel7Layout.setVerticalGroup(
            scorePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel7.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne7.setFont(arcadeFont42);
        userOne7.setForeground(new java.awt.Color(255, 255, 255));
        userOne7.setText("name 1");

        userTwo7.setFont(arcadeFont42);
        userTwo7.setForeground(new java.awt.Color(255, 255, 255));
        userTwo7.setText("name 2");

        userThree7.setFont(arcadeFont42);
        userThree7.setForeground(new java.awt.Color(255, 255, 255));
        userThree7.setText("name 3");

        userFour7.setFont(arcadeFont42);
        userFour7.setForeground(new java.awt.Color(255, 255, 255));
        userFour7.setText("name 4");

        userFive7.setFont(arcadeFont42);
        userFive7.setForeground(new java.awt.Color(255, 255, 255));
        userFive7.setText("name 5");

        javax.swing.GroupLayout namePanel7Layout = new javax.swing.GroupLayout(namePanel7);
        namePanel7.setLayout(namePanel7Layout);
        namePanel7Layout.setHorizontalGroup(
            namePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel7Layout.setVerticalGroup(
            namePanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame7Layout = new javax.swing.GroupLayout(statsScreenGame7);
        statsScreenGame7.setLayout(statsScreenGame7Layout);
        statsScreenGame7Layout.setHorizontalGroup(
            statsScreenGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame7, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame7Layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addGroup(statsScreenGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame7, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame7Layout.createSequentialGroup()
                        .addComponent(rankPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        statsScreenGame7Layout.setVerticalGroup(
            statsScreenGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame7Layout.createSequentialGroup()
                .addGroup(statsScreenGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame7Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame7))
                    .addGroup(statsScreenGame7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(295, Short.MAX_VALUE))
        );

        statsScreenGame8.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame8.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame8.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame8.setBorder(null);
        statsBackButtonGame8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame8ActionPerformed(evt);
            }
        });

        statsLabelGame8.setFont(arcadeFont54);
        statsLabelGame8.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame8.setText("Tetris");

        showFullGame8.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame8.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame8.setText("SHOW FULL LEADERBOARD");
        showFullGame8.setBorder(null);
        showFullGame8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame8ActionPerformed(evt);
            }
        });

        rankPanel8.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne8.setFont(arcadeFont42);
        rankOne8.setForeground(new java.awt.Color(255, 255, 255));
        rankOne8.setText("1.");

        rankTwo8.setFont(arcadeFont42);
        rankTwo8.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo8.setText("2.");

        rankThree8.setFont(arcadeFont42);
        rankThree8.setForeground(new java.awt.Color(255, 255, 255));
        rankThree8.setText("3.");

        rankFour8.setFont(arcadeFont42);
        rankFour8.setForeground(new java.awt.Color(255, 255, 255));
        rankFour8.setText("4.");

        rankFive8.setFont(arcadeFont42);
        rankFive8.setForeground(new java.awt.Color(255, 255, 255));
        rankFive8.setText("5.");

        javax.swing.GroupLayout rankPanel8Layout = new javax.swing.GroupLayout(rankPanel8);
        rankPanel8.setLayout(rankPanel8Layout);
        rankPanel8Layout.setHorizontalGroup(
            rankPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel8Layout.setVerticalGroup(
            rankPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel8)
                .addGap(43, 43, 43)
                .addComponent(rankOne8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree8, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour8, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive8, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel8.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne8.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne8.setFont(arcadeFont42);
        scoreOne8.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne8.setText("score 1");

        scoreTwo8.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo8.setFont(arcadeFont42);
        scoreTwo8.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo8.setText("score 2");

        scoreThree8.setFont(arcadeFont42);
        scoreThree8.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree8.setText("score 3");

        scoreFour8.setFont(arcadeFont42);
        scoreFour8.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour8.setText("score 4");

        scoreFive8.setFont(arcadeFont42);
        scoreFive8.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive8.setText("score 5");

        javax.swing.GroupLayout scorePanel8Layout = new javax.swing.GroupLayout(scorePanel8);
        scorePanel8.setLayout(scorePanel8Layout);
        scorePanel8Layout.setHorizontalGroup(
            scorePanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel8Layout.setVerticalGroup(
            scorePanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel8.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne8.setFont(arcadeFont42);
        userOne8.setForeground(new java.awt.Color(255, 255, 255));
        userOne8.setText("name 1");

        userTwo8.setFont(arcadeFont42);
        userTwo8.setForeground(new java.awt.Color(255, 255, 255));
        userTwo8.setText("name 2");

        userThree8.setFont(arcadeFont42);
        userThree8.setForeground(new java.awt.Color(255, 255, 255));
        userThree8.setText("name 3");

        userFour8.setFont(arcadeFont42);
        userFour8.setForeground(new java.awt.Color(255, 255, 255));
        userFour8.setText("name 4");

        userFive8.setFont(arcadeFont42);
        userFive8.setForeground(new java.awt.Color(255, 255, 255));
        userFive8.setText("name 5");

        javax.swing.GroupLayout namePanel8Layout = new javax.swing.GroupLayout(namePanel8);
        namePanel8.setLayout(namePanel8Layout);
        namePanel8Layout.setHorizontalGroup(
            namePanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel8Layout.setVerticalGroup(
            namePanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame8Layout = new javax.swing.GroupLayout(statsScreenGame8);
        statsScreenGame8.setLayout(statsScreenGame8Layout);
        statsScreenGame8Layout.setHorizontalGroup(
            statsScreenGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame8, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame8Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(statsScreenGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame8, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame8Layout.createSequentialGroup()
                        .addComponent(rankPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        statsScreenGame8Layout.setVerticalGroup(
            statsScreenGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame8Layout.createSequentialGroup()
                .addGroup(statsScreenGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame8Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame8))
                    .addGroup(statsScreenGame8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        statsScreenGame9.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame9.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame9.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame9.setBorder(null);
        statsBackButtonGame9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame9ActionPerformed(evt);
            }
        });

        statsLabelGame9.setFont(arcadeFont54);
        statsLabelGame9.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame9.setText("Hangman");

        showFullGame9.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame9.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame9.setText("SHOW FULL LEADERBOARD");
        showFullGame9.setBorder(null);
        showFullGame9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame9ActionPerformed(evt);
            }
        });

        rankPanel9.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne9.setFont(arcadeFont42);
        rankOne9.setForeground(new java.awt.Color(255, 255, 255));
        rankOne9.setText("1.");

        rankTwo9.setFont(arcadeFont42);
        rankTwo9.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo9.setText("2.");

        rankThree9.setFont(arcadeFont42);
        rankThree9.setForeground(new java.awt.Color(255, 255, 255));
        rankThree9.setText("3.");

        rankFour9.setFont(arcadeFont42);
        rankFour9.setForeground(new java.awt.Color(255, 255, 255));
        rankFour9.setText("4.");

        rankFive9.setFont(arcadeFont42);
        rankFive9.setForeground(new java.awt.Color(255, 255, 255));
        rankFive9.setText("5.");

        javax.swing.GroupLayout rankPanel9Layout = new javax.swing.GroupLayout(rankPanel9);
        rankPanel9.setLayout(rankPanel9Layout);
        rankPanel9Layout.setHorizontalGroup(
            rankPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel9Layout.setVerticalGroup(
            rankPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel9)
                .addGap(43, 43, 43)
                .addComponent(rankOne9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo9, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree9, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour9, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel9.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne9.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne9.setFont(arcadeFont42);
        scoreOne9.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne9.setText("score 1");

        scoreTwo9.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo9.setFont(arcadeFont42);
        scoreTwo9.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo9.setText("score 2");

        scoreThree9.setFont(arcadeFont42);
        scoreThree9.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree9.setText("score 3");

        scoreFour9.setFont(arcadeFont42);
        scoreFour9.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour9.setText("score 4");

        scoreFive9.setFont(arcadeFont42);
        scoreFive9.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive9.setText("score 5");

        javax.swing.GroupLayout scorePanel9Layout = new javax.swing.GroupLayout(scorePanel9);
        scorePanel9.setLayout(scorePanel9Layout);
        scorePanel9Layout.setHorizontalGroup(
            scorePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel9Layout.setVerticalGroup(
            scorePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel9.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne9.setFont(arcadeFont42);
        userOne9.setForeground(new java.awt.Color(255, 255, 255));
        userOne9.setText("name 1");

        userTwo9.setFont(arcadeFont42);
        userTwo9.setForeground(new java.awt.Color(255, 255, 255));
        userTwo9.setText("name 2");

        userThree9.setFont(arcadeFont42);
        userThree9.setForeground(new java.awt.Color(255, 255, 255));
        userThree9.setText("name 3");

        userFour9.setFont(arcadeFont42);
        userFour9.setForeground(new java.awt.Color(255, 255, 255));
        userFour9.setText("name 4");

        userFive9.setFont(arcadeFont42);
        userFive9.setForeground(new java.awt.Color(255, 255, 255));
        userFive9.setText("name 5");

        javax.swing.GroupLayout namePanel9Layout = new javax.swing.GroupLayout(namePanel9);
        namePanel9.setLayout(namePanel9Layout);
        namePanel9Layout.setHorizontalGroup(
            namePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel9Layout.setVerticalGroup(
            namePanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame9Layout = new javax.swing.GroupLayout(statsScreenGame9);
        statsScreenGame9.setLayout(statsScreenGame9Layout);
        statsScreenGame9Layout.setHorizontalGroup(
            statsScreenGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame9, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame9Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(statsScreenGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame9, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame9Layout.createSequentialGroup()
                        .addComponent(rankPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        statsScreenGame9Layout.setVerticalGroup(
            statsScreenGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame9Layout.createSequentialGroup()
                .addGroup(statsScreenGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame9Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame9))
                    .addGroup(statsScreenGame9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame9, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        statsScreenGame10.setBackground(new java.awt.Color(0, 0, 0));
        statsScreenGame10.setPreferredSize(new java.awt.Dimension(1261, 1024));

        statsBackButtonGame10.setBackground(new java.awt.Color(0, 0, 0));
        statsBackButtonGame10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/back.png"))); // NOI18N
        statsBackButtonGame10.setBorder(null);
        statsBackButtonGame10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsBackButtonGame10ActionPerformed(evt);
            }
        });

        statsLabelGame10.setFont(arcadeFont54);
        statsLabelGame10.setForeground(new java.awt.Color(255, 255, 255));
        statsLabelGame10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsLabelGame10.setText("Whack A Mole");

        showFullGame10.setBackground(new java.awt.Color(0, 0, 0));
        showFullGame10.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        showFullGame10.setText("SHOW FULL LEADERBOARD");
        showFullGame10.setBorder(null);
        showFullGame10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFullGame10ActionPerformed(evt);
            }
        });

        rankPanel10.setBackground(new java.awt.Color(0, 0, 0));

        rankLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rank-med.png"))); // NOI18N

        rankOne10.setFont(arcadeFont42);
        rankOne10.setForeground(new java.awt.Color(255, 255, 255));
        rankOne10.setText("1.");

        rankTwo10.setFont(arcadeFont42);
        rankTwo10.setForeground(new java.awt.Color(255, 255, 255));
        rankTwo10.setText("2.");

        rankThree10.setFont(arcadeFont42);
        rankThree10.setForeground(new java.awt.Color(255, 255, 255));
        rankThree10.setText("3.");

        rankFour10.setFont(arcadeFont42);
        rankFour10.setForeground(new java.awt.Color(255, 255, 255));
        rankFour10.setText("4.");

        rankFive10.setFont(arcadeFont42);
        rankFive10.setForeground(new java.awt.Color(255, 255, 255));
        rankFive10.setText("5.");

        javax.swing.GroupLayout rankPanel10Layout = new javax.swing.GroupLayout(rankPanel10);
        rankPanel10.setLayout(rankPanel10Layout);
        rankPanel10Layout.setHorizontalGroup(
            rankPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rankPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(rankFive10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankFour10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankThree10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankTwo10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rankOne10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        rankPanel10Layout.setVerticalGroup(
            rankPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rankLabel10)
                .addGap(43, 43, 43)
                .addComponent(rankOne10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(rankTwo10, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(rankThree10, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(rankFour10, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(rankFive10, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        scorePanel10.setBackground(new java.awt.Color(0, 0, 0));

        scoreLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/score-med.png"))); // NOI18N

        scoreOne10.setBackground(new java.awt.Color(0, 0, 0));
        scoreOne10.setFont(arcadeFont42);
        scoreOne10.setForeground(new java.awt.Color(255, 255, 255));
        scoreOne10.setText("score 1");

        scoreTwo10.setBackground(new java.awt.Color(0, 0, 0));
        scoreTwo10.setFont(arcadeFont42);
        scoreTwo10.setForeground(new java.awt.Color(255, 255, 255));
        scoreTwo10.setText("score 2");

        scoreThree10.setFont(arcadeFont42);
        scoreThree10.setForeground(new java.awt.Color(255, 255, 255));
        scoreThree10.setText("score 3");

        scoreFour10.setFont(arcadeFont42);
        scoreFour10.setForeground(new java.awt.Color(255, 255, 255));
        scoreFour10.setText("score 4");

        scoreFive10.setFont(arcadeFont42);
        scoreFive10.setForeground(new java.awt.Color(255, 255, 255));
        scoreFive10.setText("score 5");

        javax.swing.GroupLayout scorePanel10Layout = new javax.swing.GroupLayout(scorePanel10);
        scorePanel10.setLayout(scorePanel10Layout);
        scorePanel10Layout.setHorizontalGroup(
            scorePanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scorePanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scorePanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreTwo10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFive10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreOne10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreThree10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreFour10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scoreLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        scorePanel10Layout.setVerticalGroup(
            scorePanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scorePanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scoreLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreOne10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreTwo10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreThree10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFour10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scoreFive10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namePanel10.setBackground(new java.awt.Color(0, 0, 0));

        nameLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/name-med.png"))); // NOI18N

        userOne10.setFont(arcadeFont42);
        userOne10.setForeground(new java.awt.Color(255, 255, 255));
        userOne10.setText("name 1");

        userTwo10.setFont(arcadeFont42);
        userTwo10.setForeground(new java.awt.Color(255, 255, 255));
        userTwo10.setText("name 2");

        userThree10.setFont(arcadeFont42);
        userThree10.setForeground(new java.awt.Color(255, 255, 255));
        userThree10.setText("name 3");

        userFour10.setFont(arcadeFont42);
        userFour10.setForeground(new java.awt.Color(255, 255, 255));
        userFour10.setText("name 4");

        userFive10.setFont(arcadeFont42);
        userFive10.setForeground(new java.awt.Color(255, 255, 255));
        userFive10.setText("name 5");

        javax.swing.GroupLayout namePanel10Layout = new javax.swing.GroupLayout(namePanel10);
        namePanel10.setLayout(namePanel10Layout);
        namePanel10Layout.setHorizontalGroup(
            namePanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(namePanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userOne10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userTwo10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userThree10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFour10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userFive10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );
        namePanel10Layout.setVerticalGroup(
            namePanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(namePanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userOne10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userTwo10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userThree10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFour10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userFive10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statsScreenGame10Layout = new javax.swing.GroupLayout(statsScreenGame10);
        statsScreenGame10.setLayout(statsScreenGame10Layout);
        statsScreenGame10Layout.setHorizontalGroup(
            statsScreenGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame10Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(statsBackButtonGame10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statsLabelGame10, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenGame10Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(statsScreenGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showFullGame10, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(statsScreenGame10Layout.createSequentialGroup()
                        .addComponent(rankPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(scorePanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(namePanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        statsScreenGame10Layout.setVerticalGroup(
            statsScreenGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsScreenGame10Layout.createSequentialGroup()
                .addGroup(statsScreenGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsScreenGame10Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(statsBackButtonGame10))
                    .addGroup(statsScreenGame10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statsLabelGame10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showFullGame10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsScreenGame10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rankPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scorePanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(namePanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        statsScreen.setLayer(statsScreenMain, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        statsScreen.setLayer(statsScreenGame10, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout statsScreenLayout = new javax.swing.GroupLayout(statsScreen);
        statsScreen.setLayout(statsScreenLayout);
        statsScreenLayout.setHorizontalGroup(
            statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1449, Short.MAX_VALUE)
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame1, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame2, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame3, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame4, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame5, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame6, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame9, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1467, Short.MAX_VALUE))
        );
        statsScreenLayout.setVerticalGroup(
            statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1556, Short.MAX_VALUE)
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame1, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame2, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame3, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame4, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame5, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame6, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame9, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
            .addGroup(statsScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreenGame10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1568, Short.MAX_VALUE))
        );

        layeredPane.setLayer(homeScreen, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredPane.setLayer(settingsScreen, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredPane.setLayer(gamesScreen, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredPane.setLayer(statsScreen, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneLayout = new javax.swing.GroupLayout(layeredPane);
        layeredPane.setLayout(layeredPaneLayout);
        layeredPaneLayout.setHorizontalGroup(
            layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(homeScreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(settingsScreen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(gamesScreen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreen))
        );
        layeredPaneLayout.setVerticalGroup(
            layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(homeScreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(settingsScreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(gamesScreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statsScreen, javax.swing.GroupLayout.Alignment.TRAILING))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //method that runs when the user presses the play button on the main screen
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        //make the games screen visible and the previous menu invisible
        gamesScreen.setVisible(true);
        homeScreen.setVisible(false);
    }//GEN-LAST:event_playButtonActionPerformed

    //method that runs when the user presses the messaging button on the main screen
    private void messagingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messagingButtonActionPerformed
        try {
            //create a new instance of the globalChatGUI object in the globalChatGUI.java file and make it visible, this essentially opens a new (kind of separate) program that will allow messaging to happen
            new globalChatGUI().setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_messagingButtonActionPerformed

    //method that runs when the user presses the backbutton button on the settings screen
    private void settingsBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsBackButtonActionPerformed
        //make settings screen invisible and the homescreen visible again
        settingsScreen.setVisible(false);
        homeScreen.setVisible(true);
    }//GEN-LAST:event_settingsBackButtonActionPerformed

    //method that runs when the user presses the settings button on the main menu
    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        //make home screen invisible and settings screen visible
        homeScreen.setVisible(false);
        settingsScreen.setVisible(true);
    }//GEN-LAST:event_settingsButtonActionPerformed

    //runs when the user presses the change username button in the settings screen then run the changeUserNameGUI program, this opens a new window where the user can change their name
    private void changeUserNameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeUserNameButtonActionPerformed
        new changeUserNameGUI().setVisible(true);
    }//GEN-LAST:event_changeUserNameButtonActionPerformed

    //method that runs when the user presses the backbutton button on the games screen
    private void gamesBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gamesBackButtonActionPerformed
        //make games screen invisible and the homescreen visible again
        gamesScreen.setVisible(false);
        homeScreen.setVisible(true);
    }//GEN-LAST:event_gamesBackButtonActionPerformed

    //runs when the user presses the play button on the first game in the games screen
    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        //set the currentgame global variable equal to "Game 1"
        currentGame = "Game 1";

        if (invadersGameMode.equals("spider")) {
            //run the gameframe program from spider invaders and make it visible, this runs the game
            new spider.invaders.GameFrame().setVisible(true);
        } else {
            //run the gameframe program from space invaders and make it visible, this runs the game
            new spaceIntruders.Game.GameFrame().setVisible(true);
        }

    }//GEN-LAST:event_button1ActionPerformed

    //when the user mouses over anywhere on the gamesscreen then make sure all the game icons and play buttons are returned back to their default images and texts
    private void gamesScreenMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gamesScreenMouseMoved
        button1.setText("Space Invaders");
        button2.setText("Tic Tac Toe");
        button3.setText("Pong");
        button4.setText("Snakes & Ladders");
        button5.setText("Tanks A Lot");
        button6.setText("Checkers");
        button7.setText("Snake");
        button8.setText("Tetris");
        button9.setText("Hangman");
        button10.setText("Whack A Mole");

        gameIcon1.setIcon(imgSpaceInvaders);
        //gameIcon2.setIcon();
        gameIcon3.setIcon(imgPong);
        //gameIcon4.setIcon();
        //gameIcon5.setIcon();
        //gameIcon6.setIcon();
        //gameIcon7.setIcon();
        //gameIcon8.setIcon();
        //gameIcon9.setIcon();
        //gameIcon10.setIcon();
    }//GEN-LAST:event_gamesScreenMouseMoved

    //runs when the user presses the play button on the second game in the games screen
    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        //set the currentgame global variable equal to "Game 2"
        currentGame = "Game 2";

        //run the tictacGUI program from my tic tac toe program and make it visible, this runs the game
        new TicTacGUI().setVisible(true);
    }//GEN-LAST:event_button2ActionPerformed

    //enters the stats screen by pressing the stats button
    private void statsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsButtonActionPerformed
        homeScreen.setVisible(false);
        statsScreen.setVisible(true);
    }//GEN-LAST:event_statsButtonActionPerformed

    //runs when the user presses the music button on the main home screen
    private void musicButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_musicButtonActionPerformed
        
        //run the spotimah jar, this runs hugh's spotify-llike music program. Unfortunately the file he sent me was both sent late, and not working. I am not responsible for the program not working
        try{
            Process spotimah = Runtime.getRuntime().exec("java -jar SpotiMah.jar");
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_musicButtonActionPerformed

    //runs when the suer presses teh exit button on the home screen
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        //exits the entire program
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    //runs when the user presses the back button on the stats screen
    private void statsBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonActionPerformed
        //make home screen visible and stats screen invisible
        homeScreen.setVisible(true);
        statsScreen.setVisible(false);
    }//GEN-LAST:event_statsBackButtonActionPerformed

    //runs when the user presses the back button on the statsgame 1 screen
    private void statsBackButtonGame1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame1ActionPerformed
        //make statshome screen visible and statsgame1 screen invisible
        statsScreenMain.setVisible(true);
        statsScreenGame1.setVisible(false);
    }//GEN-LAST:event_statsBackButtonGame1ActionPerformed

    //runs when the user presses the play button on the third game in the games screen
    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        //set the currentgame global variable equal to "Game 2"
        currentGame = "Game 3";

        //run the pongstart() method in the pong.java file, this starts the pong game/program
        Pong.pongStart();
    }//GEN-LAST:event_button3ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame1ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 1")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame1ActionPerformed


    private void statsBackButtonGame2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame2ActionPerformed
        //goes back to the stats main menu
        statsScreenGame2.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame2ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame2ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 2")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame2ActionPerformed

    private void statsBackButtonGame3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame3ActionPerformed
        //goes back to the stats main menu
        statsScreenGame3.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame3ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame3ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 3")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame3ActionPerformed

    private void statsBackButtonGame4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame4ActionPerformed
        //goes back to the stats main menu
        statsScreenGame4.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame4ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame4ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 4")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame4ActionPerformed

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game1MouseMoved
        button1.setText("> PLAY <");
        gameIcon1.setIcon(gifSpaceInvaders);
    }//GEN-LAST:event_game1MouseMoved

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game2MouseMoved
        button2.setText("> PLAY <");
        //gameIcon2.setIcon(ticTacToeGif);
    }//GEN-LAST:event_game2MouseMoved

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game3MouseMoved
        button3.setText("> PLAY <");
        gameIcon3.setIcon(gifPong);
    }//GEN-LAST:event_game3MouseMoved

    //runs when the user presses the play button on snakes and ladders
    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        //set the currentgame global variable equal to "Game 4"
        currentGame = "Game 4";

        //run the gameframe program from space invaders and make it visible, this runs the game
        new gameMenu().setVisible(true);
    }//GEN-LAST:event_button4ActionPerformed

    private void game4MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game4MouseMoved
        button4.setText("> PLAY <");
        //gameIcon4.setIcon(gifPong);
    }//GEN-LAST:event_game4MouseMoved

    private void statsGame2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame2MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame2MouseMoved

    //when the user presses the game 1 button on the stats screen, this will open the stats screen for game 1, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton1ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame1.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 1");

            scoreOne1.setText(leaderboard.get(0).score + "");
            scoreTwo1.setText(leaderboard.get(1).score + "");
            scoreThree1.setText(leaderboard.get(2).score + "");
            scoreFour1.setText(leaderboard.get(3).score + "");
            scoreFive1.setText(leaderboard.get(4).score + "");

            userOne1.setText(leaderboard.get(0).name);
            userTwo1.setText(leaderboard.get(1).name);
            userThree1.setText(leaderboard.get(2).name);
            userFour1.setText(leaderboard.get(3).name);
            userFive1.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton1ActionPerformed

    private void statsGame1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame1MouseMoved

    private void statsGame1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame1MouseClicked

    //when the user presses the game 2 button on the stats screen, this will open the stats screen for game 2, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton2ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame2.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 2");

            scoreOne2.setText(leaderboard.get(0).score + "");
            scoreTwo2.setText(leaderboard.get(1).score + "");
            scoreThree2.setText(leaderboard.get(2).score + "");
            scoreFour2.setText(leaderboard.get(3).score + "");
            scoreFive2.setText(leaderboard.get(4).score + "");

            userOne2.setText(leaderboard.get(0).name);
            userTwo2.setText(leaderboard.get(1).name);
            userThree2.setText(leaderboard.get(2).name);
            userFour2.setText(leaderboard.get(3).name);
            userFive2.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton2ActionPerformed

    //when the user presses the game 3 button on the stats screen, this will open the stats screen for game 3, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton3ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame3.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 3");

            scoreOne3.setText(leaderboard.get(0).score + "");
            scoreTwo3.setText(leaderboard.get(1).score + "");
            scoreThree3.setText(leaderboard.get(2).score + "");
            scoreFour3.setText(leaderboard.get(3).score + "");
            scoreFive3.setText(leaderboard.get(4).score + "");

            userOne3.setText(leaderboard.get(0).name);
            userTwo3.setText(leaderboard.get(1).name);
            userThree3.setText(leaderboard.get(2).name);
            userFour3.setText(leaderboard.get(3).name);
            userFive3.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton3ActionPerformed

    private void statsGame3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame3MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame3MouseMoved

    //when the user presses the game 4 button on the stats screen, this will open the stats screen for game 4, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton4ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame4.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 4");

            scoreOne4.setText(leaderboard.get(0).score + "");
            scoreTwo4.setText(leaderboard.get(1).score + "");
            scoreThree4.setText(leaderboard.get(2).score + "");
            scoreFour4.setText(leaderboard.get(3).score + "");
            scoreFive4.setText(leaderboard.get(4).score + "");

            userOne4.setText(leaderboard.get(0).name);
            userTwo4.setText(leaderboard.get(1).name);
            userThree4.setText(leaderboard.get(2).name);
            userFour4.setText(leaderboard.get(3).name);
            userFive4.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton4ActionPerformed

    private void statsGame4MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame4MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame4MouseMoved

    //runs when the user presses the play button on the fifth game
    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        //set the currentgame global variable equal to "Game 5"
        currentGame = "Game 5";

        
        /**
         * this launches Kieran and Graeme's game externally. Their game simply wouldnt start when it was
         * inside the arcade. since their game is outside of this project, it cannot call any arcadeGUI methods
         * to set score or get score. If i had received the game earlier it could have been implemented directly into the arcade
         * 
         * ALSO, the game had sound that worked in netbeans, but upon building it into a jar the sound would not play. This is not something
         * i wanted to sit and fix so i just left it as is
         */
        try{
            Process tanks = Runtime.getRuntime().exec("java -jar TankGame.jar");
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_button5ActionPerformed

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game5MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game5MouseMoved
        button5.setText("> PLAY <");
        //gameIcon5.setIcon(gifPong);
    }//GEN-LAST:event_game5MouseMoved

    //runs when the user presses the play button on the sixth game
    private void button6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button6ActionPerformed
        //set the currentgame global variable equal to "Game 6"
        currentGame = "Game 6";

        //opens the checkers game
        Checkers.startGame();
    }//GEN-LAST:event_button6ActionPerformed

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game6MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game6MouseMoved
        button6.setText("> PLAY <");
        //gameIcon6.setIcon(gifPong);
    }//GEN-LAST:event_game6MouseMoved

    //runs when the user presses the play button on the seventh game
    private void button7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button7ActionPerformed
        //set the currentgame global variable equal to "Game 7"
        currentGame = "Game 7";
        
        
        //creates a new instance of the snake class game
        new Snake();
    }//GEN-LAST:event_button7ActionPerformed

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game7MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game7MouseMoved
        button7.setText("> PLAY <");
        //gameIcon7.setIcon(gifPong);
    }//GEN-LAST:event_game7MouseMoved

    //runs when the user presses the play button on the eighth game
    private void button8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button8ActionPerformed
        //set the currentgame global variable equal to "Game 8"
        currentGame = "Game 8";
        
        
        //creates a new instance of the tetris game
        TetrisISU.startGame();
    }//GEN-LAST:event_button8ActionPerformed

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game8MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game8MouseMoved
        button8.setText("> PLAY <");
        //gameIcon8.setIcon(gifPong);
    }//GEN-LAST:event_game8MouseMoved

    //runs when the user presses the play button on the ninth game
    private void button9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9ActionPerformed
        //set the currentgame global variable equal to "Game 9"
        currentGame = "Game 9";
        
        
        /**
         * this launches Shahob & Ariadne's game externally. Their game was very complicated and would have
         * taken ages to implement into the arcade itself, since their game is outside of this project, it cannot call and arcadeGUI methods
         * to set score or get score. If i had received the game earlier it could have been implemented directly into the arcade
         */
        try{
            Process hangman = Runtime.getRuntime().exec("java -jar HangmanCars.jar");
        }catch(Exception e){
            
        }
    }//GEN-LAST:event_button9ActionPerformed

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game9MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game9MouseMoved
        button9.setText("> PLAY <");
        //gameIcon9.setIcon(gifPong);
    }//GEN-LAST:event_game9MouseMoved

    //runs when the user presses the play button on the tenth game
    private void button10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button10ActionPerformed
        //set the currentgame global variable equal to "Game 10"
        currentGame = "Game 10";
        
        
        //creates a new instance of the whackamole game
        WhackAMole.startGame();
    }//GEN-LAST:event_button10ActionPerformed

    //when the player hovers over the game's panel, then play a preview gif and set the text to say play
    private void game10MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_game10MouseMoved
        button10.setText("> PLAY <");
        //gameIcon10.setIcon(gifPong);
    }//GEN-LAST:event_game10MouseMoved

    //when the user presses the game 5 button on the stats screen, this will open the stats screen for game 5, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton5ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame5.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 5");

            scoreOne5.setText(leaderboard.get(0).score + "");
            scoreTwo5.setText(leaderboard.get(1).score + "");
            scoreThree5.setText(leaderboard.get(2).score + "");
            scoreFour5.setText(leaderboard.get(3).score + "");
            scoreFive5.setText(leaderboard.get(4).score + "");

            userOne5.setText(leaderboard.get(0).name);
            userTwo5.setText(leaderboard.get(1).name);
            userThree5.setText(leaderboard.get(2).name);
            userFour5.setText(leaderboard.get(3).name);
            userFive5.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton5ActionPerformed

    private void statsGame5MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame5MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame5MouseMoved

    //when the user presses the game 6 button on the stats screen, this will open the stats screen for game 6, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton6ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame6.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 6");

            scoreOne6.setText(leaderboard.get(0).score + "");
            scoreTwo6.setText(leaderboard.get(1).score + "");
            scoreThree6.setText(leaderboard.get(2).score + "");
            scoreFour6.setText(leaderboard.get(3).score + "");
            scoreFive6.setText(leaderboard.get(4).score + "");

            userOne6.setText(leaderboard.get(0).name);
            userTwo6.setText(leaderboard.get(1).name);
            userThree6.setText(leaderboard.get(2).name);
            userFour6.setText(leaderboard.get(3).name);
            userFive6.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton6ActionPerformed

    private void statsGame6MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame6MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame6MouseMoved

    //when the user presses the game 7 button on the stats screen, this will open the stats screen for game 7, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton7ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame7.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 7");

            scoreOne7.setText(leaderboard.get(0).score + "");
            scoreTwo7.setText(leaderboard.get(1).score + "");
            scoreThree7.setText(leaderboard.get(2).score + "");
            scoreFour7.setText(leaderboard.get(3).score + "");
            scoreFive7.setText(leaderboard.get(4).score + "");

            userOne7.setText(leaderboard.get(0).name);
            userTwo7.setText(leaderboard.get(1).name);
            userThree7.setText(leaderboard.get(2).name);
            userFour7.setText(leaderboard.get(3).name);
            userFive7.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton7ActionPerformed

    private void statsGame7MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame7MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame7MouseMoved

    //when the user presses the game 8 button on the stats screen, this will open the stats screen for game 8, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton8ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame8.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 8");

            scoreOne8.setText(leaderboard.get(0).score + "");
            scoreTwo8.setText(leaderboard.get(1).score + "");
            scoreThree8.setText(leaderboard.get(2).score + "");
            scoreFour8.setText(leaderboard.get(3).score + "");
            scoreFive8.setText(leaderboard.get(4).score + "");

            userOne8.setText(leaderboard.get(0).name);
            userTwo8.setText(leaderboard.get(1).name);
            userThree8.setText(leaderboard.get(2).name);
            userFour8.setText(leaderboard.get(3).name);
            userFive8.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton8ActionPerformed

    private void statsGame8MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame8MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame8MouseMoved

    //when the user presses the game 9 button on the stats screen, this will open the stats screen for game 9, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton9ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame9.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 9");

            scoreOne9.setText(leaderboard.get(0).score + "");
            scoreTwo9.setText(leaderboard.get(1).score + "");
            scoreThree9.setText(leaderboard.get(2).score + "");
            scoreFour9.setText(leaderboard.get(3).score + "");
            scoreFive9.setText(leaderboard.get(4).score + "");

            userOne9.setText(leaderboard.get(0).name);
            userTwo9.setText(leaderboard.get(1).name);
            userThree9.setText(leaderboard.get(2).name);
            userFour9.setText(leaderboard.get(3).name);
            userFive9.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton9ActionPerformed

    private void statsGame9MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame9MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame9MouseMoved

    //when the user presses the game 10 button on the stats screen, this will open the stats screen for game 10, getting the leaderboard and setting the text to the corresponding placements on the leaderboard
    private void statsGameButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsGameButton10ActionPerformed
        statsScreenMain.setVisible(false);
        statsScreenGame10.setVisible(true);
        try {
            ArrayList<user> leaderboard = getLeaderboard("Game 10");

            scoreOne10.setText(leaderboard.get(0).score + "");
            scoreTwo10.setText(leaderboard.get(1).score + "");
            scoreThree10.setText(leaderboard.get(2).score + "");
            scoreFour10.setText(leaderboard.get(3).score + "");
            scoreFive10.setText(leaderboard.get(4).score + "");

            userOne10.setText(leaderboard.get(0).name);
            userTwo10.setText(leaderboard.get(1).name);
            userThree10.setText(leaderboard.get(2).name);
            userFour10.setText(leaderboard.get(3).name);
            userFive10.setText(leaderboard.get(4).name);

        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statsGameButton10ActionPerformed

    private void statsGame10MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statsGame10MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_statsGame10MouseMoved

    private void statsBackButtonGame5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame5ActionPerformed
        //goes back to the stats main menu
        statsScreenGame5.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame5ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame5ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 5")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame5ActionPerformed

    private void statsBackButtonGame6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame6ActionPerformed
        //goes back to the stats main menu
        statsScreenGame6.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame6ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame6ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 6")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame6ActionPerformed

    private void statsBackButtonGame7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame7ActionPerformed
        //goes back to the stats main menu
        statsScreenGame7.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame7ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame7ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 7")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame7ActionPerformed

    private void statsBackButtonGame8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame8ActionPerformed
        //goes back to the stats main menu
        statsScreenGame8.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame8ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame8ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 8")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame8ActionPerformed

    private void statsBackButtonGame9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame9ActionPerformed
        //goes back to the stats main menu
        statsScreenGame9.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame9ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame9ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 9")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame9ActionPerformed

    private void statsBackButtonGame10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsBackButtonGame10ActionPerformed
        //goes back to the stats main menu
        statsScreenGame10.setVisible(false);
        statsScreenMain.setVisible(true);
    }//GEN-LAST:event_statsBackButtonGame10ActionPerformed

    //this method shows the full leaderboard for the following game by opening a new window showing a list of all the users and their scores in descending order
    private void showFullGame10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFullGame10ActionPerformed
        try {
            new leaderboardGUI(getLeaderboard("Game 10")).setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(arcadeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_showFullGame10ActionPerformed

    //runs when the user presses the change gamemode button button on the settings screen
    private void invadersGameModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invadersGameModeButtonActionPerformed
        if (invadersGameMode.equals("spider")) {
            invadersGameMode = "space";
            invadersGameModeLabel.setText("space invaders [1P]");
        } else {
            invadersGameMode = "spider";
            invadersGameModeLabel.setText("spider invaders [2P]");
        }
    }//GEN-LAST:event_invadersGameModeButtonActionPerformed

    //class user, this class is used by the leaderboard methods to sort and organize users based on their score, this is to make working scoreboards
    class user {

        String name;
        int score;

        //creates a new object that contains a string and an integer called name and score 
        user(String _name, int _score) {
            name = _name;
            score = _score;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(arcadeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(arcadeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(arcadeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(arcadeGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                new arcadeGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button1;
    private javax.swing.JButton button10;
    private javax.swing.JButton button2;
    private javax.swing.JButton button3;
    private javax.swing.JButton button4;
    private javax.swing.JButton button5;
    private javax.swing.JButton button6;
    private javax.swing.JButton button7;
    private javax.swing.JButton button8;
    private javax.swing.JButton button9;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton changeUserNameButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JPanel game1;
    private javax.swing.JPanel game10;
    private javax.swing.JPanel game2;
    private javax.swing.JPanel game3;
    private javax.swing.JPanel game4;
    private javax.swing.JPanel game5;
    private javax.swing.JPanel game6;
    private javax.swing.JPanel game7;
    private javax.swing.JPanel game8;
    private javax.swing.JPanel game9;
    private javax.swing.JLabel gameIcon1;
    private javax.swing.JLabel gameIcon10;
    private javax.swing.JLabel gameIcon2;
    private javax.swing.JLabel gameIcon3;
    private javax.swing.JLabel gameIcon4;
    private javax.swing.JLabel gameIcon5;
    private javax.swing.JLabel gameIcon6;
    private javax.swing.JLabel gameIcon7;
    private javax.swing.JLabel gameIcon8;
    private javax.swing.JLabel gameIcon9;
    private javax.swing.JButton gamesBackButton;
    private javax.swing.JPanel gamesPanel;
    private javax.swing.JPanel gamesScreen;
    private javax.swing.JPanel holderPanel;
    private javax.swing.JPanel homeScreen;
    private javax.swing.JPanel image1;
    private javax.swing.JPanel image10;
    private javax.swing.JPanel image2;
    private javax.swing.JPanel image3;
    private javax.swing.JPanel image4;
    private javax.swing.JPanel image5;
    private javax.swing.JPanel image6;
    private javax.swing.JPanel image7;
    private javax.swing.JPanel image8;
    private javax.swing.JPanel image9;
    private javax.swing.JButton invadersGameModeButton;
    private javax.swing.JLabel invadersGameModeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLayeredPane layeredPane;
    private javax.swing.JButton messagingButton;
    private javax.swing.JButton musicButton;
    private javax.swing.JLabel nameLabel1;
    private javax.swing.JLabel nameLabel10;
    private javax.swing.JLabel nameLabel2;
    private javax.swing.JLabel nameLabel3;
    private javax.swing.JLabel nameLabel4;
    private javax.swing.JLabel nameLabel5;
    private javax.swing.JLabel nameLabel6;
    private javax.swing.JLabel nameLabel7;
    private javax.swing.JLabel nameLabel8;
    private javax.swing.JLabel nameLabel9;
    private javax.swing.JPanel namePanel1;
    private javax.swing.JPanel namePanel10;
    private javax.swing.JPanel namePanel2;
    private javax.swing.JPanel namePanel3;
    private javax.swing.JPanel namePanel4;
    private javax.swing.JPanel namePanel5;
    private javax.swing.JPanel namePanel6;
    private javax.swing.JPanel namePanel7;
    private javax.swing.JPanel namePanel8;
    private javax.swing.JPanel namePanel9;
    private javax.swing.JLabel onlineStatusLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel rankFive1;
    private javax.swing.JLabel rankFive10;
    private javax.swing.JLabel rankFive2;
    private javax.swing.JLabel rankFive3;
    private javax.swing.JLabel rankFive4;
    private javax.swing.JLabel rankFive5;
    private javax.swing.JLabel rankFive6;
    private javax.swing.JLabel rankFive7;
    private javax.swing.JLabel rankFive8;
    private javax.swing.JLabel rankFive9;
    private javax.swing.JLabel rankFour1;
    private javax.swing.JLabel rankFour10;
    private javax.swing.JLabel rankFour2;
    private javax.swing.JLabel rankFour3;
    private javax.swing.JLabel rankFour4;
    private javax.swing.JLabel rankFour5;
    private javax.swing.JLabel rankFour6;
    private javax.swing.JLabel rankFour7;
    private javax.swing.JLabel rankFour8;
    private javax.swing.JLabel rankFour9;
    private javax.swing.JLabel rankLabel1;
    private javax.swing.JLabel rankLabel10;
    private javax.swing.JLabel rankLabel2;
    private javax.swing.JLabel rankLabel3;
    private javax.swing.JLabel rankLabel4;
    private javax.swing.JLabel rankLabel5;
    private javax.swing.JLabel rankLabel6;
    private javax.swing.JLabel rankLabel7;
    private javax.swing.JLabel rankLabel8;
    private javax.swing.JLabel rankLabel9;
    private javax.swing.JLabel rankOne1;
    private javax.swing.JLabel rankOne10;
    private javax.swing.JLabel rankOne2;
    private javax.swing.JLabel rankOne3;
    private javax.swing.JLabel rankOne4;
    private javax.swing.JLabel rankOne5;
    private javax.swing.JLabel rankOne6;
    private javax.swing.JLabel rankOne7;
    private javax.swing.JLabel rankOne8;
    private javax.swing.JLabel rankOne9;
    private javax.swing.JPanel rankPanel1;
    private javax.swing.JPanel rankPanel10;
    private javax.swing.JPanel rankPanel2;
    private javax.swing.JPanel rankPanel3;
    private javax.swing.JPanel rankPanel4;
    private javax.swing.JPanel rankPanel5;
    private javax.swing.JPanel rankPanel6;
    private javax.swing.JPanel rankPanel7;
    private javax.swing.JPanel rankPanel8;
    private javax.swing.JPanel rankPanel9;
    private javax.swing.JLabel rankThree1;
    private javax.swing.JLabel rankThree10;
    private javax.swing.JLabel rankThree2;
    private javax.swing.JLabel rankThree3;
    private javax.swing.JLabel rankThree4;
    private javax.swing.JLabel rankThree5;
    private javax.swing.JLabel rankThree6;
    private javax.swing.JLabel rankThree7;
    private javax.swing.JLabel rankThree8;
    private javax.swing.JLabel rankThree9;
    private javax.swing.JLabel rankTwo1;
    private javax.swing.JLabel rankTwo10;
    private javax.swing.JLabel rankTwo2;
    private javax.swing.JLabel rankTwo3;
    private javax.swing.JLabel rankTwo4;
    private javax.swing.JLabel rankTwo5;
    private javax.swing.JLabel rankTwo6;
    private javax.swing.JLabel rankTwo7;
    private javax.swing.JLabel rankTwo8;
    private javax.swing.JLabel rankTwo9;
    private javax.swing.JLabel scoreFive1;
    private javax.swing.JLabel scoreFive10;
    private javax.swing.JLabel scoreFive2;
    private javax.swing.JLabel scoreFive3;
    private javax.swing.JLabel scoreFive4;
    private javax.swing.JLabel scoreFive5;
    private javax.swing.JLabel scoreFive6;
    private javax.swing.JLabel scoreFive7;
    private javax.swing.JLabel scoreFive8;
    private javax.swing.JLabel scoreFive9;
    private javax.swing.JLabel scoreFour1;
    private javax.swing.JLabel scoreFour10;
    private javax.swing.JLabel scoreFour2;
    private javax.swing.JLabel scoreFour3;
    private javax.swing.JLabel scoreFour4;
    private javax.swing.JLabel scoreFour5;
    private javax.swing.JLabel scoreFour6;
    private javax.swing.JLabel scoreFour7;
    private javax.swing.JLabel scoreFour8;
    private javax.swing.JLabel scoreFour9;
    private javax.swing.JLabel scoreLabel1;
    private javax.swing.JLabel scoreLabel10;
    private javax.swing.JLabel scoreLabel2;
    private javax.swing.JLabel scoreLabel3;
    private javax.swing.JLabel scoreLabel4;
    private javax.swing.JLabel scoreLabel5;
    private javax.swing.JLabel scoreLabel6;
    private javax.swing.JLabel scoreLabel7;
    private javax.swing.JLabel scoreLabel8;
    private javax.swing.JLabel scoreLabel9;
    private javax.swing.JLabel scoreOne1;
    private javax.swing.JLabel scoreOne10;
    private javax.swing.JLabel scoreOne2;
    private javax.swing.JLabel scoreOne3;
    private javax.swing.JLabel scoreOne4;
    private javax.swing.JLabel scoreOne5;
    private javax.swing.JLabel scoreOne6;
    private javax.swing.JLabel scoreOne7;
    private javax.swing.JLabel scoreOne8;
    private javax.swing.JLabel scoreOne9;
    private javax.swing.JPanel scorePanel1;
    private javax.swing.JPanel scorePanel10;
    private javax.swing.JPanel scorePanel2;
    private javax.swing.JPanel scorePanel3;
    private javax.swing.JPanel scorePanel4;
    private javax.swing.JPanel scorePanel5;
    private javax.swing.JPanel scorePanel6;
    private javax.swing.JPanel scorePanel7;
    private javax.swing.JPanel scorePanel8;
    private javax.swing.JPanel scorePanel9;
    private javax.swing.JLabel scoreThree1;
    private javax.swing.JLabel scoreThree10;
    private javax.swing.JLabel scoreThree2;
    private javax.swing.JLabel scoreThree3;
    private javax.swing.JLabel scoreThree4;
    private javax.swing.JLabel scoreThree5;
    private javax.swing.JLabel scoreThree6;
    private javax.swing.JLabel scoreThree7;
    private javax.swing.JLabel scoreThree8;
    private javax.swing.JLabel scoreThree9;
    private javax.swing.JLabel scoreTwo1;
    private javax.swing.JLabel scoreTwo10;
    private javax.swing.JLabel scoreTwo2;
    private javax.swing.JLabel scoreTwo3;
    private javax.swing.JLabel scoreTwo4;
    private javax.swing.JLabel scoreTwo5;
    private javax.swing.JLabel scoreTwo6;
    private javax.swing.JLabel scoreTwo7;
    private javax.swing.JLabel scoreTwo8;
    private javax.swing.JLabel scoreTwo9;
    private javax.swing.JPanel settingExitHolder;
    private javax.swing.JButton settingsBackButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JPanel settingsScreen;
    private javax.swing.JButton showFullGame1;
    private javax.swing.JButton showFullGame10;
    private javax.swing.JButton showFullGame2;
    private javax.swing.JButton showFullGame3;
    private javax.swing.JButton showFullGame4;
    private javax.swing.JButton showFullGame5;
    private javax.swing.JButton showFullGame6;
    private javax.swing.JButton showFullGame7;
    private javax.swing.JButton showFullGame8;
    private javax.swing.JButton showFullGame9;
    private javax.swing.JButton statsBackButton;
    private javax.swing.JButton statsBackButtonGame1;
    private javax.swing.JButton statsBackButtonGame10;
    private javax.swing.JButton statsBackButtonGame2;
    private javax.swing.JButton statsBackButtonGame3;
    private javax.swing.JButton statsBackButtonGame4;
    private javax.swing.JButton statsBackButtonGame5;
    private javax.swing.JButton statsBackButtonGame6;
    private javax.swing.JButton statsBackButtonGame7;
    private javax.swing.JButton statsBackButtonGame8;
    private javax.swing.JButton statsBackButtonGame9;
    private javax.swing.JButton statsButton;
    private javax.swing.JPanel statsGame1;
    private javax.swing.JPanel statsGame10;
    private javax.swing.JPanel statsGame2;
    private javax.swing.JPanel statsGame3;
    private javax.swing.JPanel statsGame4;
    private javax.swing.JPanel statsGame5;
    private javax.swing.JPanel statsGame6;
    private javax.swing.JPanel statsGame7;
    private javax.swing.JPanel statsGame8;
    private javax.swing.JPanel statsGame9;
    private javax.swing.JButton statsGameButton1;
    private javax.swing.JButton statsGameButton10;
    private javax.swing.JButton statsGameButton2;
    private javax.swing.JButton statsGameButton3;
    private javax.swing.JButton statsGameButton4;
    private javax.swing.JButton statsGameButton5;
    private javax.swing.JButton statsGameButton6;
    private javax.swing.JButton statsGameButton7;
    private javax.swing.JButton statsGameButton8;
    private javax.swing.JButton statsGameButton9;
    private javax.swing.JLabel statsGameIcon1;
    private javax.swing.JLabel statsGameIcon10;
    private javax.swing.JLabel statsGameIcon2;
    private javax.swing.JLabel statsGameIcon3;
    private javax.swing.JLabel statsGameIcon4;
    private javax.swing.JLabel statsGameIcon5;
    private javax.swing.JLabel statsGameIcon6;
    private javax.swing.JLabel statsGameIcon7;
    private javax.swing.JLabel statsGameIcon8;
    private javax.swing.JLabel statsGameIcon9;
    private javax.swing.JPanel statsGameImage1;
    private javax.swing.JPanel statsGameImage10;
    private javax.swing.JPanel statsGameImage2;
    private javax.swing.JPanel statsGameImage3;
    private javax.swing.JPanel statsGameImage4;
    private javax.swing.JPanel statsGameImage5;
    private javax.swing.JPanel statsGameImage6;
    private javax.swing.JPanel statsGameImage7;
    private javax.swing.JPanel statsGameImage8;
    private javax.swing.JPanel statsGameImage9;
    private javax.swing.JPanel statsGamesPanel;
    private javax.swing.JLabel statsLabel;
    private javax.swing.JLabel statsLabelGame1;
    private javax.swing.JLabel statsLabelGame10;
    private javax.swing.JLabel statsLabelGame2;
    private javax.swing.JLabel statsLabelGame3;
    private javax.swing.JLabel statsLabelGame4;
    private javax.swing.JLabel statsLabelGame5;
    private javax.swing.JLabel statsLabelGame6;
    private javax.swing.JLabel statsLabelGame7;
    private javax.swing.JLabel statsLabelGame8;
    private javax.swing.JLabel statsLabelGame9;
    private javax.swing.JLayeredPane statsScreen;
    private javax.swing.JPanel statsScreenGame1;
    private javax.swing.JPanel statsScreenGame10;
    private javax.swing.JPanel statsScreenGame2;
    private javax.swing.JPanel statsScreenGame3;
    private javax.swing.JPanel statsScreenGame4;
    private javax.swing.JPanel statsScreenGame5;
    private javax.swing.JPanel statsScreenGame6;
    private javax.swing.JPanel statsScreenGame7;
    private javax.swing.JPanel statsScreenGame8;
    private javax.swing.JPanel statsScreenGame9;
    private javax.swing.JPanel statsScreenMain;
    private javax.swing.JLabel userFive1;
    private javax.swing.JLabel userFive10;
    private javax.swing.JLabel userFive2;
    private javax.swing.JLabel userFive3;
    private javax.swing.JLabel userFive4;
    private javax.swing.JLabel userFive5;
    private javax.swing.JLabel userFive6;
    private javax.swing.JLabel userFive7;
    private javax.swing.JLabel userFive8;
    private javax.swing.JLabel userFive9;
    private javax.swing.JLabel userFour1;
    private javax.swing.JLabel userFour10;
    private javax.swing.JLabel userFour2;
    private javax.swing.JLabel userFour3;
    private javax.swing.JLabel userFour4;
    private javax.swing.JLabel userFour5;
    private javax.swing.JLabel userFour6;
    private javax.swing.JLabel userFour7;
    private javax.swing.JLabel userFour8;
    private javax.swing.JLabel userFour9;
    private javax.swing.JLabel userOne1;
    private javax.swing.JLabel userOne10;
    private javax.swing.JLabel userOne2;
    private javax.swing.JLabel userOne3;
    private javax.swing.JLabel userOne4;
    private javax.swing.JLabel userOne5;
    private javax.swing.JLabel userOne6;
    private javax.swing.JLabel userOne7;
    private javax.swing.JLabel userOne8;
    private javax.swing.JLabel userOne9;
    private javax.swing.JLabel userThree1;
    private javax.swing.JLabel userThree10;
    private javax.swing.JLabel userThree2;
    private javax.swing.JLabel userThree3;
    private javax.swing.JLabel userThree4;
    private javax.swing.JLabel userThree5;
    private javax.swing.JLabel userThree6;
    private javax.swing.JLabel userThree7;
    private javax.swing.JLabel userThree8;
    private javax.swing.JLabel userThree9;
    private javax.swing.JLabel userTwo1;
    private javax.swing.JLabel userTwo10;
    private javax.swing.JLabel userTwo2;
    private javax.swing.JLabel userTwo3;
    private javax.swing.JLabel userTwo4;
    private javax.swing.JLabel userTwo5;
    private javax.swing.JLabel userTwo6;
    private javax.swing.JLabel userTwo7;
    private javax.swing.JLabel userTwo8;
    private javax.swing.JLabel userTwo9;
    // End of variables declaration//GEN-END:variables
}
