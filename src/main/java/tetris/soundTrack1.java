/*
 * This defines the first soundtrack
 */
package tetris;


import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import static tetris.TetrisISU.gameRunning;

//Imports the tools that java needs to run this program

public class soundTrack1 extends Thread{
    @Override //allows the method to override the parent class
    public void run(){
        while(gameRunning){
            //new inpurstream is, is sent the mp3 file we want to play
            
            InputStream is = getClass().getResourceAsStream("/tetrisMusic/TetrisMusic1.mp3");
            try{ //If an error occurs try and catch will display it in the console and prevents the game from crashing
                AdvancedPlayer player = new AdvancedPlayer(is); //creates a new AdvancedPlayer
                player.play(); //Activates the player
            } catch(JavaLayerException ex) {
            }
        } 
    }
}
