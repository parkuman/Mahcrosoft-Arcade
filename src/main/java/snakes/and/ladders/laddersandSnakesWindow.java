package snakes.and.ladders;

import java.awt.Component;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import mainMenu.arcadeGUI;

/**
 *
 * @author Saaransh Sharma
 */
public class laddersandSnakesWindow extends javax.swing.JFrame {

    //Importing the images for the dice
    ImageIcon a = new ImageIcon(getClass().getResource("/snakesAndLadders/Die1.png"));
    ImageIcon b = new ImageIcon(getClass().getResource("/snakesAndLadders/Die2.png"));
    ImageIcon c = new ImageIcon(getClass().getResource("/snakesAndLadders/Die3.png"));
    ImageIcon d = new ImageIcon(getClass().getResource("/snakesAndLadders/Die4.png"));
    ImageIcon e = new ImageIcon(getClass().getResource("/snakesAndLadders/Die5.png"));
    ImageIcon f = new ImageIcon(getClass().getResource("/snakesAndLadders/Die6.png"));

    int score = 25; //Keeps track of player's main score
    int compscore = 25; //Computer's score
    int highscore = 0; //Player's high score
    int playerwins = 0; //Tracks number of games won by player
    int compwins = 0; //Tracks number of games won by computer
    /*playerChoice switches between player 1 and computer, if false, if false, 
    it's player 1's turn, if true, it's the computer's turn      
     */
    boolean playerChoice = false;

    /**
     * Creates new form snakesWindow
     */
    public laddersandSnakesWindow() {
        initComponents();

    }

    //Method which handles drawing the dice, displays what player rolled.
    public void dice() {
        Random random = new Random();
        int dice = random.nextInt(7 - 1) + 1;

        /*Switch statement that handles the dice variable. First checks the number
        rolled, then checks to see if it's player 1's or the computer's turn. It then
        sends the value to the respective player method, diceHandler or diceHandler2        
         */
        switch (dice) {
            case 1:

                if (playerChoice == false) {
                    diceImage.setIcon(a);
                    diceHandler(1);
                } else if (playerChoice == true) {
                    diceHandler2(1);
                }
                break;
            case 2:

                if (playerChoice == false) {
                    diceImage.setIcon(b);
                    diceHandler(2);
                } else if (playerChoice == true) {
                    diceHandler2(2);
                }
                break;
            case 3:

                if (playerChoice == false) {
                    diceImage.setIcon(c);
                    diceHandler(3);
                } else if (playerChoice == true) {
                    diceHandler2(3);
                }
                break;
            case 4:

                if (playerChoice == false) {
                    diceImage.setIcon(d);
                    diceHandler(4);
                } else if (playerChoice == true) {
                    diceHandler2(4);
                }
                break;
            case 5:

                if (playerChoice == false) {
                    diceImage.setIcon(e);
                    diceHandler(5);
                } else if (playerChoice == true) {
                    diceHandler2(5);
                }
                break;
            case 6:

                if (playerChoice == false) {
                    diceImage.setIcon(f);
                    diceHandler(6);
                } else if (playerChoice == true) {
                    diceHandler2(6);
                }
                break;
            default:
                break;

        }
    }

    /*Winner method, receives the String value of whoever won from either diceHandler method
    and creates a popup message congratulating them 
     */
    public void winner(String playerName) {
        Component frame = null;

        JOptionPane.showMessageDialog(frame, playerName + " wins!");
        
        //sets the current amount of wins to increment your previous score by 1 if the winner is not the computer
        if (!playerName.equals("The computer")) {
            try {
                arcadeGUI.setScore(arcadeGUI.getScore() + 1);
            } catch (IOException ex) {
                Logger.getLogger(snakesWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*diceHandler method for player 1. Receives the dice value that the player 
    rolled from the dice method.
     */
    public void diceHandler(int diceroll) {
        //Value rolled from dice is subtracted from the score variable
        score -= diceroll;

        /*If statement structure which handles both the snake and ladder components.
        If the score is at a position of a snake, the score is reduced. If the score is at 
        a position of a ladder, it is increased
         */
        if (score == 24) {
            score -= 8;
        }
        if (score == 18) {
            score -= 6;
        }
        if (score == 20) {
            score -= 9;
        }
        if (score == 17 || score == 13) {
            score += 6;
        }
        if (score == 3) {
            score += 11;
        }

        /*Winning move for player 1, records highscore, reenables restart button and 
        sends String value to the winner method. Dice button is disabled to prevent further 
        input. Also adds to recorded number of wins and prints to the text field*/
        if (score <= 1) {
            score = 1;
            blue.setLocation(310, 590);
            diceButton.setEnabled(false);
            restartButton.setEnabled(true);
            playerwins++;
            playerWinsText.setText(Integer.toString(playerwins));
            winner("Player 1");
        } /*If statement structure which handles the positoning of player 1's piece,
        (blue). If the score matches the statement, it is set to the pre-determined
        coordinate in the JFrame. It then swtiches over to the computer and loops
        back to the dice method        
         */ else if (score > 1 && score <= 5) {
            blue.setLocation(310 + (130 * score), 590);
            playerChoice = true;
            dice();
        } else if (score == 6) {
            blue.setLocation(930, 450);
            playerChoice = true;
            dice();
        } else if (score == 7) {
            blue.setLocation(800, 450);
            playerChoice = true;
            dice();
        } else if (score == 8) {
            blue.setLocation(630, 450);
            playerChoice = true;
            dice();
        } else if (score == 9) {
            blue.setLocation(470, 450);
            playerChoice = true;
            dice();
        } else if (score == 10) {
            blue.setLocation(310, 450);
            playerChoice = true;
            dice();
        } else if (score == 11) {
            blue.setLocation(310, 300);
            playerChoice = true;
            dice();
        } else if (score == 12) {
            blue.setLocation(470, 300);
            playerChoice = true;
            dice();
        } else if (score == 13) {
            blue.setLocation(630, 300);
            playerChoice = true;
            dice();
        } else if (score == 14) {
            blue.setLocation(800, 300);
            playerChoice = true;
            dice();
        } else if (score == 15) {
            blue.setLocation(930, 300);
            playerChoice = true;
            dice();
        } else if (score == 16) {
            blue.setLocation(930, 160);
            playerChoice = true;
            dice();
        } else if (score == 17) {
            blue.setLocation(800, 160);
            playerChoice = true;
            dice();
        } else if (score == 18) {
            blue.setLocation(630, 160);
            playerChoice = true;
            dice();
        } else if (score == 19) {
            blue.setLocation(470, 160);
            playerChoice = true;
            dice();
        } else if (score == 20) {
            blue.setLocation(310, 160);
            playerChoice = true;
            dice();
        } else if (score == 21) {
            blue.setLocation(310, 20);
            playerChoice = true;
            dice();
        } else if (score == 22) {
            blue.setLocation(470, 20);
            playerChoice = true;
            dice();
        } else if (score == 23) {
            blue.setLocation(630, 20);
            playerChoice = true;
            dice();
        } else if (score == 24) {
            blue.setLocation(800, 20);
            playerChoice = true;
            dice();
        } else if (score >= 25) {
            blue.setLocation(930, 20);
            playerChoice = true;
            dice();
        }
        //Sets player 1's score to the text field
        playerScoreText.setText(Integer.toString(score));

    }

    //diceHandler2 method for computer. Receives the dice value rolled from the dice method.
    public void diceHandler2(int diceroll) {
        //Value rolled from dice is subtracted from the score variable
        compscore -= diceroll;

        /*If statement structure which handles both the snake and ladder components.
        If the score is at a position of a snake, the score is reduced. If the score is at 
        a position of a ladder, it is increased
         */
        if (compscore == 24) {
            compscore -= 8;
        }
        if (compscore == 18) {
            compscore -= 6;
        }
        if (compscore == 20) {
            compscore -= 9;
        }
        if (compscore == 17 || compscore == 13) {
            compscore += 6;
        }
        if (compscore == 3) {
            compscore += 11;
        }
        /*Winning move for computer, re-enables restart button and sends String 
        value to the winner method. Dice button is disabled to prevent further 
        input. Also adds to recorded number of wins and prints to the text field*/
        if (compscore <= 1) {
            compscore = 1;
            red.setLocation(310, 590);
            diceButton.setEnabled(false);
            restartButton.setEnabled(true);
            compwins++;
            compWinsText.setText(Integer.toString(compwins));
            winner("The computer");
        } /*If statement structure which handles the positoning of computer's piece,
        (red). If the score matches the statement, it is set to the pre-determined
        coordinate in the JFrame. It then swtiches over to the computer and loops
        back to the dice method        
         */ else if (compscore <= 5) {
            red.setLocation(310 + (130 * compscore), 590);
            playerChoice = false;
        } else if (compscore == 6) {
            red.setLocation(930, 450);
            playerChoice = false;
        } else if (compscore == 7) {
            red.setLocation(800, 450);
            playerChoice = false;
        } else if (compscore == 8) {
            red.setLocation(630, 450);
            playerChoice = false;
        } else if (compscore == 9) {
            red.setLocation(470, 450);
            playerChoice = false;
        } else if (compscore == 10) {
            red.setLocation(310, 450);
            playerChoice = false;
        } else if (compscore == 11) {
            red.setLocation(310, 300);
            playerChoice = false;
        } else if (compscore == 12) {
            red.setLocation(470, 300);
            playerChoice = false;
        } else if (compscore == 13) {
            red.setLocation(630, 300);
            playerChoice = false;
        } else if (compscore == 14) {
            red.setLocation(800, 300);
            playerChoice = false;
        } else if (compscore == 15) {
            red.setLocation(930, 300);
            playerChoice = false;
        } else if (compscore == 16) {
            red.setLocation(930, 160);
            playerChoice = false;
        } else if (compscore == 17) {
            red.setLocation(800, 160);
            playerChoice = false;
        } else if (compscore == 18) {
            red.setLocation(630, 160);
            playerChoice = false;
        } else if (compscore == 19) {
            red.setLocation(470, 160);
            playerChoice = false;
        } else if (compscore == 20) {
            red.setLocation(310, 160);
            playerChoice = false;
        } else if (compscore == 21) {
            red.setLocation(310, 20);
            playerChoice = false;
        } else if (compscore == 22) {
            red.setLocation(470, 20);
            playerChoice = false;
        } else if (compscore == 23) {
            red.setLocation(630, 20);
            playerChoice = false;
        } else if (compscore == 24) {
            red.setLocation(800, 20);
            playerChoice = false;
        } else if (compscore >= 25) {
            red.setLocation(930, 20);
            playerChoice = false;
        }
        //Sets computer's score to the text field
        compScore.setText(Integer.toString(compscore));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        borderPanel = new javax.swing.JPanel();
        exitButton = new javax.swing.JButton();
        diceButton = new javax.swing.JButton();
        red = new javax.swing.JLabel();
        blue = new javax.swing.JLabel();
        gameboard = new javax.swing.JLabel();
        diceImage = new javax.swing.JLabel();
        diceLabel = new javax.swing.JLabel();
        playerLabel = new javax.swing.JLabel();
        playerScoreText = new javax.swing.JTextField();
        compLabel = new javax.swing.JLabel();
        compScore = new javax.swing.JTextField();
        restartButton = new javax.swing.JButton();
        playerwinLabel = new javax.swing.JLabel();
        playerWinsText = new javax.swing.JTextField();
        compWinsText = new javax.swing.JTextField();
        compWinsLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ladders and Snakes");

        background.setBackground(new java.awt.Color(153, 153, 153));
        background.setForeground(new java.awt.Color(102, 102, 102));
        background.setMinimumSize(new java.awt.Dimension(1280, 1024));
        background.setPreferredSize(new java.awt.Dimension(1280, 1024));
        background.setLayout(null);

        borderPanel.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout borderPanelLayout = new javax.swing.GroupLayout(borderPanel);
        borderPanel.setLayout(borderPanelLayout);
        borderPanelLayout.setHorizontalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        borderPanelLayout.setVerticalGroup(
            borderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        background.add(borderPanel);
        borderPanel.setBounds(280, 0, 30, 1036);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        background.add(exitButton);
        exitButton.setBounds(110, 660, 75, 32);

        diceButton.setText("Roll Dice");
        diceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diceButtonActionPerformed(evt);
            }
        });
        background.add(diceButton);
        diceButton.setBounds(100, 530, 100, 32);

        red.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Red playing piece.png"))); // NOI18N
        background.add(red);
        red.setBounds(930, 10, 130, 130);

        blue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Blue playing piece.png"))); // NOI18N
        blue.setText("jLabel1");
        background.add(blue);
        blue.setBounds(920, 20, 130, 100);

        gameboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameboardremastered.png"))); // NOI18N
        gameboard.setText("jLabel2");
        background.add(gameboard);
        gameboard.setBounds(310, 10, 750, 700);
        background.add(diceImage);
        diceImage.setBounds(100, 380, 100, 100);

        diceLabel.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        diceLabel.setText("You rolled a:");
        background.add(diceLabel);
        diceLabel.setBounds(100, 310, 120, 40);

        playerLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        playerLabel.setText("Your Score:");
        background.add(playerLabel);
        playerLabel.setBounds(1090, 110, 80, 22);

        playerScoreText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerScoreTextActionPerformed(evt);
            }
        });
        background.add(playerScoreText);
        playerScoreText.setBounds(1190, 100, 71, 24);
        playerScoreText.setEditable(false);

        compLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        compLabel.setText("Computer Score:");
        background.add(compLabel);
        compLabel.setBounds(1080, 200, 100, 22);

        compScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compScoreActionPerformed(evt);
            }
        });
        background.add(compScore);
        compScore.setBounds(1190, 190, 71, 24);
        compScore.setEditable(false);

        restartButton.setText("Restart");
        restartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButtonActionPerformed(evt);
            }
        });
        background.add(restartButton);
        restartButton.setBounds(100, 590, 100, 32);

        playerwinLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        playerwinLabel.setText("Player Wins:");
        background.add(playerwinLabel);
        playerwinLabel.setBounds(50, 90, 80, 22);
        background.add(playerWinsText);
        playerWinsText.setBounds(170, 90, 71, 24);
        playerWinsText.setEditable(false);
        background.add(compWinsText);
        compWinsText.setBounds(170, 150, 70, 24);
        compWinsText.setEditable(false);

        compWinsLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        compWinsLabel.setText("Computer Wins:");
        background.add(compWinsLabel);
        compWinsLabel.setBounds(40, 150, 110, 22);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1286, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1042, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void diceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diceButtonActionPerformed
        //Restart button is deactivated so the player must play through round
        restartButton.setEnabled(false);
        //Transfers to dice method
        dice();

    }//GEN-LAST:event_diceButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        dispose(); //Exits game
    }//GEN-LAST:event_exitButtonActionPerformed

    private void playerScoreTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playerScoreTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_playerScoreTextActionPerformed

    private void compScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compScoreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_compScoreActionPerformed

    private void restartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButtonActionPerformed
        score = 25;
        compscore = 25;
        diceButton.setEnabled(true);
        playerChoice = false;
        blue.setLocation(930, 20);
        red.setLocation(930, 20);
        playerScoreText.setText("25");
        compScore.setText("25");
    }//GEN-LAST:event_restartButtonActionPerformed

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
            java.util.logging.Logger.getLogger(laddersandSnakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(laddersandSnakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(laddersandSnakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(laddersandSnakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new laddersandSnakesWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JLabel blue;
    private javax.swing.JPanel borderPanel;
    private javax.swing.JLabel compLabel;
    private javax.swing.JTextField compScore;
    private javax.swing.JLabel compWinsLabel;
    private javax.swing.JTextField compWinsText;
    private javax.swing.JButton diceButton;
    private javax.swing.JLabel diceImage;
    private javax.swing.JLabel diceLabel;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel gameboard;
    private javax.swing.JLabel playerLabel;
    private javax.swing.JTextField playerScoreText;
    private javax.swing.JTextField playerWinsText;
    private javax.swing.JLabel playerwinLabel;
    private javax.swing.JLabel red;
    private javax.swing.JButton restartButton;
    // End of variables declaration//GEN-END:variables
}
