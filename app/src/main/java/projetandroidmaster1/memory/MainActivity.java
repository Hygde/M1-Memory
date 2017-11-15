package projetandroidmaster1.memory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FileManagement FM;

    /***********************************************************************************************
     * ****************************** Activity Life Circle *****************************************
     * *********************************************************************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Memory : ","onPause()");
    }

    /***********************************************************************************************
     * *********************************** functions ******************************************
     * ********************************************************************************************/

    // Called when the player taps the Play button
    public void launchGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void launchSettingsActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    public void launchScoreActivity(View view) {
        //Intent intent = new Intent(this, .class);
        //startActivity(intent);
    }

    public void QuitGame(View view) {
        finish();
    }
}
