package spider.invaders;

import spider.invaders.controller.KeyboardController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import mainMenu.arcadeGUI;

//sound
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 *
 *
 */
public class GamePanel extends JPanel {

    private JFrame gameFrame;
    public static int shutdown = 0;

    // Required components. Do not remove!
    private Timer gameTimer;
    private KeyboardController controller;
    // Controls size of game window and framerate
    private final int gameWidth = 800;
    private final int gameHeight = 800;
    private final int framesPerSecond = 120;

    // Added Counters
    Random r = new Random();
    private int score = 0;
    private int level = 1;
    private int numberOfLives = 6;
    private int highScore;
    private int markerX, markerY;
    private static int bossHealth = 45; //30;

    // Added Objects
    private Ship playerShip;
    private Ship singleLife;
    private Ship2 playerShip2;
    private Ship2 singleLife2;
    private Ship bonusEnemy;
    private Enemy enemy;
    private Shield shield;
    private Bullet bullet;
    private Bullet2 bullet2;
    private Beam beam, beam2, beam3;

    // Added Booleans
    private boolean newBulletCanFire = true;
    private boolean newBulletCanFire2 = true;
    private boolean newBeamCanFire = true;
    private boolean newBonusEnemy = true;
    private boolean hitMarker = false;

    // Added Array Lists
    private ArrayList<Ship> lifeList = new ArrayList();
    private ArrayList<Ship> bonusEnemyList = new ArrayList();
    private ArrayList<Ship2> lifeList2 = new ArrayList();
    private ArrayList<Ship2> bonusEnemyList2 = new ArrayList();
    private ArrayList<Enemy> enemyList = new ArrayList();
    private ArrayList<Shield> shieldList = new ArrayList();
    private ArrayList<Beam> beamList = new ArrayList();
    private ImageIcon background = new ImageIcon(getClass().getResource("/spiderInvadersResources/images/backgroundSkin.jpg"));

    // Added Audio files and soundEffects
    SoundEffect beamSound = new SoundEffect("/spaceInvadersResources/sounds/alienBeam.wav");
    SoundEffect bulletSound = new SoundEffect("/spaceInvadersResources/sounds/bulletSound.wav");
    SoundEffect levelUpSound = new SoundEffect("/spaceInvadersResources/sounds/levelUpSound.wav");
    SoundEffect deathSound = new SoundEffect("/spaceInvadersResources/sounds/deathSound.wav");
    SoundEffect hitmarkerSound = new SoundEffect("/spaceInvadersResources/sounds/hitmarkerSound.wav");
    SoundEffect shieldSound = new SoundEffect("/spaceInvadersResources/sounds/shieldSound.wav");
    SoundEffect bossSound = new SoundEffect("/spaceInvadersResources/sounds/bossSound.wav");
    SoundEffect bonusSound = new SoundEffect("/spaceInvadersResources/sounds/bonusSound.wav");
    SoundEffect damageSound = new SoundEffect("/spaceInvadersResources/sounds/damageSound.wav");

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// EXTRA METHODS
// Used in the Enemy class to help with the draw method for the boss
    //
    public static int getBossHealth() {
        return bossHealth;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
//SOUNDS CLASS
    public class SoundEffect {

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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETUP GAME
    public final void setupGame() {

        // Sets enemies for normal levels
        if (level != 3 && level != 6 && level != 9 && level != 12) {
            // Six rows
            for (int row = 0; row < 6; row++) {
                // 5 columns
                for (int column = 0; column < 5; column++) {
                    enemy = new Enemy((20 + (row * 100)), (20 + (column * 60)), level, 0, column, null, 40, 40); // Enemy speed will increase each level
                    enemyList.add(enemy);
                }
            }
        }
        // Sets enemy for boss levels
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            bossSound.play();
            enemy = new Enemy(20, 20, 3, 0, 100, null, 150, 150);
            enemyList.add(enemy);
        }
        // Gives directions on level 1
        if (level == 1) {
            JOptionPane.showMessageDialog(null, "Welcome to Spider Intruders!\n\nTHINGS TO KNOW:\n\n- Use left/right arrow keys to move\n- Press spacebar to shoot\n- The enemies get faster every level"
                    + "\n- BOSS every 3 levels\n- A bonus enemy will appear randomly\n- Shoot it for extra points!\n- Press R to reset high score\n- All pixel art is original\n- Your highscore will save when you die\n\nHAVE FUN!");
        }
        // Resets all controller movement
        controller.resetController();

        // Sets the player's ship values   
        playerShip = new Ship(375, 730, null, controller);

        // Sets the life counter Ships
        for (int column = 0; column < numberOfLives; column++) {
            singleLife = new Ship(48 + (column * 20), 10, Color.WHITE, null);
            lifeList.add(singleLife);
        }
        playerShip2 = new Ship2(370, 730, null, controller);
// Sets the life counter Ships
        for (int column = 0; column < numberOfLives; column++) {
            singleLife2 = new Ship2(48 + (column * 20), 10, Color.WHITE, null);
            lifeList2.add(singleLife2);
        }
        // Sets the values for 3 rows and 3 columns of shields
        for (int row = 0;
                row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                shield = new Shield(100 + (column * 250), 650 - (row * 10), 70, 10, Color.RED);
                shieldList.add(shield);
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PAINT
    @Override
    public void paint(Graphics g) {
        if (shutdown == 0) {
            // Sets background image
            background.paintIcon(null, g, 0, -150);

            // makes a string that says "+100" on enemy hit
            if (bullet != null) {
                if (hitMarker) {
                    g.setColor(Color.WHITE);
                    if (level != 3 && level != 6 && level != 9 && level != 12) {
                        g.drawString("+ 100", markerX + 20, markerY -= 1);
                    } else {
                        g.drawString("- 1", markerX + 75, markerY += 1);
                    }
                }
            }
            // Draws the player's ship
            playerShip.draw(g);
            playerShip2.draw(g);

            // Draws 3 evenly-spaced shields 
            for (int index = 0; index < shieldList.size(); index++) {
                shieldList.get(index).draw(g);
            }

            //Draws 3 different kinds of aliens
            try {
                for (int index = 0; index < enemyList.size(); index++) {
                    enemyList.get(index).draw(g);
                }

            } catch (IndexOutOfBoundsException e) {
            }

            // Draw a bullet on space bar press
            if (controller.getKeyStatus(32)) {
                if (newBulletCanFire) {
                    bullet = new Bullet(playerShip.getXPosition() + 22, playerShip.getYPosition() - 20, 0, Color.WHITE);
                    bulletSound.play(); // Plays bullet sound
                    newBulletCanFire = false;
                }
            }
            if (controller.getKeyStatus(87)) {
                if (newBulletCanFire2) {
                    bullet2 = new Bullet2(playerShip2.getXPosition() + 22, playerShip2.getYPosition() - 20, 0, Color.RED);
                    bulletSound.play(); // Plays bullet sound
                    newBulletCanFire2 = false;
                }
            }
            // Only attempts to draw bullet after key press
            if (bullet != null) {
                bullet.draw(g);
            }
            if (bullet2 != null) {
                bullet2.draw(g);
            }
            // Generates random beams shot from enemies
            if (level != 3 && level != 6 && level != 9 && level != 12) {
                if (newBeamCanFire) {
                    for (int index = 0; index < enemyList.size(); index++) {
                        if (r.nextInt(30) == index) {
                            beam = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition(), 0, Color.GREEN);
                            beamList.add(beam);
                            // Plays beam sound for normal enemies
                            beamSound.play();
                        }
                        newBeamCanFire = false;
                    }
                }
            }
            // Generates beams at a faster rate for boss
            if (level == 3 || level == 6 || level == 9 || level == 12) {
                if (newBeamCanFire) {
                    for (int index = 0; index < enemyList.size(); index++) {
                        if (r.nextInt(5) == index) {
                            beam = new Beam(enemyList.get(index).getXPosition() + 75, enemyList.get(index).getYPosition() + 140, 0, Color.BLACK);
                            beam2 = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition() + 110, 0, Color.BLACK);
                            beam3 = new Beam(enemyList.get(index).getXPosition() + 150, enemyList.get(index).getYPosition() + 110, 0, Color.BLACK);
                            beamList.add(beam);
                            beamList.add(beam2);
                            beamList.add(beam3);
                            // Plays beam sound for normal enemies
                            beamSound.play();
                        }
                        newBeamCanFire = false;
                    }
                }
            }
            // Draws the generated beams
            for (int index = 0; index < beamList.size(); index++) {
                beamList.get(index).draw(g);
            }
            // Generates random bonus enemy
            if (newBonusEnemy) {
                if (r.nextInt(3000) == 1500) {
                    bonusEnemy = new Ship(-50, 30, Color.RED, null);
                    bonusEnemyList.add(bonusEnemy);
                    newBonusEnemy = false;
                }
            }
            // Draws bonus enemy
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                bonusEnemyList.get(index).bonusDraw(g);
            }

            // Sets the score display
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 260, 20);

            // Sets the life counter display
            g.setColor(Color.WHITE);
            g.drawString("Lives:", 11, 20);
            for (int index = 0; index < lifeList.size(); index++) {
                lifeList.get(index).lifeDraw(g);
            }
            // Sets level display
            g.setColor(Color.WHITE);
            g.drawString("Level " + level, 750, 20);

            // Sets Highscore display
            g.setColor(Color.WHITE);
            g.drawString("Highscore: " + highScore, 440, 20);

            // Draws a health display for boss level
            if (level == 3 || level == 6 || level == 9 || level == 12) {
                g.setColor(Color.BLACK);
                g.drawString("Boss Health: " + bossHealth, 352, 600);
            }
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// UPDATE GAME STATE
    public void updateGameState(int frameNumber) throws IOException {
        if (shutdown == 0) {
            // Allows player to move left and right
            playerShip.move();
            playerShip2.move();
            
            // Adds option to reset highScore
            if (controller.getKeyStatus(82)) {
                int answer = JOptionPane.showConfirmDialog(null, "Would you like to reset the high score?", ":)", 0);
                controller.resetController();
                if (answer == 0) {
                    arcadeGUI.setScore(0);
                    highScore = 0;
                }
            }

            // Makes enemies move and change direction at borders
            if ((enemyList.get(enemyList.size() - 1).getXPosition() + enemyList.get(enemyList.size() - 1).getXVelocity()) > 760 || (enemyList.get(0).getXPosition() + enemyList.get(0).getXVelocity()) < 0) {
                for (int index = 0; index < enemyList.size(); index++) {
                    enemyList.get(index).setXVelocity(enemyList.get(index).getXVelocity() * -1);
                    enemyList.get(index).setYPosition(enemyList.get(index).getYPosition() + 10);
                }
            } else {
                for (int index = 0; index < enemyList.size(); index++) {
                    enemyList.get(index).move();
                }
            }

            // Move bullet
            if (bullet != null) {
                bullet.setYPosition(bullet.getYPosition() - 15);
                if (bullet.getYPosition() < 0) {
                    newBulletCanFire = true;
                }
                if (bullet2 != null) {
                    bullet2.setYPosition(bullet2.getYPosition() - 15);
                    if (bullet2.getYPosition() < 0) {
                        newBulletCanFire2 = true;
                    }
                    // Checks for collisions with normal enemies
                    for (int index = 0; index < enemyList.size(); index++) {
                        if (bullet.isColliding(enemyList.get(index)) /*|| bullet2.isColliding(enemyList.get(index))*/) {
                            hitmarkerSound.play();
                            bullet = new Bullet(0, 0, 0, null);
                            //bullet2 = new Bullet2(0, 0, 0, null);
                            newBulletCanFire = true;
                            //newBulletCanFire2 = true;
                            // Updates score for normal levels
                            if (level != 3 && level != 6 && level != 9 && level != 12) {
                                score += 100;
                                hitMarker = true;
                                markerX = enemyList.get(index).getXPosition(); // Gets positions that the "+ 100" spawns off of
                                markerY = enemyList.get(index).getYPosition();
                                enemyList.remove(index);

                            }
                            // Updates score for boss levels
                            if (level == 3 || level == 6 || level == 9 || level == 12) {
                                hitMarker = true;
                                markerX = enemyList.get(index).getXPosition(); // Gets positions that the "- 1" spawns off of
                                markerY = enemyList.get(index).getYPosition() + 165;
                                bossHealth -= 1;
                                if (bossHealth == 0) {
                                    enemyList.remove(index);
                                    score += 9000 + level * 10;// Bonus score for defeating boss
                                }
                            }
                        }
                    }
                    for (int index = 0; index < enemyList.size(); index++) {
                        if (bullet.isColliding(enemyList.get(index)) /*|| bullet2.isColliding(enemyList.get(index))*/) {
                            hitmarkerSound.play(); // Plays hitmarker sound if you hit an enemy
                            bullet = new Bullet(0, 0, 0, null);
                            //bullet2 = new Bullet2(0, 0, 0, null);
                            newBulletCanFire = true;
                            //newBulletCanFire2 = true;
                            // Updates score for normal levels
                            if (level != 3 && level != 6 && level != 9 && level != 12) {
                                score += 100;
                                hitMarker = true;
                                markerX = enemyList.get(index).getXPosition(); // Gets positions that the "+ 100" spawns off of
                                markerY = enemyList.get(index).getYPosition();
                                enemyList.remove(index);

                            }
                            // Updates score for boss levels
                            if (level == 3 || level == 6 || level == 9 || level == 12) {
                                hitMarker = true;
                                markerX = enemyList.get(index).getXPosition(); // Gets positions that the "- 1" spawns off of
                                markerY = enemyList.get(index).getYPosition() + 165;
                                bossHealth -= 1;
                                if (bossHealth == 0) {
                                    enemyList.remove(index);
                                    score += 9000 + level * 10;// Bonus score for defeating boss
                                }
                            }
                        }
                    }
                    for (int index = 0; index < enemyList.size(); index++) {
                        if (bullet2.isColliding(enemyList.get(index)) /*|| bullet2.isColliding(enemyList.get(index))*/) {
                            hitmarkerSound.play();// Plays hitmarker sound if you hit an enemy
                            bullet2 = new Bullet2(0, 0, 0, null);
                            //bullet2 = new Bullet2(0, 0, 0, null);
                            newBulletCanFire2 = true;
                            //newBulletCanFire2 = true;
                            // Updates score for normal levels
                            if (level != 3 && level != 6 && level != 9 && level != 12) {
                                score += 100;
                                hitMarker = true;
                                markerX = enemyList.get(index).getXPosition(); // Gets positions that the "+ 100" spawns off of
                                markerY = enemyList.get(index).getYPosition();
                                enemyList.remove(index);

                            }
                            // Updates score for boss levels
                            if (level == 3 || level == 6 || level == 9 || level == 12) {
                                hitMarker = true;
                                markerX = enemyList.get(index).getXPosition(); // Gets positions that the "- 1" spawns off of
                                markerY = enemyList.get(index).getYPosition() + 165;
                                bossHealth -= 1;
                                if (bossHealth == 0) {
                                    enemyList.remove(index);
                                    score += 9000 + level * 10;// Bonus score for defeating boss
                                }
                            }
                        }
                    }
                    // Checks for collisions with shield and bullets
                    for (int index = 0; index < shieldList.size(); index++) {
                        if (bullet.isColliding(shieldList.get(index))/*||bullet2.isColliding(shieldList.get(index))*/) {
                            // Each if statement changes color of the shield, indicating "strength"
                            // STRONG
                            if (shieldList.get(index).getColor() == Color.RED) {
                                shieldList.get(index).setColor(Color.ORANGE);
                                // Plays sound if shield takes damage
                                shieldSound.play(); // Plays sound if shield takes damage
                                bullet = new Bullet(0, 0, 0, null);
                                //bullet2 = new Bullet2(0, 0, 0, null);
                                newBulletCanFire = true;
                                //newBulletCanFire2 = true;
                                // GOOD
                            } else if (shieldList.get(index).getColor() == Color.ORANGE) {
                                shieldList.get(index).setColor(Color.YELLOW);
                                // Plays sound if shield takes damage
                                shieldSound.play();
                                bullet = new Bullet(0, 0, 0, null);
                                newBulletCanFire = true;
                                //newBulletCanFire2 = true;
                                // OKAY
                            } else if (shieldList.get(index).getColor() == Color.YELLOW) {
                                shieldList.get(index).setColor(Color.WHITE);
                                // Plays sound if shield takes damage
                                shieldSound.play();
                                bullet = new Bullet(0, 0, 0, null);
                                newBulletCanFire = true;
                                // newBulletCanFire2 = true;
                                // WEAK, BREAKS ON HIT
                            } else if (shieldList.get(index).getColor() == Color.WHITE) {
                                shieldList.remove(index);
                                // Plays sound if shield takes damage
                                shieldSound.play();
                                bullet = new Bullet(0, 0, 0, null);
                                newBulletCanFire = true;
                                //  newBulletCanFire2 = true;
                            }
                        } else if ((bullet2.isColliding(shieldList.get(index)))) {

                            if (bullet2.isColliding(shieldList.get(index))/*||bullet2.isColliding(shieldList.get(index))*/) {
                                // Each if statement changes color of the shield, indicating "strength"
                                // STRONG
                                if (shieldList.get(index).getColor() == Color.RED) {
                                    shieldList.get(index).setColor(Color.ORANGE);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    bullet2 = new Bullet2(0, 0, 0, null);
                                    //bullet2 = new Bullet2(0, 0, 0, null);
                                    newBulletCanFire2 = true;
                                    //newBulletCanFire2 = true;
                                    // GOOD
                                } else if (shieldList.get(index).getColor() == Color.ORANGE) {
                                    shieldList.get(index).setColor(Color.YELLOW);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    bullet2 = new Bullet2(0, 0, 0, null);
                                    newBulletCanFire2 = true;
                                    //newBulletCanFire2 = true;
                                    // OKAY
                                } else if (shieldList.get(index).getColor() == Color.YELLOW) {
                                    shieldList.get(index).setColor(Color.WHITE);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    bullet2 = new Bullet2(0, 0, 0, null);
                                    newBulletCanFire2 = true;
                                    // newBulletCanFire2 = true;
                                    // WEAK, BREAKS ON HIT
                                } else if (shieldList.get(index).getColor() == Color.WHITE) {
                                    shieldList.remove(index);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    bullet2 = new Bullet2(0, 0, 0, null);
                                    newBulletCanFire2 = true;
                                    //  newBulletCanFire2 = true;  
                                }

                            }
                        }
                    }

                }
                // Moves bonus enemy
                if (!bonusEnemyList.isEmpty()) {
                    for (int index = 0; index < bonusEnemyList.size(); index++) {
                        bonusEnemyList.get(index).setXPosition(bonusEnemyList.get(index).getXPosition() + (2));
                        if (bonusEnemyList.get(index).getXPosition() > 800) {
                            bonusEnemyList.remove(index);
                            newBonusEnemy = true;
                        }
                    }
                    // bonus enemy and bullet collsion
                    for (int index = 0; index < bonusEnemyList.size(); index++) {
                        if (bullet != null || bullet2 != null) {
                            if (bonusEnemyList.get(index).isColliding(bullet)) {
                                bonusEnemyList.remove(index);
                                bullet = new Bullet(0, 0, 0, null);
                                bullet2 = new Bullet2(0, 0, 0, null);
                                newBulletCanFire = true;
                                newBulletCanFire2 = true;
                                newBonusEnemy = true;
                                // Plays sound if player hits a bonus enemy
                                bonusSound.play();
                                score += 5000; // add bonus to score on hit
                            }
                        }
                    }

                }

                // Moves beams on normal levels
                if (level != 3 && level != 6 && level != 9 && level != 12) {
                    if (beam != null) {
                        for (int index = 0; index < beamList.size(); index++) {
                            beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (4));
                            if (beamList.get(index).getYPosition() > 800) {
                                beamList.remove(index);
                            }
                        }
                    }
                }
                // Moves beams at a faster speed for boss
                if (level == 3 || level == 6 || level == 9 || level == 12) {
                    if (beam != null) {
                        for (int index = 0; index < beamList.size(); index++) {
                            beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (2 * level)); //Boss beam speed will increase each level
                            if (beamList.get(index).getYPosition() > 800) {
                                beamList.remove(index);
                            }
                        }
                    }
                }

                // Checks for beam and shield collisions
                try {
                    for (int j = 0; j < shieldList.size(); j++) {
                        for (int index = 0; index < beamList.size(); index++) {
                            if (beamList.get(index).isColliding(shieldList.get(j))) {
                                // STRONG
                                if (shieldList.get(j).getColor() == Color.RED) {
                                    shieldList.get(j).setColor(Color.ORANGE);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    beamList.remove(index);
                                    // GOOD
                                } else if (shieldList.get(j).getColor() == Color.ORANGE) {
                                    shieldList.get(j).setColor(Color.YELLOW);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    beamList.remove(index);
                                    // OKAY
                                } else if (shieldList.get(j).getColor() == Color.YELLOW) {
                                    shieldList.get(j).setColor(Color.WHITE);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    beamList.remove(index);
                                    // WEAK, BREAKS ON HIT
                                } else if (shieldList.get(j).getColor() == Color.WHITE) {
                                    shieldList.remove(j);
                                    // Plays sound if shield takes damage
                                    shieldSound.play();
                                    beamList.remove(index);
                                }
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                }

                // Checks for beam and player collisions
                for (int index = 0; index < beamList.size(); index++) {
                    if (beamList.get(index).isColliding(playerShip) || beamList.get(index).isColliding(playerShip2)) {
                        beamList.remove(index);
                        // Plays damage sound
                        damageSound.play();
                        lifeList.remove(lifeList.size() - 1); // Removes life if hit by bullet
                    }
                }

                // Paces beam shooting by only allowing new beams to be fired once all old beams are off screen or have collided
                if (beamList.isEmpty()) {
                    newBeamCanFire = true;
                }

                //Destroys shields if aliens collide with them
                for (int input = 0; input < enemyList.size(); input++) {
                    for (int j = 0; j < shieldList.size(); j++) {
                        if (enemyList.get(input).isColliding(shieldList.get(j))) {
                            shieldList.remove(j);
                        }
                    }
                    // If aliens exceed this X Position, you reset the level and lose a life
                    if (enemyList.get(input).getYPosition() + 50 >= 750) {
                        enemyList.clear();
                        shieldList.clear();
                        lifeList.clear();
                        beamList.clear();
                        bossHealth = 10 * level;
                        numberOfLives -= 1;
                        // Plays death sound when enemies reach bottom
                        deathSound.play();
                        setupGame();
                    }
                }

                //Updates the life counter display 
                if (playerShip.isColliding || playerShip2.isColliding) {
                    int index = lifeList.size() - 1;
                    lifeList.remove(index);
                } // Ends game if player runs out of lives
                else if (lifeList.isEmpty()) {

                    deathSound.play(); // Plays death sound when you run out of lives
                    // Gives the player an option to play again or exit
                    int answer = JOptionPane.showConfirmDialog(null, "Would you like to play again?", "You lost the game with " + score + " points", 0);
                    // If they choose to play again, this resets every element in the game
                    if (answer == 0) {
                        try {
                            int tempHighScore = arcadeGUI.getScore();
                            if (tempHighScore < score) {
                                arcadeGUI.setScore(score);
                                highScore = score;
                            } else {
                                highScore = tempHighScore;
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(spaceIntruders.Game.GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        lifeList.clear();
                        enemyList.clear();
                        shieldList.clear();
                        beamList.clear();
                        bonusEnemyList.clear();
                        score = 0;
                        level = 1;
                        bossHealth = 10 * level; //30;
                        numberOfLives = 6;
                        newBulletCanFire = true;
                        newBulletCanFire2 = true;
                        newBeamCanFire = true;
                        newBonusEnemy = true;
                        setupGame();
                    }
                    // If they choose not to play again, it closes the game
                    if (answer == 1) {
                        try {
                            int tempHighScore = arcadeGUI.getScore();
                            if (tempHighScore < score) {
                                arcadeGUI.setScore(score);
                                highScore = score;
                            } else {
                                highScore = tempHighScore;
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(spaceIntruders.Game.GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        shutdown = 1;
                        System.out.println(". . .Closing SpaceIntruders . . .");
                        gameFrame.dispose();
                    }
                }

                // Goes to next level, resets all lists, sets all counters to correct values
                if (enemyList.isEmpty()) {
                    beamList.clear();
                    shieldList.clear();
                    bonusEnemyList.clear();
                    lifeList.clear();
                    level += 1;
                    bossHealth = 10 * level;//30;
                    setupGame();
                    // Plays level up sound
                    levelUpSound.play();
                }

            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GAME PANEL    

    public GamePanel(JFrame frame) {
        try {
            this.highScore = arcadeGUI.getScore();
        } catch (IOException ex) {
            Logger.getLogger(spaceIntruders.Game.GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.gameFrame = frame;

        // Set the size of the Panel
        this.setSize(gameWidth, gameHeight);
        this.setPreferredSize(new Dimension(gameWidth, gameHeight));
        this.setBackground(Color.BLACK);

        // Register KeyboardController as KeyListener
        controller = new KeyboardController();
        this.addKeyListener(controller);

        // Call setupGame to initialize fields
        this.setupGame();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Method to start the Timer that drives the animation for the game. It is
     * not necessary for you to modify this code unless you need to in order to
     * add some functionality.
     */
    public void start() {
        shutdown = 0;
        // Set up a new Timer to repeat every 20 milliseconds (50 FPS)
        gameTimer = new Timer(1000 / framesPerSecond, new ActionListener() {

            // Tracks the number of frames that have been produced.
            // May be useful for limiting action rates
            private int frameNumber = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Update the game's state and repaint the screen
                    updateGameState(frameNumber++);
                } catch (IOException ex) {
                    Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                repaint();
            }
        });
        Timer gameTimerHitMarker = new Timer(1000, new ActionListener() {

            // Tracks the number of frames that have been produced.
            // May be useful for limiting action rates
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the game's state and repaint the screen
                hitMarker = false;
            }
        });

        gameTimer.setRepeats(true);
        gameTimer.start();
        gameTimerHitMarker.setRepeats(true);
        gameTimerHitMarker.start();
    }

}
