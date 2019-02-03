/*
 * this program makes a nice list of every user in the spreadsheet and organizes them in descending order
 */
package mainMenu;

import java.util.ArrayList;
import mainMenu.arcadeGUI.user;

/**
 *
 * @author Parker Rowe
 */
public class leaderboardGUI extends javax.swing.JFrame {

    /**
     * Creates new form leaderboardGUI
     */
    public leaderboardGUI(ArrayList<user> sentArray) {
        initComponents();
        //sets global variable leaderboard equal to the sent array of users 
        leaderboard = sentArray;
        
        //run the set text method
        setText();
    }

    //creates a new arralist of users called leaderboard
    static ArrayList<user> leaderboard = new ArrayList();
    
    //set text method is send an arraylist of users
    public void setText(){
        //create a temp string that will be added to and sent to the textarea, starting with the headers for RANK SCOrE and NAME
        String temp=String.format("%-10s%-10s%-10s\n","RANK","SCORE","NAME");
        
        //go through the leaderboard array and add onto the temp string the rank, score and name with minimum 10 spaces and moved to the leftside using string formatting
        for(int i = 0; i < leaderboard.size(); i++){
            temp += String.format("%-10d%-10d%-10s\n", i+1, leaderboard.get(i).score, leaderboard.get(i).name);
        }
        
        //set the text area to the temp string
        textArea.setText(temp);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Leaderboard");

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        textArea.setEditable(false);
        textArea.setBackground(new java.awt.Color(0, 0, 0));
        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        textArea.setForeground(new java.awt.Color(255, 255, 255));
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(leaderboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(leaderboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(leaderboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(leaderboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new leaderboardGUI(leaderboard).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
