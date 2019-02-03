package pong;

import mainMenu.arcadeGUI;

//Sounds
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//other imports
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import spaceIntruders.Game.GamePanel;

public class Pong implements ActionListener, KeyListener {

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

    public boolean shutdown = false;

    //intializes new soundeffect objects, set to the sound files in the the resources folder
    public static SoundEffect wallSound = new SoundEffect("/pongResources/pong-wall.wav");
    public static SoundEffect paddleSound = new SoundEffect("/pongResources/pong-paddle.wav");
    public static SoundEffect pointSound = new SoundEffect("/pongResources/pong-point.wav");

    public static Pong pong;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public int width = screenSize.width, height = screenSize.height - 60;
    //public int width = 1280, height = 960;

    //public int width = 1280, height = 940;
    public Renderer renderer;

    public Paddle player1;

    public Paddle player2;

    public Ball ball;

    public boolean bot = false, selectingDifficulty;

    public boolean w, s, up, down;

    public int scoreLimit = 7, playerWon;

    public static int gameStatus = 0;//0 = Menu, 1 = Paused, 2 = Playing, 3 = Over

    public int botDifficulty, botMoves, botCooldown = 0;

    public Random random;

    public JFrame jframe;

    public String player1Name;

    public static String player2Name = "Player 2";

    public static int player2Index;

    public boolean otherMemberExists = false;

    public static void pongStart() {
        pong = new Pong();
    }

    public Pong() {

        player1Name = arcadeGUI.userName;

        Timer timer = new Timer(20, this);
        random = new Random();

        jframe = new JFrame("Pong");

        renderer = new Renderer();

        jframe.setSize(width + 15, height + 35);
        jframe.setVisible(true);

        //fullscreen the game
        jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);

        jframe.add(renderer);
        jframe.addKeyListener(this);

        jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JFrame frame = (JFrame) e.getSource();
                int result = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit the game? your current game will not be counted",
                        "Exit Game", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    close();
                }
            }
        });

        timer.start();
    }

    public void close() {
        GamePanel.shutdown = 1;
        jframe.dispose();
        shutdown = true;
        System.out.println(". . .Closing Pong. . .");
    }

    public void start() {
        gameStatus = 2;
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);
        ball = new Ball(this);
    }

    public void update() {
        if (!shutdown) {
            if (player1.score >= scoreLimit) {
                gameStatus = 3;
                playerWon = 1;
                try {
                    arcadeGUI.setScore(arcadeGUI.getScore() + 1);
                } catch (IOException ex) {
                    Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (player2.score >= scoreLimit) {
                gameStatus = 3;
                playerWon = 2;

                if (otherMemberExists == true) {
                    try {
                        arcadeGUI.setScore(arcadeGUI.getScore(player2Index) + 1, player2Index);
                    } catch (IOException ex) {
                        Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            if (w) {
                player1.move(true);
            }
            if (s) {
                player1.move(false);
            }

            if (!bot) {
                if (up) {
                    player2.move(true);
                }
                if (down) {
                    player2.move(false);
                }
            } else {
                if (botCooldown > 0) {
                    botCooldown--;

                    if (botCooldown == 0) {
                        botMoves = 0;
                    }
                }

                if (botMoves < 10) {
                    if (player2.y + player2.height / 2 < ball.y) {
                        player2.move(false);
                        botMoves++;
                    }

                    if (player2.y + player2.height / 2 > ball.y) {
                        player2.move(true);
                        botMoves++;
                    }

                    if (botDifficulty == 0) {
                        botCooldown = 15;
                    }
                    if (botDifficulty == 1) {
                        botCooldown = 10;
                    }
                    if (botDifficulty == 2) {
                        botCooldown = 3;
                    }

                }
            }

            ball.update(player1, player2);
        }
    }

    public void render(Graphics2D g) {
        if (!shutdown) {
            /*This sets the colours and test that appeir on the screen&*/
            g.setColor(Color.BLACK);// this is the colour of the backgorund
            g.fillRect(0, 0, width, height);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (gameStatus == 0) {
                g.setColor(Color.WHITE);//This sets the colour of the font
                g.setFont(new Font("Arial", 1, 50));

                g.drawString("PONG", width / 2 - 75, 50);//This sets the size

                if (!selectingDifficulty) {
                    g.setFont(new Font("Arial", 1, 30));

                    g.drawString("CTRL To Play with another arcade member", width / 2 - 300, height / 2 - 100);
                    g.drawString("Press Space to Play 2 Player", width / 2 - 200, height / 2 - 25);
                    g.drawString("Press Shift to Play with Bot", width / 2 - 200, height / 2 + 25);
                    g.drawString("<< Score Limit: " + scoreLimit + " >>", width / 2 - 150, height / 2 + 75);
                }
            }
            /* The difficulty is these sets of if statements. the if statements allow you to chose the diffeculty 
                of the game
                also this allows you to chose the bots diffculty
             */
            if (selectingDifficulty) {
                String Diff1 = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : (botDifficulty == 2 ? "Hard" : "Impossible"));

                g.setFont(new Font("Arial", 1, 30));

                g.drawString("<< Bot Difficulty: " + Diff1 + " >>", width / 2 - 180, height / 2 - 25);
                g.drawString("Press Space to Play", width / 2 - 150, height / 2 + 25);
            }

            if (gameStatus == 1) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", 1, 50));
                g.drawString("PAUSED", width / 2 - 103, height / 2 - 25);
            }

            if (gameStatus == 1 || gameStatus == 2) {
                g.setColor(Color.WHITE);

                g.setStroke(new BasicStroke(5f));

                g.drawLine(width / 2, 0, width / 2, height);

                g.setStroke(new BasicStroke(2f));

                g.drawOval(width / 2 - 150, height / 2 - 150, 300, 300);

                g.setFont(new Font("Arial", 1, 50));

                g.drawString(String.valueOf(player1.score), width / 2 - 90, 50);
                g.drawString(String.valueOf(player2.score), width / 2 + 65, 50);

                player1.render(g);
                player2.render(g);
                ball.render(g);
            }

            if (gameStatus == 3) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", 1, 50));

                g.drawString("PONG", width / 2 - 75, 50);

                if (bot && playerWon == 2) {
                    g.drawString("The Bot Wins!", width / 2 - 170, 200);
                } else {
                    if (playerWon == 1) {
                        g.drawString(player1Name + " Wins!", width / 2 - 165, 200);
                    } else {
                        g.drawString(player2Name + " Wins!", width / 2 - 165, 200);
                    }
                }

                g.setFont(new Font("Arial", 1, 30));

                g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
                g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStatus == 2) {
            update();
        }

        renderer.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) {
            up = true;
        } else if (id == KeyEvent.VK_DOWN) {
            down = true;
        } else if (id == KeyEvent.VK_RIGHT) {
            if (selectingDifficulty) {
                if (botDifficulty < 2) {
                    botDifficulty++;
                } else {
                    botDifficulty = 0;
                }
            } else if (gameStatus == 0) {
                scoreLimit++;
            }
        } else if (id == KeyEvent.VK_LEFT) {
            if (selectingDifficulty) {
                if (botDifficulty > 0) {
                    botDifficulty--;
                } else {
                    botDifficulty = 2;
                }
            } else if (gameStatus == 0 && scoreLimit > 1) {
                scoreLimit--;
            }
        } else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3)) {
            gameStatus = 0;
        } else if (id == KeyEvent.VK_ESCAPE && gameStatus == 0) {
            close();
        } else if (id == KeyEvent.VK_SHIFT && gameStatus == 0) {
            bot = true;
            selectingDifficulty = true;
        } else if (id == KeyEvent.VK_SPACE) {
            if (gameStatus == 0) {
                if (!selectingDifficulty) {
                    bot = false;
                } else {
                    selectingDifficulty = false;
                }
                otherMemberExists = false;
                player2Name = "Player 2";
                start();
            } else if (gameStatus == 1) {
                gameStatus = 2;
            } else if (gameStatus == 2) {
                gameStatus = 1;
            } else if (gameStatus == 3) {
                start();
            }
            
            
            
        }else if (id == KeyEvent.VK_CONTROL) {
            if (gameStatus == 0) {
                if (!selectingDifficulty) {
                    bot = false;
                } else {
                    selectingDifficulty = false;
                }
                
                //tells the program that another arcade member is in the game
                otherMemberExists = true;
                //////////////////////////////
                //CODE FOR 2nd PLAYER AS MAHCROSOFT ARCADE MEMBER
                //opens a new pickMemberGUi
                new pickMemberGUI().setVisible(true);

                start();
                //pauses the game
                gameStatus = 1;
            } else if (gameStatus == 1) {
                gameStatus = 2;
            } else if (gameStatus == 2) {
                gameStatus = 1;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = false;
        } else if (id == KeyEvent.VK_S) {
            s = false;
        } else if (id == KeyEvent.VK_UP) {
            up = false;
        } else if (id == KeyEvent.VK_DOWN) {
            down = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
