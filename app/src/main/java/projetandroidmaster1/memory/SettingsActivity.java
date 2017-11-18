package projetandroidmaster1.memory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private static final String[] ExistingSettings = {"VOLUME"};

    private SeekBar seekBarVolume;
    private FileManagement FM;

    /***********************************************************************************************
     * ****************************** Activity Life Circle *****************************************
     * *********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
        FM = new FileManagement(this);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MEMORY : ","SettingActivity.onPause()");
        int position = seekBarVolume.getProgress();
        FM.saveAppSettings("VOLUME="+String.valueOf((double)position));//save settings when activity onPause()
    }

    /***********************************************************************************************
     * *********************************** functions ***********************************************
     * ********************************************************************************************/

    //initialize all the view with parameter of the file
    private void initView(){
        HashMap<String,Double>config = FM.readAppSettings(ExistingSettings);
        if(config.size() > 0){
            seekBarVolume.setProgress(config.get(ExistingSettings[0]).intValue());
        }
    }
}
