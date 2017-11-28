package projetandroidmaster1.memory;

import android.os.CountDownTimer;

/**
 * Created by paulv on 28/11/2017.
 */

public class CountDown extends CountDownTimer {

    public CountDown(int time) {
        super(time, 1000);
    }

    public void onTick(long millisUntilFinished) {
        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
    }

    public void onFinish() {
        //mTextField.setText("done!");
    }
}
