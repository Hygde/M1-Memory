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

    public TimeSpend(ProgressBar b, long time){
        bar = b;
        this.time = time;
    }

    public void startChrono(){
        new CountDownTimer(time, SEC) {

            @Override
            public void onTick(long l) {
                bar.setProgress((int)l);
            }

            @Override
            public void onFinish() {
                //ce que tu veux faire quand le tps est écoulé
            }
        }.start();
    }
}