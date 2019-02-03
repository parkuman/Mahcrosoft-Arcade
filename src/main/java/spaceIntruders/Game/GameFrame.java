
package spaceIntruders.Game;

import mainMenu.arcadeGUI;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Spartan Tech
 */
public class GameFrame extends JFrame
{
    private GamePanel game;
    
    
    int highScore;
    
    
    
    public GameFrame()
    {
        // Add text to title bar 
        super("Space Intruders");
        
        // Make sure the program exits when the close button is clicked
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                JFrame frame = (JFrame)e.getSource();
 
                int result = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to exit the game? Your score will not be saved",
                "Exit Game",
                JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    GamePanel.shutdown=1;
                    System.out.println(". . .Closing SpaceIntruders . . .");
                    dispose();
            }
        });
        
        
        
        
        // Create an instance of the Game class and turn on double buffering
        //  to ensure smooth animation
        game = new GamePanel(this);
        game.setDoubleBuffered(true);
        
        // Add the Breakout instance to this frame's content pane to display it
        this.getContentPane().add(game); 
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        
        // Start the game
        game.start();  
    }
    
    
    
    public static void main(String[] args) 
    {
         java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
        
    }
}
