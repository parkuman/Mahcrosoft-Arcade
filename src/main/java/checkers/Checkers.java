package checkers;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainMenu.arcadeGUI;

/**
 * Adil Shah Mr. Mah This program simulates a checkers game with 2 players.
 * White always starts, and when moving if a jump is available the player has to
 * jump
 */
public class Checkers extends JPanel {

    public static void main(String[] args) {

    }

    public static void startGame() {
        JFrame frame = new JFrame("Checkers");
        Checkers details = new Checkers();
        frame.setContentPane(details);
        frame.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screensize.width - frame.getWidth()) / 2,
                (screensize.height - frame.getHeight()) / 2);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private JButton newGame;
    //Creates a button call newGame
    private JButton forfeit;
    //Creates a button call forfeit

    private JLabel message;
    //creates a label called message

    /**
     * The constructor creates the Board (which will create and manage the
     * buttons and message label), adds all the components to the board , and
     * sets the bounds of the components. A null layout is used. (This is the
     * only thing that is done in the main Checkers class.)
     */
    public Checkers() {

        setLayout(null);
        //sets layout as null
        setPreferredSize(new Dimension(900, 750));
        //sets preferred size of application
        setBackground(new Color(0, 200, 200));  // Dark green background.

        /* Create the components and add them to the applet. */
        Board board = new Board();  // Note: The constructor for the
        //   board also creates the buttons
        //   and label.
        add(board);
        add(newGame);
        add(forfeit);
        add(message);

        /* Set the position and size of each component by calling
       its setBounds() method. */
        board.setBounds(25, 25, 650, 650); // Note:  size MUST be 164-by-164 !
        newGame.setBounds(700, 120, 120, 50);
        forfeit.setBounds(700, 480, 120, 50);
        message.setBounds(0, 700, 350, 30);

    } // end constructor

    // --------------------  Nested Classes -------------------------------
    /**
     * A CheckersMove object represents a move in the game of Checkers. It holds
     * the row and column of the piece that is to be moved and the row and
     * column of the square to which it is to be moved. (This class makes no
     * guarantee that the move is legal.)
     */
    private static class CheckersMove {

        int initialRow, initialCol;  // Position of piece to be moved.
        int finalRow, finalCol;      // Square it is to move to.

        CheckersMove(int r1, int c1, int r2, int c2) {
            // Constructor.  Just set the values of the instance variables.
            initialRow = r1;
            initialCol = c1;
            finalRow = r2;
            finalCol = c2;
        }

        boolean Jump() {
            // Test whether this move is a jump.  It is assumed that
            // the move is legal.  In a jump, the piece moves two
            // rows.  (In a regular move, it only moves one row.)
            return (initialRow - finalRow == 2 || initialRow - finalRow == -2);
        }
    }  // end class CheckersMove.

    /**
     * This panel displays a 160-by-160 checkerboard pattern with a 2-pixel
     * black border. It is assumed that the size of the panel is set to exactly
     * 164-by-164 pixels. This class does the work of letting the users play
     * checkers, and it displays the checkerboard.
     */
    private class Board extends JPanel implements ActionListener, MouseListener {

        CheckersInfo board;  // The data for the checkers board is kept here.
        //    This board is also responsible for generating
        //    lists of legal moves.

        boolean gameRunning; // Is a game currently in progress?

        /* The next three variables are valid only when the game is in progress. */
        int turn;      // Whose turn is it now?  The possible values
        //    are CheckersData.RED and CheckersData.BLACK.

        int selectedRow, selectedCol;  // If the current player has selected a piece to
        //     move, these give the row and column
        //     containing that piece.  If no piece is
        //     yet selected, then selectedRow is -1.

        CheckersMove[] legal;  // An array containing the legal moves for the
        //   current player.

        /**
         * Constructor. Create the buttons and label. Listens for mouse clicks
         * and for clicks on the buttons. Create the board and start the first
         * game.
         */
        Board() {
            setBackground(Color.BLACK);
            addMouseListener(this);
            forfeit = new JButton("Resign");
            forfeit.addActionListener(this);
            newGame = new JButton("New Game");
            newGame.addActionListener(this);
            message = new JLabel("", JLabel.CENTER);
            message.setFont(new Font("Serif", Font.BOLD, 14));
            message.setForeground(Color.black);
            board = new CheckersInfo();
            NewGame();
        }

        /**
         * Respond to user's click on one of the two buttons.
         */
        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src == newGame) {
                NewGame();
            } else if (src == forfeit) {
                Forfeit();
            }
        }

        /**
         * Start a new game
         */
        void NewGame() {
            if (gameRunning == true) {
                // This should not be possible, but it doesn't hurt to check.
                message.setText("Finish the current game first!");
                return;
            }
            board.setUp();   // Set up the pieces.
            turn = CheckersInfo.WHITE;   // RED moves first.
            legal = board.Legal(CheckersInfo.WHITE);  // Get RED's legal moves.
            selectedRow = -1;   // RED has not yet selected a piece to move.
            message.setText("Red:  Make your move.");
            gameRunning = true;
            newGame.setEnabled(false);
            forfeit.setEnabled(true);
            repaint();
        }

        /**
         * Current player resigns. Game ends. Opponent wins.
         */
        void Forfeit() {
            if (gameRunning == false) {  // Should be impossible.
                message.setText("There is no game in progress!");
                return;
            }
            if (turn == CheckersInfo.WHITE) {
                gameEnd("RED forfeits.  BLACK wins.");
            } else {
                gameEnd("BLACK forfeits.  RED wins.");
            }

        }

        /**
         * The game ends. The parameter, str, is displayed as a message to the
         * user. The states of the buttons are adjusted so players can start a
         * new game. This method is called when the game ends at any point in
         * this class.
         */
        void gameEnd(String str) {
            //if the player who goes first wins, i assume that player is the current user of mahcrosoft arcade, therefore set their score to increment by 1 in the spreadsheet
            if (str.equals("BLACK forfeits.  RED wins.")) {
                try {
                    arcadeGUI.setScore(arcadeGUI.getScore() + 1);
                } catch (IOException ex) {
                    Logger.getLogger(Checkers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            message.setText(str);
            newGame.setEnabled(true);
            forfeit.setEnabled(false);
            gameRunning = false;
        }

        /**
         * This is called by mousePressed() when a player clicks on the square
         * in the specified row and col. It has already been checked that a game
         * is, in fact, in progress.
         */
        void ClickSquare(int row, int col) {

            /* If the player clicked on one of the pieces that the player
          can move, mark this row and col as selected and return.  (This
          might change a previous selection.)  Reset the message, in
          case it was previously displaying an error message. */
            for (int i = 0; i < legal.length; i++) {
                if (legal[i].initialRow == row && legal[i].initialCol == col) {
                    selectedRow = row;
                    selectedCol = col;
                    if (turn == CheckersInfo.WHITE) {
                        message.setText("RED:  Make your move.");
                    } else {
                        message.setText("BLACK:  Make your move.");
                    }
                    repaint();
                    return;
                }
            }

            /* If no piece has been selected to be moved, the user must first
          select a piece.  Show an error message and return. */
            if (selectedRow < 0) {
                message.setText("Click the piece you want to move.");
                return;
            }

            /* If the user clicked on a square where the selected piece can be
          legally moved, then make the move and return. */
            for (int i = 0; i < legal.length; i++) {
                if (legal[i].initialRow == selectedRow && legal[i].initialCol == selectedCol
                        && legal[i].finalRow == row && legal[i].finalCol == col) {
                    Move(legal[i]);
                    return;
                }
            }

            /* If we get to this point, there is a piece selected, and the square where
          the user just clicked is not one where that piece can be legally moved.
          Show an error message. */
            message.setText("Click the square you want to move to.");

        }  // end doClickSquare()

        /**
         * This is called when the current player has chosen the specified move.
         * Make the move, and then either end or continue the game
         * appropriately.
         */
        void Move(CheckersMove move) {

            board.Move(move);

            /* If the move was a jump, it's possible that the player has another
          jump.  Check for legal jumps starting from the square that the player
          just moved to.  If there are any, the player must jump.  The same
          player continues moving.
             */
            if (move.Jump()) {
                legal = board.getLegalJumpsFrom(turn, move.finalRow, move.finalCol);
                if (legal != null) {
                    if (turn == CheckersInfo.WHITE) {
                        message.setText("RED:  You must continue jumping.");
                    } else {
                        message.setText("BLACK:  You must continue jumping.");
                    }
                    selectedRow = move.finalRow;  // Since only one piece can be moved, select it.
                    selectedCol = move.finalCol;
                    repaint();
                    return;
                }
            }

            /* The current player's turn is ended, so change to the other player.
          Get that player's legal moves.  If the player has no legal moves,
          then the game ends. */
            if (turn == CheckersInfo.WHITE) {
                turn = CheckersInfo.BLACK;
                legal = board.Legal(turn);
                if (legal == null) {
                    gameEnd("BLACK has no moves.  RED wins.");
                } else if (legal[0].Jump()) {
                    message.setText("BLACK:  Make your move.  You must jump.");
                } else {
                    message.setText("BLACK:  Make your move.");
                }
            } else {
                turn = CheckersInfo.WHITE;
                legal = board.Legal(turn);
                if (legal == null) {
                    gameEnd("RED has no moves.  BLACK wins.");
                } else if (legal[0].Jump()) {
                    message.setText("RED:  Make your move.  You must jump.");
                } else {
                    message.setText("RED:  Make your move.");
                }
            }

            /* Set selectedRow = -1 to record that the player has not yet selected
          a piece to move. */
            selectedRow = -1;

            /* As a courtesy to the user, if all legal moves use the same piece, then
          select that piece automatically so the user won't have to click on it
          to select it. */
            if (legal != null) {
                boolean StartSquare = true;
                for (int i = 1; i < legal.length; i++) {
                    if (legal[i].initialRow != legal[0].initialRow
                            || legal[i].initialCol != legal[0].initialCol) {
                        StartSquare = false;
                        break;
                    }
                }
                if (StartSquare) {
                    selectedRow = legal[0].initialRow;
                    selectedCol = legal[0].initialCol;
                }
            }

            /* Make sure the board is redrawn in its new state. */
            repaint();

        }  // end doMakeMove();

        /**
         * Draw a checkerboard pattern in gray and lightGray. Draw the checkers.
         * If a game is in progress, hilite the legal moves.
         */
        public void paintComponent(Graphics grahics) {

            /* Draw a two-pixel black border around the edges of the canvas. */
            grahics.setColor(Color.black);
            grahics.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
            grahics.drawRect(1, 1, getSize().width - 3, getSize().height - 3);

            /* Draw the squares of the checkerboard and the checkers. */
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2) {
                        grahics.setColor(Color.RED);
                    } else {
                        grahics.setColor(Color.BLACK);
                    }
                    grahics.fillRect(8 + col * 80, 8 + row * 80, 80, 80);
                    switch (board.piece(row, col)) {
                        case CheckersInfo.WHITE:
                            grahics.setColor(Color.WHITE);
                            grahics.fillOval(16 + col * 80, 16 + row * 80, 60, 60);
                            break;
                        case CheckersInfo.BLACK:
                            grahics.setColor(Color.BLACK);
                            grahics.fillOval(16 + col * 80, 16 + row * 80, 60, 60);
                            break;
                        case CheckersInfo.WHITE_KING:
                            grahics.setColor(Color.WHITE);
                            grahics.fillOval(16 + col * 80, 16 + row * 80, 60, 60);
                            grahics.setColor(Color.BLACK);
                            grahics.drawString("K", 42 + col * 80, 50 + row * 80);
                            break;
                        case CheckersInfo.BLACK_KING:
                            grahics.setColor(Color.BLACK);
                            grahics.fillOval(16 + col * 80, 16 + row * 80, 60, 60);
                            grahics.setColor(Color.WHITE);
                            grahics.drawString("K", 42 + col * 80, 50 + row * 80);
                            break;
                    }
                }
            }

            /* If a game is in progress, hilite the legal moves.   Note that legalMoves
          is never null while a game is in progress. */
            if (gameRunning) {
                /* First, draw a 2-pixel cyan border around the pieces that can be moved. */
                grahics.setColor(Color.yellow);
                for (CheckersMove legal1 : legal) {
                    grahics.drawRect(8 + legal1.initialCol * 80, 8 + legal1.initialRow * 80, 76, 76);
                    grahics.drawRect(12 + legal1.initialCol * 80, 12 + legal1.initialRow * 80, 68, 68);
                }
                /* If a piece is selected for moving (i.e. if selectedRow >= 0), then
                draw a 2-pixel white border around that piece and draw green borders 
                around each square that that piece can be moved to. */
                if (selectedRow >= 0) {
                    grahics.setColor(Color.white);
                    grahics.drawRect(8 + selectedCol * 80, 8 + selectedRow * 80, 76, 76);
                    grahics.drawRect(12 + selectedCol * 80, 12 + selectedRow * 80, 68, 68);
                    grahics.setColor(Color.green);
                    for (CheckersMove legal1 : legal) {
                        if (legal1.initialCol == selectedCol && legal1.initialRow == selectedRow) {
                            grahics.drawRect(8 + legal1.finalCol * 80, 8 + legal1.finalRow * 80, 76, 76);
                            grahics.drawRect(12 + legal1.finalCol * 80, 12 + legal1.finalRow * 80, 68, 68);
                        }
                    }
                }
            }

        }  // end paintComponent()

        /**
         * Respond to a user click on the board. If no game is in progress, show
         * an error message. Otherwise, find the row and column that the user
         * clicked and call doClickSquare() to handle it.
         */
        @Override
        public void mousePressed(MouseEvent evt) {
            if (gameRunning == false) {
                message.setText("Click \"New Game\" to start a new game.");
            } else {
                int col = (evt.getX() - 8) / 80;
                int row = (evt.getY() - 8) / 80;
                if (col >= 0 && col < 8 && row >= 0 && row < 8) {
                    ClickSquare(row, col);
                }
            }
        }

        public void mouseReleased(MouseEvent evt) {
        }

        public void mouseClicked(MouseEvent evt) {
        }

        public void mouseEntered(MouseEvent evt) {
        }

        public void mouseExited(MouseEvent evt) {
        }

    }  // end class Board

    /**
     * An object of this class holds data about a game of checkers. It knows
     * what kind of piece is on each square of the checkerboard. Note that RED
     * moves "up" the board (i.e. row number decreases) while BLACK moves "down"
     * the board (i.e. row number increases). Methods are provided to return
     * lists of available legal moves.
     */
    private static class CheckersInfo {

        /*  The following constants represent the possible contents of a square
          on the board.  The constants RED and BLACK also represent players
          in the game. */
        static final int EMPTY = 0,
                WHITE = 1,
                WHITE_KING = 2,
                BLACK = 3,
                BLACK_KING = 4;

        int[][] board;  // board[r][c] is the contents of row r, column c.  

        /**
         * Constructor. Create the board and set it up for a new game.
         */
        CheckersInfo() {
            board = new int[8][8];
            setUp();
        }

        /**
         * Set up the board with checkers in position for the beginning of a
         * game. Note that checkers can only be found in squares that satisfy
         * row % 2 == col % 2. At the start of the game, all such squares in the
         * first three rows contain black squares and all such squares in the
         * last three rows contain red squares.
         */
        private void setUp() {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2) {
                        if (row < 3) {
                            board[row][col] = BLACK;
                        } else if (row > 4) {
                            board[row][col] = WHITE;
                        } else {
                            board[row][col] = EMPTY;
                        }
                    } else {
                        board[row][col] = EMPTY;
                    }
                }
            }
        }  // end setUpGame()

        /**
         * Return the contents of the square in the specified row and column.
         */
        int piece(int row, int col) {
            return board[row][col];
        }

        /**
         * Make the specified move. It is assumed that move is non-null and that
         * the move it represents is legal.
         */
        void Move(CheckersMove move) {
            DoMove(move.initialRow, move.initialCol, move.finalRow, move.finalCol);
        }

        /**
         * Make the move from (fromRow,fromCol) to (toRow,toCol). It is assumed
         * that this move is legal. If the move is a jump, the jumped piece is
         * removed from the board. If a piece moves to the last row on the
         * opponent's side of the board, the piece becomes a king.
         */
        void DoMove(int fromRow, int fromCol, int toRow, int toCol) {
            board[toRow][toCol] = board[fromRow][fromCol];
            board[fromRow][fromCol] = EMPTY;
            if (fromRow - toRow == 2 || fromRow - toRow == -2) {
                // The move is a jump.  Remove the jumped piece from the board.
                int jumpRow = (fromRow + toRow) / 2;  // Row of the jumped piece.
                int jumpCol = (fromCol + toCol) / 2;  // Column of the jumped piece.
                board[jumpRow][jumpCol] = EMPTY;
            }
            if (toRow == 0 && board[toRow][toCol] == WHITE) {
                board[toRow][toCol] = WHITE_KING;
            }
            if (toRow == 7 && board[toRow][toCol] == BLACK) {
                board[toRow][toCol] = BLACK_KING;
            }
        }

        /**
         * Return an array containing all the legal CheckersMoves for the
         * specified player on the current board. If the player has no legal
         * moves, null is returned. The value of player should be one of the
         * constants RED or BLACK; if not, null is returned. If the returned
         * value is non-null, it consists entirely of jump moves or entirely of
         * regular moves, since if the player can jump, only jumps are legal
         * moves.
         */
        CheckersMove[] Legal(int player) {

            if (player != WHITE && player != BLACK) {
                return null;
            }

            int playerKing;  // The constant representing a King belonging to player.
            if (player == WHITE) {
                playerKing = WHITE_KING;
            } else {
                playerKing = BLACK_KING;
            }

            ArrayList<CheckersMove> moves = new ArrayList<>();  // Moves will be stored in this list.

            /*  First, check for any possible jumps.  Look at each square on the board.
          If that square contains one of the player's pieces, look at a possible
          jump in each of the four directions from that square.  If there is 
          a legal jump in that direction, put it in the moves ArrayList.
             */
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player || board[row][col] == playerKing) {
                        if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2)) {
                            moves.add(new CheckersMove(row, col, row + 2, col + 2));
                        }
                        if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2)) {
                            moves.add(new CheckersMove(row, col, row - 2, col + 2));
                        }
                        if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2)) {
                            moves.add(new CheckersMove(row, col, row + 2, col - 2));
                        }
                        if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2)) {
                            moves.add(new CheckersMove(row, col, row - 2, col - 2));
                        }
                    }
                }
            }

            /*  If any jump moves were found, then the user must jump, so we don't 
          add any regular moves.  However, if no jumps were found, check for
          any legal regular moves.  Look at each square on the board.
          If that square contains one of the player's pieces, look at a possible
          move in each of the four directions from that square.  If there is 
          a legal move in that direction, put it in the moves ArrayList.
             */
            if (moves.isEmpty()) {
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (board[row][col] == player || board[row][col] == playerKing) {
                            if (canMove(player, row, col, row + 1, col + 1)) {
                                moves.add(new CheckersMove(row, col, row + 1, col + 1));
                            }
                            if (canMove(player, row, col, row - 1, col + 1)) {
                                moves.add(new CheckersMove(row, col, row - 1, col + 1));
                            }
                            if (canMove(player, row, col, row + 1, col - 1)) {
                                moves.add(new CheckersMove(row, col, row + 1, col - 1));
                            }
                            if (canMove(player, row, col, row - 1, col - 1)) {
                                moves.add(new CheckersMove(row, col, row - 1, col - 1));
                            }
                        }
                    }
                }
            }

            /* If no legal moves have been found, return null.  Otherwise, create
          an array just big enough to hold all the legal moves, copy the
          legal moves from the ArrayList into the array, and return the array. */
            if (moves.isEmpty()) {
                return null;
            } else {
                CheckersMove[] moveArray = new CheckersMove[moves.size()];
                for (int i = 0; i < moves.size(); i++) {
                    moveArray[i] = moves.get(i);
                }
                return moveArray;
            }

        }  // end getLegalMoves

        /**
         * Return a list of the legal jumps that the specified player can make
         * starting from the specified row and column. If no such jumps are
         * possible, null is returned. The logic is similar to the logic of the
         * getLegalMoves() method.
         */
        CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
            if (player != WHITE && player != BLACK) {
                return null;
            }
            int playerKing;  // The constant representing a King belonging to player.
            if (player == WHITE) {
                playerKing = WHITE_KING;
            } else {
                playerKing = BLACK_KING;
            }
            ArrayList<CheckersMove> moves = new ArrayList<>();  // The legal jumps will be stored in this list.
            if (board[row][col] == player || board[row][col] == playerKing) {
                if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2)) {
                    moves.add(new CheckersMove(row, col, row + 2, col + 2));
                }
                if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2)) {
                    moves.add(new CheckersMove(row, col, row - 2, col + 2));
                }
                if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2)) {
                    moves.add(new CheckersMove(row, col, row + 2, col - 2));
                }
                if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2)) {
                    moves.add(new CheckersMove(row, col, row - 2, col - 2));
                }
            }
            if (moves.isEmpty()) {
                return null;
            } else {
                CheckersMove[] moveArray = new CheckersMove[moves.size()];
                for (int i = 0; i < moves.size(); i++) {
                    moveArray[i] = moves.get(i);
                }
                return moveArray;
            }
        }  // end getLegalMovesFrom()

        /**
         * This is called by the two previous methods to check whether the
         * player can legally jump from (r1,c1) to (r3,c3). It is assumed that
         * the player has a piece at (r1,c1), that (r3,c3) is a position that is
         * 2 rows and 2 columns distant from (r1,c1) and that (r2,c2) is the
         * square between (r1,c1) and (r3,c3).
         */
        private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

            if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) {
                return false;  // (r3,c3) is off the board.
            }
            if (board[r3][c3] != EMPTY) {
                return false;  // (r3,c3) already contains a piece.
            }
            if (player == WHITE) {
                if (board[r1][c1] == WHITE && r3 > r1) {
                    return false;  // Regular red piece can only move up.
                }
                if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING) {
                    return false;  // There is no black piece to jump.
                }
                return true;  // The jump is legal.
            } else {
                if (board[r1][c1] == BLACK && r3 < r1) {
                    return false;  // Regular black piece can only move downn.
                }
                if (board[r2][c2] != WHITE && board[r2][c2] != WHITE_KING) {
                    return false;  // There is no red piece to jump.
                }
                return true;  // The jump is legal.
            }

        }  // end canJump()

        /**
         * This is called by the getLegalMoves() method to determine whether the
         * player can legally move from (r1,c1) to (r2,c2). It is assumed that
         * (r1,r2) contains one of the player's pieces and that (r2,c2) is a
         * neighboring square.
         */
        private boolean canMove(int player, int r1, int c1, int r2, int c2) {

            if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8) {
                return false;  // (r2,c2) is off the board.
            }
            if (board[r2][c2] != EMPTY) {
                return false;  // (r2,c2) already contains a piece.
            }
            if (player == WHITE) {
                if (board[r1][c1] == WHITE && r2 > r1) {
                    return false;  // Regular red piece can only move down.
                }
                return true;  // The move is legal.
            } else {
                if (board[r1][c1] == BLACK && r2 < r1) {
                    return false;  // Regular black piece can only move up.
                }
                return true;  // The move is legal.
            }

        }  // end canMove()

    } // end class CheckersData

} // end class Checkers
