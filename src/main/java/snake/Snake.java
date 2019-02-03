package snake;

import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import mainMenu.arcadeGUI;


public class Snake {
    
    private JPanel board;                                                       //the jPanel for the gameboard
    private JButton[] snakeBodyPart;                                            //the body of the snake
    private JTextArea scoreViewer;                                              //where the score is going to be displayed 
    
    private final int speed = 50;                                               //the speed of the snake
    private final int BoardWidth = 900;                                         //the witdth of the board
    private final int BoardHight = 550;                                         //the hight of the board
    private final int ScoreBoarderHight = 50;                                   //the hight of the score board
    private final int Length = 4;                                               //the initial length of the snake
    private final int Body = 25;                                                //the width and length of the body 
    private final Point POINT = new Point(200, 150);                            //where the snake is going to spawn

    private enum GAME_TYPE {Borderless, BORDER};                                //the two different game type,with and without the boarder            
    private GAME_TYPE selectedGameType = GAME_TYPE.Borderless;                  //use enums to avoid runtime errors              
    private int totalBodyPart;                                                  //the total length of the snake body
    private int directionX;                                                     //the number of pixels the snake is going on x axis                 
    private int directionY;                                                     //the number of pixels the snake is going on y axis
    private int score;                                                          //the score of the current game
    private boolean Left;                                                       //for direction of movements
    private boolean Right;
    private boolean Up;
    private boolean Down;
    private boolean isRunning;                                                  //determine or check whether the snake should be runing or not
    private Random random = new Random();                                       //for spawning the food, a random location
    private boolean start;                                                      //to see if the user clicked play or not
    private final Border lineBorder = new LineBorder(new Color(96,185,208));
    
    private ImageIcon dot = new ImageIcon(getClass().getResource("/snake/dot.jpg"));                         //importing images 
    private ImageIcon ghost = new ImageIcon(getClass().getResource("/snake/ghost.jpg"));               
    private ImageIcon faceRight = new ImageIcon(getClass().getResource("/snake/headRight.jpg"));
    private ImageIcon faceLeft = new ImageIcon(getClass().getResource("/snake/headLeft.jpg"));
    private ImageIcon faceUp = new ImageIcon(getClass().getResource("/snake/headUp.jpg"));
    private ImageIcon faceDown = new ImageIcon(getClass().getResource("/snake/headDown.jpg"));
    
   

    public Snake() {
        
        resetDefaultValues();                                                   //initialize all variables.
        
        init();                                                                 // initialize GUI.
        
        createInitSnake();                                                      // Create Initial body of a snake.
        
        isRunning = true;                                                       // Initialize Thread.
        createThread();
    }

    public void init() {
        
    
    
        final JFrame frame = new JFrame("Snake");                                     //the container
        frame.setSize(900, 650);
       
        setJMenueBar(frame);                                                    //Create Menue bar with functions
        
        JPanel scorePanel = new JPanel();                                       //where the score is going to be
        scoreViewer = new JTextArea("Current score: " + score);
        scoreViewer.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));        //the font and size 
        scoreViewer.setEnabled(false);
        scoreViewer.setBackground(new Color(247,76,39));                        //color

        board = new JPanel();                                                   //the container
        board.setLayout(null);                                                  //disabled the layout manager
        board.setBounds(0, 0, BoardWidth, BoardHight);                          //the size, color
        board.setBackground(new Color(86,185,208));
        scorePanel.setLayout(new GridLayout(0, 1));
        scorePanel.setBounds(0, BoardHight, BoardWidth, ScoreBoarderHight);
        scorePanel.setBackground(new Color(0,0,0));
        scorePanel.add(scoreViewer);                                            //will contain score board

        //final JButton play = new JButton("Play");
        
        //frame.setLayout(new BorderLayout());
        //frame.add(play, BorderLayout.CENTER);                                 //adding the button in the middle of the panel
        
//        play.setBounds(350, 250, 200, 100);
//        play.setBackground(new Color(247,76,39));
//        
//        play.addActionListener(new ActionListener() {                         //show the current score board whent the play button is clicked
//            public void actionPerformed(ActionEvent evt) {
//             play.setVisible(false);
//             play.setEnabled(false);
//             start = true;
//             }
//            });
        
        //board.add(play);
        frame.getContentPane().setLayout(null);                                  
        frame.getContentPane().add(board);                                      //adding the board intothe rame
        frame.getContentPane().add(scorePanel);                                 //adding the score into the frame
        frame.setVisible(true);                                                 //make it visible
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addKeyListener(new KeyAdapter() {                                 //adding the key listner for the movements 
            @Override
            public void keyPressed(KeyEvent e) {                                //to see which key they pressed
                snakeKeyPressed(e);
            }
        });
        // Add a listener to exit the JVM on window close
        frame.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent event){
                    isRunning = false;
                    frame.dispose();
                }
            });
        
        //request focus on the snake game window so that key presses register on this screen, not the arcade window
        frame.setFocusable(true);
        frame.requestFocus();
        
        frame.setResizable(false);                                              //users can't resize the window

    }
    public void setJMenueBar(final JFrame frame) {

        JMenuBar myMBar = new JMenuBar();                                       //adding menu bar

        JMenu game = new JMenu("Game");
        JMenuItem newgame = new JMenuItem("New Game");
        JMenuItem exit = new JMenuItem("Exit");
        newgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startNewGame();                                                 //create a new game
            }
        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isRunning=false;                                                //exit the game
                frame.dispose();
            }
        });
        game.add(newgame);                                                      //adding all of them into the window
        game.addSeparator();
        game.add(exit);
        myMBar.add(game);

        JMenu type = new JMenu("Type");
        JMenuItem noMaze = new JMenuItem("No Maze");
        noMaze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedGameType = GAME_TYPE.Borderless;                        //the game has no border
                startNewGame();                                                 //start a new game
            }
        });
        JMenuItem border = new JMenuItem("Border Maze");
        border.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedGameType = GAME_TYPE.BORDER;                            //the game hs border
                startNewGame();                                                 //start a new game
            }
        });
        type.add(noMaze);                                                       //adding them into the game
        type.add(border);
        myMBar.add(type);

        frame.setJMenuBar(myMBar);                                              //changing the default menu bar to myMBar
    }


    public void resetDefaultValues() {                                          //the default value of the game
        snakeBodyPart = new JButton[2000];                                      //the maximun number of the body parts
        
        totalBodyPart = Length;
        directionX = Body;                                                      //start out moving to the right
        directionY = 0;
        score = 0;                                                              //0 score
        Left = false;
        Right = true;
        Up = true;
        Down = true;
    }

    void startNewGame() {                                                       
        resetDefaultValues();                                   
        board.removeAll();                                                      //reset the gameplay area
        createInitSnake();                                                      //create the snake
        createThread();                                                         //start a new thread
        scoreViewer.setText("Current Score" + score);
        isRunning = true;                                                       //the snake is moving
    }

   
    public void createInitSnake() {
                                                                                // Location of the snake's head.
        int x = (int) POINT.getX();
        int y = (int) POINT.getY();

                                                                                // Initially the snake has three body part.
        snakeBodyPart[0] = new JButton(faceRight);                              //the head of the snake
        snakeBodyPart[0].setBorder(lineBorder);
            snakeBodyPart[0].setBounds(x, y, Body, Body);
            snakeBodyPart[0].setBackground(new Color(59,63,66));
            board.add(snakeBodyPart[0]);
                                                                                // Set location of the body part of the snake.
            x = x - Body;
        for (int i = 1; i < totalBodyPart; i++) {
            snakeBodyPart[i] = new JButton(ghost);  
            snakeBodyPart[i].setBorder(lineBorder);
            snakeBodyPart[i].setBounds(x, y, Body, Body);
            snakeBodyPart[i].setBackground(new Color(59,63,66));
            board.add(snakeBodyPart[i]);
                                                                                // Set location of the next body part of the snake.
            x = x - Body;
        }

        createFood();                                                           // Create food.
    }

   
    void createFood() {                                                     
        int randomX = Body + (Body * random.nextInt(30));                       //generating the random x and y value of the food
        int randomY = Body + (Body * random.nextInt(20));                       //the numbers 30 and 20 are used due to the size of the game board

        snakeBodyPart[totalBodyPart] = new JButton(ghost);                      //sets the image of the button to ghost
        snakeBodyPart[totalBodyPart].setBorder(lineBorder);                     //changes the border to no border
        snakeBodyPart[totalBodyPart].setBounds(randomX, randomY, Body, Body);   //sets the bounds of the button
        board.add(snakeBodyPart[totalBodyPart]);                                //adds the button the board

        totalBodyPart++;                                                        //counts the number of snake body parts
    }
    
//    public void sound(String path){
//
//    try{
//        AudioInputStream audio = AudioSystem.getAudioInputStream(Menu.class.getResource(path));
//        Clip clip = AudioSystem.getClip();
//        clip.open(audio);
//        clip.start();
//    } catch (Exception e){
//        System.out.println("check "+path+"\n");
//        e.printStackTrace();
//    }
//}

    /**
     * Process next step of the snake.
     * And decide what should be done.
     */
    void processNextStep() {
        boolean isBorderTouched = false;
                                                                                    //Generates new location of snake head, and sets it for xDirecction and yDirection
        int newHeadLocX = (int) snakeBodyPart[0].getLocation().getX() + directionX; 
        int newHeadLocY = (int) snakeBodyPart[0].getLocation().getY() + directionY;

                                                                                //Most last part of the snake is food, so that is why it is the counter minus 1.
        int foodLocX = (int) snakeBodyPart[totalBodyPart - 1].getLocation().getX();
        int foodLocY = (int) snakeBodyPart[totalBodyPart - 1].getLocation().getY();

                                                                                //Check does snake cross the border of the board?
        if (newHeadLocX >= BoardWidth) {                                        //touch the right border, it then sets the isBorderTouched to true,
                                                                                //and does that for the bottom as it means the snake has hit the border.
            newHeadLocX = 0;
            isBorderTouched = true;
        } else if (newHeadLocX +Body<= 0) {                                     //touch the left border
            newHeadLocX = BoardWidth - Body;
            isBorderTouched = true;
        } else if (newHeadLocY >= BoardHight) {                                 //touch the bottom boarder
            newHeadLocY = 0;
            isBorderTouched = true;
        } else if (newHeadLocY + Body<= 0) {                                    //touch the top boarder
            newHeadLocY = BoardHight - Body;
            isBorderTouched = true;
        }

                                                                                
        if (newHeadLocX == foodLocX && newHeadLocY == foodLocY) {               //Check has snake touched the food or not
                                                                                //Set score.
            score += 100;
            scoreViewer.setText("Current Score: " + score);
            
            createFood();                                                       //Create new food.
        }

        
        
       
        if(isGameOver(isBorderTouched, newHeadLocX, newHeadLocY)) {             //checks to see if the game is over
           scoreViewer.setText("GAME OVER	" + score);
           
           
           //if the current score is greater than the current highscore for the user, then set the current score as the high score
            try {
                if(arcadeGUI.getScore() < score){
                    arcadeGUI.setScore(score);
                }
            } catch (IOException ex) {
                Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
            }
           
           
           isRunning = false;
          
           return;
        } else {
            moveSnakeForword(newHeadLocX, newHeadLocY);                         //keep moving the snake using the moveforward method
        }

        board.repaint();                                                        //repaints the entire board, keep the process going
    }

    
    private boolean isGameOver(boolean isBorderTouched, int headLocX, int headLocY) {
        switch(selectedGameType) {
            case BORDER:                                                        //when there is border, gameover when the snake touchs the border
                if(isBorderTouched) {   
                    return true;
                }
            default:
                break;
        }
        
        for (int i = Length; i < totalBodyPart - 2; i++) {
            Point partLoc = snakeBodyPart[i].getLocation();
            if (partLoc.equals(new Point(headLocX, headLocY))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Every body part should be placed to location of the front part.
     * For example if part:0(100,150) , part: 1(90, 150), part:2(80,150) and new head location (110,150) then,
       Location of part:2 should be (80,150) to (90,150), part:1 will be (90,150) to (100,150) and part:3 will be (100,150) to (110,150)
     * This movement process should be start from the last part to first part.
     * (totalBodyPart - 1) means food and (totalBodyPart - 2) means tail.
     * @param headLocX
     * @param headLocY
     */
    public void moveSnakeForword(int headLocX, int headLocY) {
//        if(run){
        for (int i = totalBodyPart - 2; i > 0; i--) {
            Point frontBodyPartPoint = snakeBodyPart[i - 1].getLocation();      //get the location of this body part
            snakeBodyPart[i].setLocation(frontBodyPartPoint);                   //move it forward
        }
        snakeBodyPart[0].setBounds(headLocX, headLocY, Body, Body);             //moving the head
    }

    public void snakeKeyPressed(KeyEvent e) {
                                                                                //snake should move to left when player pressed left arrow
        if (Left == true && e.getKeyCode() == 37) {
            directionX = -Body;                                                 //means snake move right to left by 25 pixel
            directionY = 0;
            Right = false;                                                      //means snake cant move from left to right
            Up = true;                                                          //means snake can move from down to up
            Down = true;                                                        //means snake can move from up to down
            snakeBodyPart[0].setIcon(faceLeft);
            
        }
                                                                                //snake should move to up when player pressed up arrow
        if (Up == true && e.getKeyCode() == 38) {
            directionX = 0;
            directionY = -Body;                                                 //means snake move from down to up by 25 pixel
            Down = false;                                                       //means snake can move from up to down
            Right = true;                                                       //means snake can move from left to right
            Left = true;                                                        //means snake can move from right to left
            snakeBodyPart[0].setIcon(faceUp); 
        }
                                                                                //snake should move to right when player pressed right arrow
        if (Right == true && e.getKeyCode() == 39) {
            directionX = +Body;                                                 //means snake move from left to right by 25 pixel
            directionY = 0;
            Left = false;
            Up = true;
            Down = true;
            snakeBodyPart[0].setIcon(faceRight);
        }
                                                                                //snake should move to down when player pressed down arrow
        if (Down == true && e.getKeyCode() == 40) {
            directionX = 0;
            directionY = +Body;                                                 //means snake move from left to right by 25 pixel
            Up = false;
            Right = true;
            Left = true;
            snakeBodyPart[0].setIcon(faceDown);
        }
    }

    private void createThread() {
                                                                                //start thread
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(isRunning){
                    runIt();
                }
            }
        });
        thread.start();                                                         //go to runIt() method
    }

    public void runIt() {
        while (true) {
            if(isRunning) {                                                         //Process what should be next step of the snake.
                processNextStep();
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}
