package projetandroidmaster1.memory;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Samuel on 20/11/2017.
 */

public class TimeSpend{

    private final long  SEC = 1000;
    private ProgressBar bar;
    private long        time;
    private long        currentTime;
    private boolean     start = false;
    private boolean     end = false;

    public TimeSpend(ProgressBar b, long time){
        bar = b;
        this.time = time;
    }

    public void startChrono(){
        this.start = true;
        new CountDownTimer(time, SEC) {

            @Override
            public void onTick(long l) {
                final long temp = l;
                Handler UIHandler = new Handler(Looper.getMainLooper());
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress((int)temp);
                    }
                });

            }

            @Override
            public void onFinish() {
                Handler UIHandler = new Handler(Looper.getMainLooper());
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(0);
                    }
                });
                currentTime = 0;
                end = true;
                GameSurfaceView.setLoose();
            }
        }.start();
    }
    public long getRemainingTime() {
        return this.currentTime;
    }
    public boolean isStart() { return this.start; }
    public boolean isEnd() { return this.end; }
    public void setCurrentTime(long c) { this.currentTime = c; }
}