package projetandroidmaster1.memory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private static final String[] ExistingSettings = {"SOUND"};

    private Switch Sound;
    private FileManagement FM;

    /***********************************************************************************************
     * ****************************** Activity Life Circle *****************************************
     * *********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Sound = (Switch) findViewById(R.id.SettingsActivity_soundswitch);
        FM = new FileManagement(this);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MEMORY : ","SettingActivity.onPause()");
        boolean enabled = Sound.isChecked();
        FM.saveAppSettings("SOUND="+String.valueOf(enabled));//save settings when activity onPause()
    }

    /***********************************************************************************************
     * *********************************** functions ***********************************************
     * ********************************************************************************************/

    //initialize all the view with parameter of the file
    private void initView(){
        HashMap<String,String>config = FM.readAppSettings();
        if(config.size() > 0){
            Sound.setChecked(Boolean.valueOf(config.get(ExistingSettings[0])));
        }
    }
}
