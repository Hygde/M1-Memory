package projetandroidmaster1.memory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBarVolume;

    /***********************************************************************************************
     * ****************************** Activity Life Circle *****************************************
     * *********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MEMORY : ","SettingActivity.onPause()");
        int position = seekBarVolume.getProgress();
        //todo : save into a file this value;
        //todo : send the new value to the music service
    }

    /***********************************************************************************************
     * *********************************** functions ***********************************************
     * ********************************************************************************************/
}
