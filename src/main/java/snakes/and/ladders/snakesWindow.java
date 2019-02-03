package snakes.and.ladders;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import mainMenu.arcadeGUI;

/**
 *
 * @author Saaransh Sharma & Hugh Mainwaring
 * ICS4U1
 */
public class snakesWindow extends javax.swing.JFrame {
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /** CUSTOM SOUND PLAYING CLASS, this was imported by Parker after the game was handed in, due to
     *  the way java needs to gather resources when it is packaged into an executable Parker has imported this into
     *  the game in order for the sound to work
     */
    public static class SoundEffect {
        private Clip clip;

        // Constructor to construct each element of the enum with its own sound file.
        SoundEffect(String soundFileName) {
            try {
                // Use URL (instead of File) to read from disk and JAR.
                URL url = this.getClass().getResource(soundFileName);
                // Set up an audio input stream piped from the sound file.
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                // Get a clip resource.
                clip = AudioSystem.getClip();
                // Open audio clip and load samples from the audio input stream.
                clip.open(audioInputStream);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            }
        }

        // Play or Re-play the sound effect from the beginning, by rewinding.
        public void play() {

            if (clip.isRunning()) {
                clip.stop();   // Stop the player if it is still running
            }
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();     // Start playing
        }
    }
    
    //create new instance of sound effect objects with the corresponding sound files
    public static SoundEffect dieSound = new SoundEffect("/snakesAndLadders/die.wav");
    public static SoundEffect winSound = new SoundEffect("/snakesAndLadders/win.wav");
    public static SoundEffect loseSound = new SoundEffect("/snakesAndLadders/lose.wav");
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //start of non-parker code
    
    //Importing the images for the dice
    ImageIcon a = new ImageIcon(getClass().getResource("/snakesAndLadders/Die1.png"));
    ImageIcon b = new ImageIcon(getClass().getResource("/snakesAndLadders/Die2.png"));
    ImageIcon c = new ImageIcon(getClass().getResource("/snakesAndLadders/Die3.png"));
    ImageIcon d = new ImageIcon(getClass().getResource("/snakesAndLadders/Die4.png"));
    ImageIcon e = new ImageIcon(getClass().getResource("/snakesAndLadders/Die5.png"));
    ImageIcon f = new ImageIcon(getClass().getResource("/snakesAndLadders/Die6.png"));

    int score = 0; //Keeps track of player's main score
    int compscore = 0; //Computer's score
    int highscore = 0; //Player's high score
    int playerwins = 0; //Tracks number of games won by player
    int compwins = 0; //Tracks number of games won by computer
    /*playerChoice switches between player 1 and computer, if false, if false, 
    it's player 1's turn, if true, it's the computer's turn      
     */
    boolean playerChoice = false;
    
    
    //removed because of new audio system
    /*File sound;
    AudioInputStream win, lose, die;
    Clip clip;*/
    
    int player1highscore = 0;
    int player1record = 100;
    int player2highscore = 0;
    int player2record = 100;
    //keeps track of the highscores

    /**
     * Creates new form snakesWindow
     */
    public snakesWindow() {
        initComponents();

    }

    //Method which handles drawing the dice, displays what player rolled.
    public void dice() {
        //Selects a random number between 1-6, then set as dice variable
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
        //Value rolled from dice is added to the score variable
        score += diceroll;
        if(score < 7)
        {
        status.setText("You landed on " + (score+1) + ".");
        } else {
        status.setText("You landed on " + score + ".");
        playerScoreText.setText(Integer.toString(score));
        }
        /*If statement structure which handles both the snake and ladder components.
        If the score is at a position of a snake, the score is reduced. If the score is at 
        a position of a ladder, it is increased
         */
        if (score == 24) {
            score -= 8;
            status.setText("You landed on a snake and went down to 16!");
        }
        if (score == 18) {
            score -= 6;
            status.setText("You landed on a snake and went down to 12!");
        }
        if (score == 20) {
            score -= 9;
            status.setText("You landed on a snake and went down to 21!");
        }
        if (score == 17 || score == 13) {
            score += 6;
            status.setText("You landed on a ladder and went up to 23!");
        }
        if (score == 2) {
            score += 12;
            status.setText("You landed on a ladder and went up to 14!");
        }

        /*If statement structure which handles the positoning of player 1's piece,
        (blue). If the score matches the statement, it is set to the pre-determined
        coordinate in the JFrame. It then swtiches over to the computer and loops
        back to the dice method        
         */
        if (score <= 5) {
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
            /*Winning move for player 1, records highscore, reenables restart button and 
            sends String value to the winner method. Dice button is disabled to prevent further 
            input. Also adds to recorded number of wins and prints to the text field
             */
            score = 25;
            blue.setLocation(930, 20);
            diceButton.setEnabled(false);
            highscore = 25;
            restartButton.setEnabled(true);
        playerScoreText.setText(Integer.toString(score)); //Sets player 1's score to the text field

            //play the win sound
            winSound.play();
            
            
            playerwins++;
            playerWinsText.setText(Integer.toString(playerwins));
            winner("Player 1");
            if(player1highscore<player1record)
            {
                player1HighscoreText.setText(Integer.toString(player1highscore));
                player1highscore = player1record;
            }
            //if the number of rolls was the lowest for any game, it will update the highscore
            
    }
    }

    //diceHandler2 method for computer. Receives the dice value rolled from the dice method.
    public void diceHandler2(int diceroll) {
        //Value rolled from dice is added to the score variable
        compscore += diceroll;
        if(compscore < 7)
        {
        status.setText("<html>Computer landed on " + (compscore+1) + ".<html/>");
        } else {
        status.setText("<html>Computer landed on " + compscore + ".<html/>");
        compScore.setText(Integer.toString(compscore));
        }
        
        if(diceroll == 1)
        {
            diceImage1.setIcon(a);
        } else if(diceroll == 2) {
            diceImage1.setIcon(b);
        } else if(diceroll == 3) {
            diceImage1.setIcon(c);
        } else if(diceroll == 4) {
            diceImage1.setIcon(d);
        } else if(diceroll == 5) {
            diceImage1.setIcon(e);
        } else if(diceroll == 6) {
            diceImage1.setIcon(f);
        }

        /*If statement structure which handles both the snake and ladder components.
        If the score is at a position of a snake, the score is reduced. If the score is at 
        a position of a ladder, it is increased
         */
        if (compscore == 24) {
            compscore -= 8;
            status1.setText("<html>Computer landed on a snake and went down to 16!<html/>");
        }
        if (compscore == 18) {
            compscore -= 6;
            status1.setText("<html>Computer landed on a snake and went down to 12!<html/>");
        }
        if (compscore == 20) {
            compscore -= 9;
            status1.setText("<html>Computer landed on a snake and went down to 11!<html/>");
        }
        if (compscore == 17 || compscore == 13) {
            compscore += 6;
            status1.setText("<html>Computer landed on a ladder and went up to 23!<html/>");
        }
        if (compscore == 2) {
            compscore += 12;
            status1.setText("<html>Computer landed on a ladder and went up to 14!<html/>");
        }
        /*If statement structure which handles the positoning of the computer's piece,
        (red). If the score matches the statement, it is set to the pre-determined
        coordinate in the JFrame. It then swtiches over to the computer and loops
        back to the dice method        
         */
        if (compscore <= 5) {
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
            /*Winning move for the computer. Re-enables restart button and 
            sends String value to the winner method. Dice button is disabled to prevent further 
            input. Also adds to recorded number of wins and prints to the text field
             */
            compscore = 25;
            red.setLocation(930, 20);
            diceButton.setEnabled(false);
            restartButton.setEnabled(true);
            
            //play the lose sound
            loseSound.play();
            
            compwins++;
            compWinsText.setText(Integer.toString(compwins));
            winner("The computer");
            if(player2highscore<player2record)
            {
                player2HighscoreText.setText(Integer.toString(player2highscore));
                player2highscore = player2record;
            }
            //if the number of rolls was the lowest for any game, it will update the highscore
        }
        //Sets the computer's score to the text field
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
        diceLabel1 = new javax.swing.JLabel();
        red = new javax.swing.JLabel();
        blue = new javax.swing.JLabel();
        gameboard = new javax.swing.JLabel();
        diceImage = new javax.swing.JLabel();
        diceLabel = new javax.swing.JLabel();
        playerScoreText = new javax.swing.JTextField();
        compScore = new javax.swing.JTextField();
        restartButton = new javax.swing.JButton();
        playerwinLabel = new javax.swing.JLabel();
        compWinsLabel = new javax.swing.JLabel();
        playerWinsText = new javax.swing.JTextField();
        compWinsText = new javax.swing.JTextField();
        playerLabel = new javax.swing.JLabel();
        compLabel = new javax.swing.JLabel();
        status1 = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        diceImage1 = new javax.swing.JLabel();
        player1HighscoreLabel = new javax.swing.JLabel();
        player1HighscoreText = new javax.swing.JTextField();
        player2HighscoreText = new javax.swing.JTextField();
        player2HighscoreLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Snakes and Ladders");

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
        exitButton.setBounds(110, 590, 75, 32);

        diceButton.setText("Roll Dice");
        diceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diceButtonActionPerformed(evt);
            }
        });
        background.add(diceButton);
        diceButton.setBounds(100, 500, 100, 32);

        diceLabel1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        diceLabel1.setText("Computer rolled a:");
        background.add(diceLabel1);
        diceLabel1.setBounds(1100, 330, 150, 40);

        red.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Red playing piece.png"))); // NOI18N
        background.add(red);
        red.setBounds(270, 590, 130, 130);

        blue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Blue playing piece.png"))); // NOI18N
        blue.setText("jLabel1");
        background.add(blue);
        blue.setBounds(290, 590, 130, 110);

        gameboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gameboardremastered.png"))); // NOI18N
        gameboard.setText("jLabel2");
        background.add(gameboard);
        gameboard.setBounds(310, 10, 750, 700);
        background.add(diceImage);
        diceImage.setBounds(100, 380, 100, 100);

        diceLabel.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        diceLabel.setText("You rolled a:");
        background.add(diceLabel);
        diceLabel.setBounds(100, 330, 120, 40);

        playerScoreText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playerScoreTextActionPerformed(evt);
            }
        });
        background.add(playerScoreText);
        playerScoreText.setBounds(1190, 100, 71, 24);
        playerScoreText.setEditable(false);

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
        restartButton.setBounds(100, 540, 100, 32);

        playerwinLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        playerwinLabel.setText("Player Wins:");
        background.add(playerwinLabel);
        playerwinLabel.setBounds(50, 90, 80, 22);

        compWinsLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        compWinsLabel.setText("Computer Wins:");
        background.add(compWinsLabel);
        compWinsLabel.setBounds(40, 150, 110, 22);
        background.add(playerWinsText);
        playerWinsText.setBounds(170, 90, 71, 24);
        playerWinsText.setEditable(false);
        background.add(compWinsText);
        compWinsText.setBounds(170, 150, 70, 24);
        compWinsText.setEditable(false);

        playerLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        playerLabel.setText("Your Score:");
        background.add(playerLabel);
        playerLabel.setBounds(1090, 110, 80, 22);

        compLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        compLabel.setText("Computer Score:");
        background.add(compLabel);
        compLabel.setBounds(1080, 200, 100, 22);

        status1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        background.add(status1);
        status1.setBounds(1070, 500, 200, 110);

        status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        background.add(status);
        status.setBounds(10, 630, 260, 50);
        background.add(diceImage1);
        diceImage1.setBounds(1120, 380, 100, 100);

        player1HighscoreLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        player1HighscoreLabel.setText("Player 1 Highscore:");
        background.add(player1HighscoreLabel);
        player1HighscoreLabel.setBounds(320, 720, 120, 30);
        background.add(player1HighscoreText);
        player1HighscoreText.setBounds(470, 720, 50, 24);
        background.add(player2HighscoreText);
        player2HighscoreText.setBounds(470, 760, 50, 24);

        player2HighscoreLabel.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        player2HighscoreLabel.setText("Computer's Highscore:");
        background.add(player2HighscoreLabel);
        player2HighscoreLabel.setBounds(320, 760, 150, 22);

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
        if(score<25){
        
            //play the die sound
            dieSound.play();
        
        }
        player1highscore++;
        player2highscore++;
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
        /*Restart function. Starts by resetting player 1 and computer's score to 0, 
        then reactivates the Dice button. The positions of the player pieces are returned
        to their original positon, and the text field values are reset.
         */

        score = 0;
        compscore = 0;
        diceButton.setEnabled(true);
        playerChoice = false;
        blue.setLocation(310, 590);
        red.setLocation(310, 590);
        playerScoreText.setText("0");
        compScore.setText("0");
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
            java.util.logging.Logger.getLogger(snakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(snakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(snakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(snakesWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new snakesWindow().setVisible(true);
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
    private javax.swing.JLabel diceImage1;
    private javax.swing.JLabel diceLabel;
    private javax.swing.JLabel diceLabel1;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel gameboard;
    private javax.swing.JLabel player1HighscoreLabel;
    private javax.swing.JTextField player1HighscoreText;
    private javax.swing.JLabel player2HighscoreLabel;
    private javax.swing.JTextField player2HighscoreText;
    private javax.swing.JLabel playerLabel;
    private javax.swing.JTextField playerScoreText;
    private javax.swing.JTextField playerWinsText;
    private javax.swing.JLabel playerwinLabel;
    private javax.swing.JLabel red;
    private javax.swing.JButton restartButton;
    private javax.swing.JLabel status;
    private javax.swing.JLabel status1;
    // End of variables declaration//GEN-END:variables
}
