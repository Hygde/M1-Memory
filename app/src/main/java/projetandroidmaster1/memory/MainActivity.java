package projetandroidmaster1.memory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public boolean debug = true;

    private FileManagement FM;

    /***********************************************************************************************
     * ****************************** Activity Life Circle *****************************************
     * *********************************************************************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*FM = new FileManagement(this);
        FM.WriteScore("150");
        ArrayList<Double> test = FM.ReadScoreFile();
        toast(test.get(0).toString());*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MEMORY : ","MainActivity.onPause()");
    }

    /***********************************************************************************************
     * *********************************** functions ***********************************************
     * ********************************************************************************************/

    // Called when the player taps the Play button
    public void launchGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    //called when the player taps the setting button
    public void launchSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //called when the player taps the score button
    public void launchScoreActivity(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    //called when the player taps the quit button
    public void QuitGame(View view) {
        finish();
    }

    public void debug_toast(String input) {
        Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
