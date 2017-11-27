package projetandroidmaster1.memory;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Samuel on 20/11/2017.
 * Class used to play the sound
 */

public class Media extends Thread {

    private MediaPlayer player;

    public Media(Context ctx){
        player = MediaPlayer.create(ctx,R.raw.bip);
    }

    public void run(){
        player.start();//do not be called in UI thread => app not responding
        try {
            Thread.sleep(player.getDuration());
            player.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
