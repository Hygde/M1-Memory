package projetandroidmaster1.memory;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        FM = new FileManagement(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Button btn = (Button)findViewById(R.id.MainActivity_button_play);
        if(FM.readGameState()!=null)btn.setText(R.string.MainActivity_button_continue);
        else btn.setText(R.string.MainActivity_button_play);
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
    public void quitGame(View view) {
        finish();
    }

    public void debug_toast(String input) {
        Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void dbgfunc(int[][] mat){
        for(int i = 0; i < mat.length; i++){
            String str = "";
            for(int j = 0; j < mat[0].length; j++){
                str =  str + String.valueOf(mat[i][j])+" ";
            }
            Log.e("MEMORY : ","MainActivity.dbgfunc() : "+str);
        }
    }
}
