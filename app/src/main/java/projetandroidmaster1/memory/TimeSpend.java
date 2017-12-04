package projetandroidmaster1.memory;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

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
                currentTime = l;
            }

            @Override
            public void onFinish() {
                currentTime = 0;
                end = true;
                Handler UIHandler = new Handler(Looper.getMainLooper());
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bar.setProgress(0);
                    }
                });
                GameSurfaceView.setLoose();
            }
        }.start();
    }
    public long getRemainingTime() {
        return this.currentTime;
    }
    public boolean isStart() { return this.start; }
    public boolean isEnd() { return this.end; }
}