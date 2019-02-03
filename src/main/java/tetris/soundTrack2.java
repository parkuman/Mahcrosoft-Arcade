/*
 * This defines the second soundtrack
 */
package tetris;


import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import static tetris.TetrisISU.gameRunning;
//Imports the tools that java needs to run this program

public class soundTrack2 extends Thread{ //Refer to soundTrack1
    @Override 
    public void run(){
        while(gameRunning){
            //new input stream is send the music we want to play
            InputStream is = getClass().getResourceAsStream("/tetrisMusic/TetrisMusic2.mp3");
            try{
                AdvancedPlayer player = new AdvancedPlayer(is);
                player.play();
            } catch(JavaLayerException ex) {}
        } 
    }
}
