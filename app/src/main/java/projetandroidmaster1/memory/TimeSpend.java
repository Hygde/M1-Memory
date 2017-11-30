package projetandroidmaster1.memory;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Samuel on 20/11/2017.
 */

public class TimeSpend{

    private final long SEC = 1000;
    private ProgressBar bar;
    private long time;
    private long current_time;

    public TimeSpend(ProgressBar b, long time){
        bar = b;
        this.time = time;
    }

    public long getCurrentTime(){return current_time;}

    public void startChrono(){
        new CountDownTimer(time, SEC) {

            @Override
            public void onTick(long l) {
                current_time = l;
                bar.setProgress((int)l);
            }

            @Override
            public void onFinish() {
                bar.setProgress(0);
                //todo : call function to end the game
                GameSurfaceView.setLoose();
            }
        }.start();
    }
}