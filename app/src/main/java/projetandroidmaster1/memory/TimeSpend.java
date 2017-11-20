package projetandroidmaster1.memory;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Chronometer;

/**
 * Created by Samuel on 20/11/2017.
 */

public class TimeSpend{

    private Chronometer chronometer;

    public TimeSpend(Context ctx){
        chronometer = new Chronometer(ctx);
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    public void StartChrono(){chronometer.start();}
    public long getTimeElapse(){return SystemClock.elapsedRealtime() - chronometer.getBase();}
    public void StopChrono(){chronometer.stop();}
}
