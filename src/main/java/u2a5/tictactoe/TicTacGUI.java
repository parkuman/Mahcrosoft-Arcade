/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package u2a5.tictactoe;

import mainMenu.arcadeGUI;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Parker
 */
public class TicTacGUI extends javax.swing.JFrame {

    //creates a sting 'mode', this string stores the mode the user is currently playing (either "3x3" or "4x4")
    String mode;

    //int xWins, oWins, and ties are all integers that keep track of how many times each 'person' wins, this is for the scoreboard
    int xWins = 0;
    int oWins = 0;
    int ties = 0;

    //new 2D integer array for the 3x3 grid gamemode, set to 3 columns 3 rows
    int[][] x3check = new int[3][3];
    //new 2D integer array for the 4x4 grid gamemode, set to 4 columns 4 rows
    int[][] x4check = new int[4][4];

    //string 'turn' that keeps track of the current player's turn. It is initialized as x because in tic tac toe x always goes first
    String turn = "x";

    //integers xWin and oWin is an integer that is usesd to check if x or o wins. Think of it as a boolean where 0 is false and 1 is true
    int xWin = 0;
    int oWin = 0;

    /**
     * creates new imageIcons for the X's and O's of each varying grid sizes.
     * the icons for the 3x3 grid are 100x100 pixels the icons for the 4x4 grid
     * are 80x80 pixels to accomodate the smaller individual grid sizes
     */
    ImageIcon Xx3 = new ImageIcon(getClass().getResource("/tictactoeImages/Xx3.png"));
    ImageIcon Ox3 = new ImageIcon(getClass().getResource("/tictactoeImages/Ox3.png"));

    ImageIcon Xx4 = new ImageIcon(getClass().getResource("/tictactoeImages/Xx4.png"));
    ImageIcon Ox4 = new ImageIcon(getClass().getResource("/tictactoeImages/Ox4.png"));

    /**
     * integer clicks is used to keep track of how many clicks there have been
     * in a game, this is used to check if there has been the max amount of
     * turns
     *
     * in a 3x3 grid game there are maximum 9 clicks in a 4x4 grid game there
     * are maximum 16 clicks
     */
    int clicks = 0;

    /**
     * Creates new form TicTacGUI
     *
     * this code runs when the GUI window is opened
     */
    public TicTacGUI() {
        initComponents();

        //runs the 3x3Grid method as the default when the GUI opens
        grid3X3();
        //make sure to hide the gamePanel, so the menu shows up first
        gamePanel.setVisible(false);

        //set the text of the top label in the game screen to the current person's turn
        topLabel.setText(turn + "'s turn");

        //run the scoreboard method once the GUI opens to display the current amount of wins (it will be 0 for all)
        scoreboard();
    }

    /**
     * this method is run when the user presses the 3x3 grid button, it will let
     * the program know which game mode to run and disable the button so it
     * cannot be pressed again
     */
    //the method for the 3x3 gamemode
    public void grid3X3() {
        //disable the 3x3 option button and enable the 4x4 button option, this acts like a toggle (if one is on the other is off)
        threeXButton.setEnabled(false);
        fourXButton.setEnabled(true);

        //set the mode to "3x3"
        mode = "3x3";
    }

    //the method for the 3x3 gamemode
    public void grid4X4() {
        //disable the 4x4 option button and enable the 3x3 button option, this acts like a toggle (if one is on the other is off)
        fourXButton.setEnabled(false);
        threeXButton.setEnabled(true);

        //set the mode to "4x4"
        mode = "4x4";
    }

    //turn method that is run whenever the user makes a turn (clicks a spot on the grid)
    public void turn() {
        //if it is x's turn
        if (turn.equals("x")) {
            //set the turn to be o for the next one
            turn = "o";
        } else {
            //if the turn is not x (it must be o's turn), then set the turn to be x so the program knows for next time
            turn = "x";
        }
        //set the label at the top to indicate who's turn it is
        topLabel.setText(turn + "'s turn");
    }

    //scoreboard method is what displays the number of wins at the top of the game screen
    public void scoreboard() {
        //set the xWins, oWins, and "cWins" (cat's wins) to the amount of wins each corresponding player has
        xWinsField.setText(Integer.toString(xWins));
        oWinsField.setText(Integer.toString(oWins));
        cWinsField.setText(Integer.toString(ties));
    }

    //3x3 grid reset method
    public void x3reset() {

        /**
         * set background colour and text colour of the resetButton to black and
         * grey accordingly
         *
         * this is in place because if a game is finished the button will turn
         * white to attract attention to it, so when you press it it should go
         * back to normal
         */
        resetButton.setBackground(Color.BLACK);
        resetButton.setForeground(Color.GRAY);

        //reset the current turn back to x
        turn = "x";

        //reset the top text to indicate the current turn
        topLabel.setText(turn + "'s turn");

        //reset clicks, xWin, and oWin back to 0
        clicks = 0;
        xWin = 0;
        oWin = 0;

        //nested for loop that sets all values in the 3x3 2D array back to 0
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                x3check[r][c] = 0;
            }
        }

        //reset all icons to null (nothing) and re-enable all of them
        A1x3.setIcon(null);
        A2x3.setIcon(null);
        A3x3.setIcon(null);
        B1x3.setIcon(null);
        B2x3.setIcon(null);
        B3x3.setIcon(null);
        C1x3.setIcon(null);
        C2x3.setIcon(null);
        C3x3.setIcon(null);

        A1x3.setEnabled(true);
        A2x3.setEnabled(true);
        A3x3.setEnabled(true);
        B1x3.setEnabled(true);
        B2x3.setEnabled(true);
        B3x3.setEnabled(true);
        C1x3.setEnabled(true);
        C2x3.setEnabled(true);
        C3x3.setEnabled(true);
    }

    //x4reset method is the same as the x3reset method except it resets all the x4 buttons
    public void x4reset() {
        resetButton.setBackground(Color.BLACK);
        resetButton.setForeground(Color.GRAY);

        turn = "x";
        topLabel.setText(turn + "'s turn");
        clicks = 0;
        xWin = 0;
        oWin = 0;

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                x4check[r][c] = 0;
            }
        }

        A1x4.setIcon(null);
        A2x4.setIcon(null);
        A3x4.setIcon(null);
        A4x4.setIcon(null);
        B1x4.setIcon(null);
        B2x4.setIcon(null);
        B3x4.setIcon(null);
        B4x4.setIcon(null);
        C1x4.setIcon(null);
        C2x4.setIcon(null);
        C3x4.setIcon(null);
        C4x4.setIcon(null);
        D1x4.setIcon(null);
        D2x4.setIcon(null);
        D3x4.setIcon(null);
        D4x4.setIcon(null);

        A1x4.setEnabled(true);
        A2x4.setEnabled(true);
        A3x4.setEnabled(true);
        A4x4.setEnabled(true);
        B1x4.setEnabled(true);
        B2x4.setEnabled(true);
        B3x4.setEnabled(true);
        B4x4.setEnabled(true);
        C1x4.setEnabled(true);
        C2x4.setEnabled(true);
        C3x4.setEnabled(true);
        C4x4.setEnabled(true);
        D1x4.setEnabled(true);
        D2x4.setEnabled(true);
        D3x4.setEnabled(true);
        D4x4.setEnabled(true);
    }

    //x3disableButtons method disables all the buttons in the 3x3 grid
    public void x3disableButtons() {
        A1x3.setEnabled(false);
        A2x3.setEnabled(false);
        A3x3.setEnabled(false);
        B1x3.setEnabled(false);
        B2x3.setEnabled(false);
        B3x3.setEnabled(false);
        C1x3.setEnabled(false);
        C2x3.setEnabled(false);
        C3x3.setEnabled(false);
    }

    //x4disableButtons method disables all the buttons in the 4x4 grid
    public void x4disableButtons() {
        A1x4.setEnabled(false);
        A2x4.setEnabled(false);
        A3x4.setEnabled(false);
        A4x4.setEnabled(false);
        B1x4.setEnabled(false);
        B2x4.setEnabled(false);
        B3x4.setEnabled(false);
        B4x4.setEnabled(false);
        C1x4.setEnabled(false);
        C2x4.setEnabled(false);
        C3x4.setEnabled(false);
        C4x4.setEnabled(false);
        D1x4.setEnabled(false);
        D2x4.setEnabled(false);
        D3x4.setEnabled(false);
        D4x4.setEnabled(false);
    }

    /**
     * x3winner method is a method taken from the example code, it will run at
     * the end of every turn,
     *
     * this method checks for win conditions within the 3x3 grid by comparing
     * values in the x3check 2d array
     */
    public void x3winner() {
        /**
         * check rows for winner this loop runs three times, once for each row
         * in the 3x3 grid
         */
        for (int x = 0; x <= 2; x++) {
            //checks if all three values in a row are equal
            if ((x3check[x][0] == x3check[x][1]) && (x3check[x][0] == x3check[x][2])) {
                //if they're all equal to 1 (1 is the value set in the array for an x turn), then set xWin to 1 (in other words: true)
                if (x3check[x][0] == 1) {
                    xWin = 1;

                    //if they're all equal to 2 (2 is the value set in the array for an o turn), then set oWin to 1 (in other words: true)
                } else if (x3check[x][0] == 2) {
                    oWin = 1;
                }
            }
        }

        /**
         * Check columns for winner this loop runs three times, once for each
         * column in the 3x3 grid
         */
        for (int x = 0; x <= 2; x++) {
            //checks if all three values in a column are equal
            if ((x3check[0][x] == x3check[1][x]) && (x3check[0][x] == x3check[2][x])) {
                //if they're all equal to 1 (1 is the value set in the array for an x turn), then set xWin to 1 (in other words: true)
                if (x3check[0][x] == 1) {
                    xWin = 1;

                    //if they're all equal to 2 (2 is the value set in the array for an o turn), then set oWin to 1 (in other words: true)
                } else if (x3check[0][x] == 2) {
                    oWin = 1;
                }
            }
        }

        /**
         * Check diagonals for winner checks if the [0][0],[1][1], and [2][2]
         * spots in the array are equal (top left to bottom right diagonal) OR
         * it checks if the [2][0],[1][1], and [0][2] spots in the array are
         * equal (top right to bottom left diagonal)
         *
         * if either of those are met, someone has won and move on to the next
         * checks
         */
        if (((x3check[0][0] == x3check[1][1]) && (x3check[0][0] == x3check[2][2])) || ((x3check[2][0] == x3check[1][1]) && (x3check[1][1] == x3check[0][2]))) {
            /**
             * if [1][1] (the middle cell in the array) is equal to 1 (1 is the
             * value set in the array for an x turn), then set xWin to 1 (in
             * other words: true)
             *
             * this check works because [1][1] is a cell that is present in both
             * diagonal win conditions so it can be the sole check condition
             * this will change in the 4x4 diagonal win condition check because
             * the diagonals never share a cell
             */
            if (x3check[1][1] == 1) {
                xWin = 1;

                /**
                 * if [1][1] (the middle cell in the array) is equal to 2 (2 is
                 * the value set in the array for an o turn), then set oWin to 1
                 * (in other words: true)
                 *
                 * this check works because [1][1] is a cell that is present in
                 * both diagonal win conditions so it can be the sole check
                 * condition this will change in the 4x4 diagonal win condition
                 * check because the diagonals never share a cell
                 */
            } else if (x3check[1][1] == 2) {
                oWin = 1;
            }

        }

        //check to see if any of the xWin or oWin is 1 (ie true)
        if ((xWin == 1) || (oWin == 1)) {
            //if there is a winner, disable all the buttons so no more are pressed
            x3disableButtons();

            //then check: if x won, make a dialog box saying x is the winner, and add one to the xWins counter
            if (xWin == 1) {
                JOptionPane.showMessageDialog(null, "X is the winner");
                xWins++;
                try {
                    arcadeGUI.setScore(arcadeGUI.getScore() + 1);
                } catch (IOException ex) {
                    Logger.getLogger(TicTacGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                //if x didn't win, then o must have: make a dialog box saying o is the winner, and add one to the oWins counter
            } else {
                JOptionPane.showMessageDialog(null, "O is the winner");
                oWins++;
            }
            //set the reset button to have a white background and black text so it stands out to the user (like a "click me!" kind of thing)
            resetButton.setBackground(Color.WHITE);
            resetButton.setForeground(Color.BLACK);
        }

        /**
         * Checks if the game is a tie, if the max amount of clicks is reached,
         * and xWin and oWin are 0 (ie false)
         */
        if ((clicks == 9) && xWin == 0 && oWin == 0) {
            //disable the buttons so you cannot press them anymore
            x3disableButtons();

            //let the user know the game was a tie
            JOptionPane.showMessageDialog(null, "The game is a tie");

            //add one to the ties counter, since the game is a tie
            ties++;

            //set the reset button to have a white background and black text so it stands out to the user (like a "click me!" kind of thing)
            resetButton.setBackground(Color.WHITE);
            resetButton.setForeground(Color.BLACK);
        }

        //run the scoreboard method to update it, in case there was a win and the counters need to be updated
        scoreboard();
    }

    /**
     * x4winner method is a method taken from the example code, with a few
     * changes. it will run at the end of every turn,
     *
     * this method checks for win conditions within the 4x4 grid by comparing
     * values in the x4check 2d array
     *
     * the code for the rows & columns check is the same as the 3x3 grid but
     * with one more row and column, please see x3winner method for annotations
     * this is different, however, for the diagonal check so the annotation is
     * different
     */
    public void x4winner() {
        /**
         * check rows for winner
         */
        for (int x = 0; x <= 3; x++) {
            if ((x4check[x][0] == x4check[x][1]) && (x4check[x][0] == x4check[x][2]) && (x4check[x][0] == x4check[x][3])) {
                if (x4check[x][0] == 1) {
                    xWin = 1;

                } else if (x4check[x][0] == 2) {
                    oWin = 1;

                }
            }
        }

        /**
         * Check columns for winner
         */
        for (int x = 0; x <= 3; x++) {
            if ((x4check[0][x] == x4check[1][x]) && (x4check[0][x] == x4check[2][x]) && (x4check[0][x] == x4check[3][x])) {
                if (x4check[0][x] == 1) {
                    xWin = 1;
                } else if (x4check[0][x] == 2) {
                    oWin = 1;
                }
            }
        }

        /**
         * instead of just one diagonal win condition check, there needs to be
         * 2, since the potential diagonal win conditions do not intersect in a
         * mutual cell like in the 3x3 grid, we need separate checks
         */
        /**
         * 1. Check top left to bottom right diagonal for winner
         *
         * if [0][0], [1][1], [2][2], and [3][3] are all equal, then check for a
         * winner
         */
        if ((x4check[0][0] == x4check[1][1]) && (x4check[0][0] == x4check[2][2]) && (x4check[0][0] == x4check[3][3])) {
            //if [0][0] (and therefore all the rest since they are equal), is 1, then set xWin = 1 (ie true)
            if (x4check[0][0] == 1) {
                xWin = 1;

                //if [0][0] (and therefore all the rest since they are equal), is 2, then set oWin = 1 (ie true)
            } else if (x4check[0][0] == 2) {
                oWin = 1;
            }
        }

        /**
         * 2. Check top right to bottom left diagonal for winner
         *
         * if [3][0], [2][1], [1][2], and [0][3] are all equal, then check for a
         * winner
         */
        if ((x4check[3][0] == x4check[2][1]) && (x4check[2][1] == x4check[1][2]) && (x4check[1][2] == x4check[0][3])) {
            //if [3][0] (and therefore all the rest since they are equal), is 1, then set xWin = 1 (ie true)
            if (x4check[3][0] == 1) {
                xWin = 1;

                //if [3][0] (and therefore all the rest since they are equal), is 2, then set oWin = 1 (ie true)
            } else if (x4check[3][0] == 2) {
                oWin = 1;
            }
        }

        //same as x3winner
        if ((xWin == 1) || (oWin == 1)) {
            x4disableButtons();
            if (xWin == 1) {
                //sets the new score to be the original incremented by 1
                try {
                    arcadeGUI.setScore(arcadeGUI.getScore() + 1);
                } catch (IOException ex) {
                    Logger.getLogger(TicTacGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "X is the winner");
                xWins++;
            } else {
                JOptionPane.showMessageDialog(null, "O is the winner");
                oWins++;
            }
            resetButton.setBackground(Color.WHITE);
            resetButton.setForeground(Color.BLACK);
        }

        //same as x3winner
        /**
         * Checks if the game is a tie
         */
        if ((clicks == 16) && xWin == 0 && oWin == 0) {
            x4disableButtons();
            JOptionPane.showMessageDialog(null, "The game is a tie");
            ties++;
            resetButton.setBackground(Color.WHITE);
            resetButton.setForeground(Color.BLACK);
        }

        scoreboard();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layeredPane = new javax.swing.JLayeredPane();
        menuPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        gridSizePanel = new javax.swing.JPanel();
        gridSizeLabel = new javax.swing.JLabel();
        threeXButton = new javax.swing.JButton();
        fourXButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        gamePanel = new javax.swing.JPanel();
        scoresPanel = new javax.swing.JPanel();
        cWinsField = new javax.swing.JTextField();
        cWinsLabel = new javax.swing.JLabel();
        xWinsField = new javax.swing.JTextField();
        xWinsLabel = new javax.swing.JLabel();
        oWinsField = new javax.swing.JTextField();
        oWinsLabel = new javax.swing.JLabel();
        topLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        resetScoreButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        layeredPaneGrids = new javax.swing.JLayeredPane();
        threeXgamePanel = new javax.swing.JPanel();
        grid3x3 = new javax.swing.JLayeredPane();
        A1x3 = new javax.swing.JButton();
        A2x3 = new javax.swing.JButton();
        A3x3 = new javax.swing.JButton();
        B1x3 = new javax.swing.JButton();
        B2x3 = new javax.swing.JButton();
        B3x3 = new javax.swing.JButton();
        C1x3 = new javax.swing.JButton();
        C2x3 = new javax.swing.JButton();
        C3x3 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        fourXgamePanel = new javax.swing.JPanel();
        grid4x4 = new javax.swing.JLayeredPane();
        A1x4 = new javax.swing.JButton();
        A2x4 = new javax.swing.JButton();
        A3x4 = new javax.swing.JButton();
        A4x4 = new javax.swing.JButton();
        B1x4 = new javax.swing.JButton();
        B2x4 = new javax.swing.JButton();
        B3x4 = new javax.swing.JButton();
        B4x4 = new javax.swing.JButton();
        C1x4 = new javax.swing.JButton();
        C2x4 = new javax.swing.JButton();
        C3x4 = new javax.swing.JButton();
        C4x4 = new javax.swing.JButton();
        D1x4 = new javax.swing.JButton();
        D2x4 = new javax.swing.JButton();
        D3x4 = new javax.swing.JButton();
        D4x4 = new javax.swing.JButton();
        jSeparator22 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator23 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        jSeparator26 = new javax.swing.JSeparator();
        jSeparator27 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tic Tac Toe");

        menuPanel.setBackground(new java.awt.Color(0, 0, 0));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("DialogInput", 1, 70)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TIC TAC TOE");

        buttonsPanel.setBackground(new java.awt.Color(0, 0, 0));

        playButton.setBackground(new java.awt.Color(0, 0, 0));
        playButton.setFont(new java.awt.Font("DialogInput", 1, 48)); // NOI18N
        playButton.setForeground(new java.awt.Color(255, 255, 255));
        playButton.setText("PLAY");
        playButton.setBorder(null);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        gridSizePanel.setBackground(new java.awt.Color(0, 0, 0));

        gridSizeLabel.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        gridSizeLabel.setForeground(new java.awt.Color(255, 255, 255));
        gridSizeLabel.setText("Select Grid Size");

        threeXButton.setBackground(new java.awt.Color(0, 0, 0));
        threeXButton.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        threeXButton.setForeground(new java.awt.Color(255, 255, 255));
        threeXButton.setText("3x3");
        threeXButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        threeXButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threeXButtonActionPerformed(evt);
            }
        });

        fourXButton.setBackground(new java.awt.Color(0, 0, 0));
        fourXButton.setFont(new java.awt.Font("DialogInput", 1, 24)); // NOI18N
        fourXButton.setForeground(new java.awt.Color(255, 255, 255));
        fourXButton.setText("4x4");
        fourXButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        fourXButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fourXButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gridSizePanelLayout = new javax.swing.GroupLayout(gridSizePanel);
        gridSizePanel.setLayout(gridSizePanelLayout);
        gridSizePanelLayout.setHorizontalGroup(
            gridSizePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gridSizePanelLayout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .addComponent(gridSizeLabel)
                .addContainerGap(66, Short.MAX_VALUE))
            .addGroup(gridSizePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(threeXButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fourXButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gridSizePanelLayout.setVerticalGroup(
            gridSizePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gridSizePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gridSizeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(gridSizePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(threeXButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fourXButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        exitButton.setBackground(new java.awt.Color(0, 0, 0));
        exitButton.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        exitButton.setForeground(new java.awt.Color(255, 255, 255));
        exitButton.setText("Exit");
        exitButton.setBorder(null);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jSeparator3.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator3.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gridSizePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(playButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                        .addComponent(exitButton)
                        .addGap(148, 148, 148)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(playButton)
                .addGap(7, 7, 7)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gridSizePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(exitButton)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        gamePanel.setBackground(new java.awt.Color(0, 0, 0));

        scoresPanel.setBackground(new java.awt.Color(0, 0, 0));

        cWinsField.setEditable(false);
        cWinsField.setBackground(new java.awt.Color(0, 0, 0));
        cWinsField.setFont(new java.awt.Font("DialogInput", 1, 48)); // NOI18N
        cWinsField.setForeground(new java.awt.Color(255, 255, 255));

        cWinsLabel.setFont(new java.awt.Font("DialogInput", 1, 14)); // NOI18N
        cWinsLabel.setForeground(new java.awt.Color(255, 255, 255));
        cWinsLabel.setText("Cat's Games");

        xWinsField.setEditable(false);
        xWinsField.setBackground(new java.awt.Color(0, 0, 0));
        xWinsField.setFont(new java.awt.Font("DialogInput", 1, 48)); // NOI18N
        xWinsField.setForeground(new java.awt.Color(255, 255, 255));

        xWinsLabel.setFont(new java.awt.Font("DialogInput", 1, 14)); // NOI18N
        xWinsLabel.setForeground(new java.awt.Color(255, 255, 255));
        xWinsLabel.setText("X Wins");

        oWinsField.setEditable(false);
        oWinsField.setBackground(new java.awt.Color(0, 0, 0));
        oWinsField.setFont(new java.awt.Font("DialogInput", 1, 48)); // NOI18N
        oWinsField.setForeground(new java.awt.Color(255, 255, 255));

        oWinsLabel.setFont(new java.awt.Font("DialogInput", 1, 14)); // NOI18N
        oWinsLabel.setForeground(new java.awt.Color(255, 255, 255));
        oWinsLabel.setText("O Wins");

        javax.swing.GroupLayout scoresPanelLayout = new javax.swing.GroupLayout(scoresPanel);
        scoresPanel.setLayout(scoresPanelLayout);
        scoresPanelLayout.setHorizontalGroup(
            scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scoresPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xWinsField, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(xWinsLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(cWinsField, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cWinsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oWinsField, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(oWinsLabel)))
                .addGap(14, 14, 14))
        );
        scoresPanelLayout.setVerticalGroup(
            scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scoresPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(scoresPanelLayout.createSequentialGroup()
                        .addComponent(xWinsField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xWinsLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, scoresPanelLayout.createSequentialGroup()
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cWinsField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(oWinsField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(scoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cWinsLabel)
                            .addComponent(oWinsLabel))))
                .addContainerGap())
        );

        topLabel.setFont(new java.awt.Font("DialogInput", 1, 36)); // NOI18N
        topLabel.setForeground(new java.awt.Color(255, 255, 255));
        topLabel.setText("'s Turn");

        backButton.setBackground(new java.awt.Color(0, 0, 0));
        backButton.setFont(new java.awt.Font("DialogInput", 1, 14)); // NOI18N
        backButton.setForeground(new java.awt.Color(102, 102, 102));
        backButton.setText("Back");
        backButton.setBorder(null);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        resetScoreButton.setBackground(new java.awt.Color(0, 0, 0));
        resetScoreButton.setFont(new java.awt.Font("DialogInput", 1, 14)); // NOI18N
        resetScoreButton.setForeground(new java.awt.Color(102, 102, 102));
        resetScoreButton.setText("Reset Score");
        resetScoreButton.setBorder(null);
        resetScoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetScoreButtonActionPerformed(evt);
            }
        });

        resetButton.setBackground(new java.awt.Color(0, 0, 0));
        resetButton.setFont(new java.awt.Font("DialogInput", 1, 14)); // NOI18N
        resetButton.setForeground(new java.awt.Color(102, 102, 102));
        resetButton.setText("Reset Game");
        resetButton.setBorder(null);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        threeXgamePanel.setBackground(new java.awt.Color(0, 0, 0));
        threeXgamePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        A1x3.setBackground(new java.awt.Color(0, 0, 0));
        A1x3.setForeground(new java.awt.Color(0, 0, 0));
        A1x3.setBorder(null);
        A1x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                A1x3MouseClicked(evt);
            }
        });
        A1x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A1x3ActionPerformed(evt);
            }
        });

        A2x3.setBackground(new java.awt.Color(0, 0, 0));
        A2x3.setBorder(null);
        A2x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                A2x3MouseClicked(evt);
            }
        });
        A2x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A2x3ActionPerformed(evt);
            }
        });

        A3x3.setBackground(new java.awt.Color(0, 0, 0));
        A3x3.setBorder(null);
        A3x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                A3x3MouseClicked(evt);
            }
        });
        A3x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A3x3ActionPerformed(evt);
            }
        });

        B1x3.setBackground(new java.awt.Color(0, 0, 0));
        B1x3.setBorder(null);
        B1x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                B1x3MouseClicked(evt);
            }
        });
        B1x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B1x3ActionPerformed(evt);
            }
        });

        B2x3.setBackground(new java.awt.Color(0, 0, 0));
        B2x3.setBorder(null);
        B2x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                B2x3MouseClicked(evt);
            }
        });
        B2x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B2x3ActionPerformed(evt);
            }
        });

        B3x3.setBackground(new java.awt.Color(0, 0, 0));
        B3x3.setBorder(null);
        B3x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                B3x3MouseClicked(evt);
            }
        });
        B3x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B3x3ActionPerformed(evt);
            }
        });

        C1x3.setBackground(new java.awt.Color(0, 0, 0));
        C1x3.setBorder(null);
        C1x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                C1x3MouseClicked(evt);
            }
        });
        C1x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C1x3ActionPerformed(evt);
            }
        });

        C2x3.setBackground(new java.awt.Color(0, 0, 0));
        C2x3.setBorder(null);
        C2x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                C2x3MouseClicked(evt);
            }
        });
        C2x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C2x3ActionPerformed(evt);
            }
        });

        C3x3.setBackground(new java.awt.Color(0, 0, 0));
        C3x3.setBorder(null);
        C3x3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                C3x3MouseClicked(evt);
            }
        });
        C3x3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C3x3ActionPerformed(evt);
            }
        });

        jSeparator6.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator6.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator7.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator7.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator7.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator8.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator8.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator8.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator9.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator9.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator9.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator10.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator10.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator10.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator11.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator11.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator11.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator12.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator12.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator12.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator5.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator5.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        grid3x3.setLayer(A1x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(A2x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(A3x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(B1x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(B2x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(B3x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(C1x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(C2x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(C3x3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator10, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator11, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator12, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid3x3.setLayer(jSeparator5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout grid3x3Layout = new javax.swing.GroupLayout(grid3x3);
        grid3x3.setLayout(grid3x3Layout);
        grid3x3Layout.setHorizontalGroup(
            grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grid3x3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(A2x3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(A3x3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(A1x3, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jSeparator7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(B1x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator8)
                    .addComponent(B2x3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(B3x3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(grid3x3Layout.createSequentialGroup()
                        .addComponent(C1x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(C2x3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator12)
                    .addComponent(C3x3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        grid3x3Layout.setVerticalGroup(
            grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grid3x3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator6)
                    .addComponent(jSeparator5)
                    .addGroup(grid3x3Layout.createSequentialGroup()
                        .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(C1x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(B1x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(A1x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(A2x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(B2x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(C2x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(grid3x3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(A3x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(B3x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(C3x3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout threeXgamePanelLayout = new javax.swing.GroupLayout(threeXgamePanel);
        threeXgamePanel.setLayout(threeXgamePanelLayout);
        threeXgamePanelLayout.setHorizontalGroup(
            threeXgamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, threeXgamePanelLayout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addComponent(grid3x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        threeXgamePanelLayout.setVerticalGroup(
            threeXgamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(threeXgamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grid3x3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        fourXgamePanel.setBackground(new java.awt.Color(0, 0, 0));
        fourXgamePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        A1x4.setBackground(new java.awt.Color(0, 0, 0));
        A1x4.setForeground(new java.awt.Color(51, 51, 51));
        A1x4.setBorder(null);
        A1x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A1x4ActionPerformed(evt);
            }
        });

        A2x4.setBackground(new java.awt.Color(0, 0, 0));
        A2x4.setForeground(new java.awt.Color(51, 51, 51));
        A2x4.setBorder(null);
        A2x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A2x4ActionPerformed(evt);
            }
        });

        A3x4.setBackground(new java.awt.Color(0, 0, 0));
        A3x4.setForeground(new java.awt.Color(51, 51, 51));
        A3x4.setBorder(null);
        A3x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A3x4ActionPerformed(evt);
            }
        });

        A4x4.setBackground(new java.awt.Color(0, 0, 0));
        A4x4.setForeground(new java.awt.Color(51, 51, 51));
        A4x4.setBorder(null);
        A4x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                A4x4ActionPerformed(evt);
            }
        });

        B1x4.setBackground(new java.awt.Color(0, 0, 0));
        B1x4.setForeground(new java.awt.Color(51, 51, 51));
        B1x4.setBorder(null);
        B1x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B1x4ActionPerformed(evt);
            }
        });

        B2x4.setBackground(new java.awt.Color(0, 0, 0));
        B2x4.setForeground(new java.awt.Color(51, 51, 51));
        B2x4.setBorder(null);
        B2x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B2x4ActionPerformed(evt);
            }
        });

        B3x4.setBackground(new java.awt.Color(0, 0, 0));
        B3x4.setForeground(new java.awt.Color(51, 51, 51));
        B3x4.setBorder(null);
        B3x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B3x4ActionPerformed(evt);
            }
        });

        B4x4.setBackground(new java.awt.Color(0, 0, 0));
        B4x4.setForeground(new java.awt.Color(51, 51, 51));
        B4x4.setBorder(null);
        B4x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B4x4ActionPerformed(evt);
            }
        });

        C1x4.setBackground(new java.awt.Color(0, 0, 0));
        C1x4.setForeground(new java.awt.Color(51, 51, 51));
        C1x4.setBorder(null);
        C1x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C1x4ActionPerformed(evt);
            }
        });

        C2x4.setBackground(new java.awt.Color(0, 0, 0));
        C2x4.setForeground(new java.awt.Color(51, 51, 51));
        C2x4.setBorder(null);
        C2x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C2x4ActionPerformed(evt);
            }
        });

        C3x4.setBackground(new java.awt.Color(0, 0, 0));
        C3x4.setForeground(new java.awt.Color(51, 51, 51));
        C3x4.setBorder(null);
        C3x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C3x4ActionPerformed(evt);
            }
        });

        C4x4.setBackground(new java.awt.Color(0, 0, 0));
        C4x4.setForeground(new java.awt.Color(51, 51, 51));
        C4x4.setBorder(null);
        C4x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C4x4ActionPerformed(evt);
            }
        });

        D1x4.setBackground(new java.awt.Color(0, 0, 0));
        D1x4.setForeground(new java.awt.Color(51, 51, 51));
        D1x4.setBorder(null);
        D1x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D1x4ActionPerformed(evt);
            }
        });

        D2x4.setBackground(new java.awt.Color(0, 0, 0));
        D2x4.setForeground(new java.awt.Color(51, 51, 51));
        D2x4.setBorder(null);
        D2x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D2x4ActionPerformed(evt);
            }
        });

        D3x4.setBackground(new java.awt.Color(0, 0, 0));
        D3x4.setForeground(new java.awt.Color(51, 51, 51));
        D3x4.setBorder(null);
        D3x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D3x4ActionPerformed(evt);
            }
        });

        D4x4.setBackground(new java.awt.Color(0, 0, 0));
        D4x4.setForeground(new java.awt.Color(51, 51, 51));
        D4x4.setBorder(null);
        D4x4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D4x4ActionPerformed(evt);
            }
        });

        jSeparator22.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator22.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator22.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator22.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator13.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator13.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator13.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator14.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator14.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator14.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator16.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator16.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator16.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator15.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator15.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator15.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator24.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator24.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator24.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator24.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator23.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator23.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator23.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator23.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator17.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator17.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator17.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator18.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator18.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator18.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator19.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator19.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator19.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator20.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator20.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator20.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator21.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator21.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator21.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator25.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator25.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator25.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator26.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator26.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator26.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        jSeparator27.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator27.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator27.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N

        grid4x4.setLayer(A1x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(A2x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(A3x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(A4x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(B1x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(B2x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(B3x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(B4x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(C1x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(C2x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(C3x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(C4x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(D1x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(D2x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(D3x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(D4x4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator22, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator13, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator14, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator16, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator15, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator24, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator23, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator17, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator18, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator19, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator20, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator21, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator25, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator26, javax.swing.JLayeredPane.DEFAULT_LAYER);
        grid4x4.setLayer(jSeparator27, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout grid4x4Layout = new javax.swing.GroupLayout(grid4x4);
        grid4x4.setLayout(grid4x4Layout);
        grid4x4Layout.setHorizontalGroup(
            grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grid4x4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(A1x4, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jSeparator13)
                    .addComponent(A2x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(A3x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(A4x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(B1x4, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jSeparator14)
                    .addComponent(B2x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator18, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(B3x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(B4x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(C1x4, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jSeparator15)
                    .addComponent(C2x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator19, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(C3x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator26, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(C4x4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(D2x4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator20)
                    .addComponent(D3x4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator27, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(D4x4, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(D1x4, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jSeparator16, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        grid4x4Layout.setVerticalGroup(
            grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grid4x4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator24)
                    .addComponent(jSeparator23)
                    .addComponent(jSeparator22)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(grid4x4Layout.createSequentialGroup()
                            .addComponent(A1x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(A2x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(A3x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(A4x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(grid4x4Layout.createSequentialGroup()
                            .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(D1x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(C1x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(B1x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(B2x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(C2x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(D2x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(B3x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(C3x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(D3x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(grid4x4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(C4x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(D4x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(B4x4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout fourXgamePanelLayout = new javax.swing.GroupLayout(fourXgamePanel);
        fourXgamePanel.setLayout(fourXgamePanelLayout);
        fourXgamePanelLayout.setHorizontalGroup(
            fourXgamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fourXgamePanelLayout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(grid4x4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );
        fourXgamePanelLayout.setVerticalGroup(
            fourXgamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fourXgamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(grid4x4)
                .addContainerGap())
        );

        layeredPaneGrids.setLayer(threeXgamePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredPaneGrids.setLayer(fourXgamePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneGridsLayout = new javax.swing.GroupLayout(layeredPaneGrids);
        layeredPaneGrids.setLayout(layeredPaneGridsLayout);
        layeredPaneGridsLayout.setHorizontalGroup(
            layeredPaneGridsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
            .addGroup(layeredPaneGridsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(fourXgamePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layeredPaneGridsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(threeXgamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layeredPaneGridsLayout.setVerticalGroup(
            layeredPaneGridsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
            .addGroup(layeredPaneGridsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(fourXgamePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layeredPaneGridsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(threeXgamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                        .addComponent(topLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resetButton)
                            .addComponent(resetScoreButton)))
                    .addComponent(scoresPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(gamePanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(layeredPaneGrids)
                    .addContainerGap()))
        );
        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton)
                    .addComponent(topLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addComponent(resetButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetScoreButton)))
                .addGap(4, 4, 4)
                .addComponent(scoresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePanelLayout.createSequentialGroup()
                    .addContainerGap(154, Short.MAX_VALUE)
                    .addComponent(layeredPaneGrids, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        layeredPane.setLayer(menuPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        layeredPane.setLayer(gamePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layeredPaneLayout = new javax.swing.GroupLayout(layeredPane);
        layeredPane.setLayout(layeredPaneLayout);
        layeredPaneLayout.setHorizontalGroup(
            layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(gamePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layeredPaneLayout.setVerticalGroup(
            layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(gamePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        //once the play button is pressed, reset the game boards in case there was a previous game going on
        x3reset();
        x4reset();

        //set the menuPanel to be invisible and the gamePanel to be visible, gamePanel is where the gameplay takes place
        menuPanel.setVisible(false);
        gamePanel.setVisible(true);

        //if the mode is 3x3, make the 3x3 gamePanel visible and 4x4 game panel invisible
        if (mode.equals("3x3")) {
            threeXgamePanel.setVisible(true);
            fourXgamePanel.setVisible(false);
        } //if the mode is not 3x3 (therefore 4x4) make the 4x4 gamePanel visible and 3x3 game panel invisible
        else {
            fourXgamePanel.setVisible(true);
            threeXgamePanel.setVisible(false);
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void fourXButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fourXButtonActionPerformed
        //if the fourXbutton is pressed, run the grid4x4 method, this sets the game mode to 4x4
        grid4X4();
    }//GEN-LAST:event_fourXButtonActionPerformed

    private void threeXButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threeXButtonActionPerformed
        //if the threeXbutton is pressed, run the grid3x3 method, this sets the game mode to 3x3
        grid3X3();
    }//GEN-LAST:event_threeXButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed

        //exits the program by only closing the window, without closing the original arcade
        this.dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        //if the usre presses the back button make the game panel invisible and the menu visible again
        menuPanel.setVisible(true);
        gamePanel.setVisible(false);
    }//GEN-LAST:event_backButtonActionPerformed

    private void A1x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_A1x3MouseClicked

    }//GEN-LAST:event_A1x3MouseClicked

    private void B1x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_B1x3MouseClicked

    }//GEN-LAST:event_B1x3MouseClicked

    private void C1x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_C1x3MouseClicked

    }//GEN-LAST:event_C1x3MouseClicked

    private void A2x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_A2x3MouseClicked

    }//GEN-LAST:event_A2x3MouseClicked

    private void B2x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_B2x3MouseClicked

    }//GEN-LAST:event_B2x3MouseClicked

    private void C2x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_C2x3MouseClicked

    }//GEN-LAST:event_C2x3MouseClicked

    private void A3x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_A3x3MouseClicked

    }//GEN-LAST:event_A3x3MouseClicked

    private void B3x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_B3x3MouseClicked

    }//GEN-LAST:event_B3x3MouseClicked

    private void C3x3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_C3x3MouseClicked

    }//GEN-LAST:event_C3x3MouseClicked

    //all buttons of the 3x3 grid function the same exact way, the only difference is the value that changes in the 2D array. this uses pretty much the same code as the example
    //if the user presses the A1 button on the 3x3 grid, run the following code
    private void A1x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A1x3ActionPerformed
        //increment the clicks counter by 1
        clicks++;

        //if the turn is x, run the following code
        if (turn.equals("x")) {
            //set the icon to the x ImageIcon for the 3x3 grid
            A1x3.setIcon(Xx3);
            //set the disabled icon to the same thing so that the button becomes disables, this is so they cannot press the same button twice
            A1x3.setDisabledIcon(Xx3);
            //set the [0][0] element in the array to 1, 1 is the identifier for x.
            x3check[0][0] = 1;
        } else {
            //set the icon to the o ImageIcon for the 3x3 grid
            A1x3.setIcon(Ox3);
            //set the disabled icon to the same thing so that the button becomes disables, this is so they cannot press the same button twice
            A1x3.setDisabledIcon(Ox3);
            //set the [0][0] element in the array to 2, 2 is the identifier for o.
            x3check[0][0] = 2;
        }
        //run the x3 winner method to check if there are any wins
        x3winner();

        //run the turn method to prepare for the next turn
        turn();

        //disable the current button so they cannot press it more than once
        A1x3.setEnabled(false);
    }//GEN-LAST:event_A1x3ActionPerformed

    //same as the first button but for the current one instead
    private void B1x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B1x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            B1x3.setIcon(Xx3);
            B1x3.setDisabledIcon(Xx3);
            x3check[1][0] = 1;
        } else {
            B1x3.setIcon(Ox3);
            B1x3.setDisabledIcon(Ox3);
            x3check[1][0] = 2;
        }
        x3winner();

        turn();
        B1x3.setEnabled(false);
    }//GEN-LAST:event_B1x3ActionPerformed

    //same as the first button but for the current one instead
    private void C1x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C1x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            C1x3.setIcon(Xx3);
            C1x3.setDisabledIcon(Xx3);
            x3check[2][0] = 1;
        } else {
            C1x3.setIcon(Ox3);
            C1x3.setDisabledIcon(Ox3);
            x3check[2][0] = 2;
        }
        x3winner();

        turn();
        C1x3.setEnabled(false);
    }//GEN-LAST:event_C1x3ActionPerformed

    //same as the first button but for the current one instead
    private void A2x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A2x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            A2x3.setIcon(Xx3);
            A2x3.setDisabledIcon(Xx3);
            x3check[0][1] = 1;
        } else {
            A2x3.setIcon(Ox3);
            A2x3.setDisabledIcon(Ox3);
            x3check[0][1] = 2;
        }
        x3winner();

        turn();
        A2x3.setEnabled(false);
    }//GEN-LAST:event_A2x3ActionPerformed

    //same as the first button but for the current one instead
    private void B2x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B2x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            B2x3.setIcon(Xx3);
            B2x3.setDisabledIcon(Xx3);
            x3check[1][1] = 1;
        } else {
            B2x3.setIcon(Ox3);
            B2x3.setDisabledIcon(Ox3);
            x3check[1][1] = 2;
        }
        x3winner();

        turn();
        B2x3.setEnabled(false);
    }//GEN-LAST:event_B2x3ActionPerformed

    //same as the first button but for the current one instead
    private void C2x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C2x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            C2x3.setIcon(Xx3);
            C2x3.setDisabledIcon(Xx3);
            x3check[2][1] = 1;
        } else {
            C2x3.setIcon(Ox3);
            C2x3.setDisabledIcon(Ox3);
            x3check[2][1] = 2;
        }
        x3winner();

        turn();
        C2x3.setEnabled(false);
    }//GEN-LAST:event_C2x3ActionPerformed

    //same as the first button but for the current one instead
    private void A3x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A3x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            A3x3.setIcon(Xx3);
            A3x3.setDisabledIcon(Xx3);
            x3check[0][2] = 1;
        } else {
            A3x3.setIcon(Ox3);
            A3x3.setDisabledIcon(Ox3);
            x3check[0][2] = 2;
        }
        x3winner();

        turn();
        A3x3.setEnabled(false);
    }//GEN-LAST:event_A3x3ActionPerformed

    //same as the first button but for the current one instead
    private void B3x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B3x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            B3x3.setIcon(Xx3);
            B3x3.setDisabledIcon(Xx3);
            x3check[1][2] = 1;
        } else {
            B3x3.setIcon(Ox3);
            B3x3.setDisabledIcon(Ox3);
            x3check[1][2] = 2;
        }
        x3winner();

        turn();
        B3x3.setEnabled(false);
    }//GEN-LAST:event_B3x3ActionPerformed

    //same as the first button but for the current one instead
    private void C3x3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C3x3ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            C3x3.setIcon(Xx3);
            C3x3.setDisabledIcon(Xx3);
            x3check[2][2] = 1;
        } else {
            C3x3.setIcon(Ox3);
            C3x3.setDisabledIcon(Ox3);
            x3check[2][2] = 2;
        }
        x3winner();

        turn();
        C3x3.setEnabled(false);
    }//GEN-LAST:event_C3x3ActionPerformed

    //this code runs when the resetButton is pressed
    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        //if the mode is 3x3 grid, run the 3x3 reset method
        if (mode.equals("3x3")) {
            x3reset();

            //if the mode is not 3x3 grid (ie 4x4 grid), run the 4x4 reset method
        } else {
            x4reset();
        }
    }//GEN-LAST:event_resetButtonActionPerformed

    //all buttons of the 4x4 grid function the same exact way, the only difference is the value that changes in the 2D array. this uses pretty much the same code as the example
    //if the user presses the A1 button on the 4x4 grid, run the following code
    private void A1x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A1x4ActionPerformed
        //increment the clicks counter by 1
        clicks++;

        //if the turn is x, run the following code
        if (turn.equals("x")) {
            //set the icon to the x ImageIcon for the 3x3 grid
            A1x4.setIcon(Xx4);
            //set the disabled icon to the same thing so that the button becomes disables, this is so they cannot press the same button twice
            A1x4.setDisabledIcon(Xx4);
            //set the [0][0] element in the 4x4 array to 1, 1 is the identifier for x.
            x4check[0][0] = 1;
        } else {
            //set the icon to the o ImageIcon for the 3x3 grid
            A1x4.setIcon(Ox4);
            //set the disabled icon to the same thing so that the button becomes disables, this is so they cannot press the same button twice
            A1x4.setDisabledIcon(Ox4);
            //set the [0][0] element in the 4x4 array to 2, 2 is the identifier for o.
            x4check[0][0] = 2;
        }
        //run the x4 winner method to check if there are any wins
        x4winner();

        //run the turn method to prepare for the next turn
        turn();
        //disable the current button so it cannot be pressed again
        A1x4.setEnabled(false);
    }//GEN-LAST:event_A1x4ActionPerformed

    //same as the first button but for the current one instead
    private void B1x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B1x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            B1x4.setIcon(Xx4);
            B1x4.setDisabledIcon(Xx4);
            x4check[1][0] = 1;
        } else {
            B1x4.setIcon(Ox4);
            B1x4.setDisabledIcon(Ox4);
            x4check[1][0] = 2;
        }
        x4winner();

        turn();
        B1x4.setEnabled(false);
    }//GEN-LAST:event_B1x4ActionPerformed

    //same as the first button but for the current one instead
    private void C1x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C1x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            C1x4.setIcon(Xx4);
            C1x4.setDisabledIcon(Xx4);
            x4check[2][0] = 1;
        } else {
            C1x4.setIcon(Ox4);
            C1x4.setDisabledIcon(Ox4);
            x4check[2][0] = 2;
        }
        x4winner();

        turn();
        C1x4.setEnabled(false);
    }//GEN-LAST:event_C1x4ActionPerformed

    //same as the first button but for the current one instead
    private void D1x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D1x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            D1x4.setIcon(Xx4);
            D1x4.setDisabledIcon(Xx4);
            x4check[3][0] = 1;
        } else {
            D1x4.setIcon(Ox4);
            D1x4.setDisabledIcon(Ox4);
            x4check[3][0] = 2;
        }
        x4winner();

        turn();
        D1x4.setEnabled(false);
    }//GEN-LAST:event_D1x4ActionPerformed

    //same as the first button but for the current one instead
    private void A2x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A2x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            A2x4.setIcon(Xx4);
            A2x4.setDisabledIcon(Xx4);
            x4check[0][1] = 1;
        } else {
            A2x4.setIcon(Ox4);
            A2x4.setDisabledIcon(Ox4);
            x4check[0][1] = 2;
        }
        x4winner();

        turn();
        A2x4.setEnabled(false);
    }//GEN-LAST:event_A2x4ActionPerformed

    //same as the first button but for the current one instead
    private void B2x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B2x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            B2x4.setIcon(Xx4);
            B2x4.setDisabledIcon(Xx4);
            x4check[1][1] = 1;
        } else {
            B2x4.setIcon(Ox4);
            B2x4.setDisabledIcon(Ox4);
            x4check[1][1] = 2;
        }
        x4winner();

        turn();
        B2x4.setEnabled(false);
    }//GEN-LAST:event_B2x4ActionPerformed

    //same as the first button but for the current one instead
    private void C2x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C2x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            C2x4.setIcon(Xx4);
            C2x4.setDisabledIcon(Xx4);
            x4check[2][1] = 1;
        } else {
            C2x4.setIcon(Ox4);
            C2x4.setDisabledIcon(Ox4);
            x4check[2][1] = 2;
        }
        x4winner();

        turn();
        C2x4.setEnabled(false);
    }//GEN-LAST:event_C2x4ActionPerformed

    //same as the first button but for the current one instead
    private void D2x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D2x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            D2x4.setIcon(Xx4);
            D2x4.setDisabledIcon(Xx4);
            x4check[3][1] = 1;
        } else {
            D2x4.setIcon(Ox4);
            D2x4.setDisabledIcon(Ox4);
            x4check[3][1] = 2;
        }
        x4winner();

        turn();
        D2x4.setEnabled(false);
    }//GEN-LAST:event_D2x4ActionPerformed

    //same as the first button but for the current one instead
    private void A3x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A3x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            A3x4.setIcon(Xx4);
            A3x4.setDisabledIcon(Xx4);
            x4check[0][2] = 1;
        } else {
            A3x4.setIcon(Ox4);
            A3x4.setDisabledIcon(Ox4);
            x4check[0][2] = 2;
        }
        x4winner();

        turn();
        A3x4.setEnabled(false);
    }//GEN-LAST:event_A3x4ActionPerformed

    //same as the first button but for the current one instead
    private void B3x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B3x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            B3x4.setIcon(Xx4);
            B3x4.setDisabledIcon(Xx4);
            x4check[1][2] = 1;
        } else {
            B3x4.setIcon(Ox4);
            B3x4.setDisabledIcon(Ox4);
            x4check[1][2] = 2;
        }
        x4winner();

        turn();
        B3x4.setEnabled(false);
    }//GEN-LAST:event_B3x4ActionPerformed

    //same as the first button but for the current one instead
    private void C3x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C3x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            C3x4.setIcon(Xx4);
            C3x4.setDisabledIcon(Xx4);
            x4check[2][2] = 1;
        } else {
            C3x4.setIcon(Ox4);
            C3x4.setDisabledIcon(Ox4);
            x4check[2][2] = 2;
        }
        x4winner();

        turn();
        C3x4.setEnabled(false);
    }//GEN-LAST:event_C3x4ActionPerformed

    //same as the first button but for the current one instead
    private void D3x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D3x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            D3x4.setIcon(Xx4);
            D3x4.setDisabledIcon(Xx4);
            x4check[3][2] = 1;
        } else {
            D3x4.setIcon(Ox4);
            D3x4.setDisabledIcon(Ox4);
            x4check[3][2] = 2;
        }
        x4winner();

        turn();
        D3x4.setEnabled(false);
    }//GEN-LAST:event_D3x4ActionPerformed

    //same as the first button but for the current one instead
    private void A4x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_A4x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            A4x4.setIcon(Xx4);
            A4x4.setDisabledIcon(Xx4);
            x4check[0][3] = 1;
        } else {
            A4x4.setIcon(Ox4);
            A4x4.setDisabledIcon(Ox4);
            x4check[0][3] = 2;
        }
        x4winner();

        turn();
        A4x4.setEnabled(false);
    }//GEN-LAST:event_A4x4ActionPerformed

    //same as the first button but for the current one instead
    private void B4x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B4x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            B4x4.setIcon(Xx4);
            B4x4.setDisabledIcon(Xx4);
            x4check[1][3] = 1;
        } else {
            B4x4.setIcon(Ox4);
            B4x4.setDisabledIcon(Ox4);
            x4check[1][3] = 2;
        }
        x4winner();

        turn();
        B4x4.setEnabled(false);
    }//GEN-LAST:event_B4x4ActionPerformed

    //same as the first button but for the current one instead
    private void C4x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C4x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            C4x4.setIcon(Xx4);
            C4x4.setDisabledIcon(Xx4);
            x4check[2][3] = 1;
        } else {
            C4x4.setIcon(Ox4);
            C4x4.setDisabledIcon(Ox4);
            x4check[2][3] = 2;
        }
        x4winner();

        turn();
        C4x4.setEnabled(false);
    }//GEN-LAST:event_C4x4ActionPerformed

    //same as the first button but for the current one instead
    private void D4x4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D4x4ActionPerformed
        clicks++;
        if (turn.equals("x")) {
            D4x4.setIcon(Xx4);
            D4x4.setDisabledIcon(Xx4);
            x4check[3][3] = 1;
        } else {
            D4x4.setIcon(Ox4);
            D4x4.setDisabledIcon(Ox4);
            x4check[3][3] = 2;
        }
        x4winner();

        turn();
        D4x4.setEnabled(false);
    }//GEN-LAST:event_D4x4ActionPerformed

    //runs when the user presses the reset score button
    private void resetScoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetScoreButtonActionPerformed
        //resets all the win counters back to 0
        xWins = 0;
        oWins = 0;
        ties = 0;

        //run the scoreboard method to check for any changes and put them on the screen
        scoreboard();
    }//GEN-LAST:event_resetScoreButtonActionPerformed

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
            java.util.logging.Logger.getLogger(TicTacGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TicTacGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TicTacGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicTacGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TicTacGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A1x3;
    private javax.swing.JButton A1x4;
    private javax.swing.JButton A2x3;
    private javax.swing.JButton A2x4;
    private javax.swing.JButton A3x3;
    private javax.swing.JButton A3x4;
    private javax.swing.JButton A4x4;
    private javax.swing.JButton B1x3;
    private javax.swing.JButton B1x4;
    private javax.swing.JButton B2x3;
    private javax.swing.JButton B2x4;
    private javax.swing.JButton B3x3;
    private javax.swing.JButton B3x4;
    private javax.swing.JButton B4x4;
    private javax.swing.JButton C1x3;
    private javax.swing.JButton C1x4;
    private javax.swing.JButton C2x3;
    private javax.swing.JButton C2x4;
    private javax.swing.JButton C3x3;
    private javax.swing.JButton C3x4;
    private javax.swing.JButton C4x4;
    private javax.swing.JButton D1x4;
    private javax.swing.JButton D2x4;
    private javax.swing.JButton D3x4;
    private javax.swing.JButton D4x4;
    private javax.swing.JButton backButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JTextField cWinsField;
    private javax.swing.JLabel cWinsLabel;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton fourXButton;
    private javax.swing.JPanel fourXgamePanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JLayeredPane grid3x3;
    private javax.swing.JLayeredPane grid4x4;
    private javax.swing.JLabel gridSizeLabel;
    private javax.swing.JPanel gridSizePanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLayeredPane layeredPane;
    private javax.swing.JLayeredPane layeredPaneGrids;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JTextField oWinsField;
    private javax.swing.JLabel oWinsLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton resetScoreButton;
    private javax.swing.JPanel scoresPanel;
    private javax.swing.JButton threeXButton;
    private javax.swing.JPanel threeXgamePanel;
    private javax.swing.JLabel topLabel;
    private javax.swing.JTextField xWinsField;
    private javax.swing.JLabel xWinsLabel;
    // End of variables declaration//GEN-END:variables
}
