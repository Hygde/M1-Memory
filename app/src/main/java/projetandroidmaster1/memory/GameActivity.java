package projetandroidmaster1.memory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by paulv on 14/11/2017.
 * Game activity which launches the view and runs the main thread
 */


public class GameActivity extends AppCompatActivity {

    private static final String[] ExistingSettings = {"SOUND"};

    private FileManagement      FM;
    private GameSurfaceView     gameView;

    private ProgressBar         maxTryBar;
    private int                 maxTryValue = 3;

    private ProgressBar         chronoBar;
    private TextView            chronoText;
    private long                time = 10000;

    // Oncreate we directly setup the GameSurfaceView, needed to display the game
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        // Init surface view
        gameView = (GameSurfaceView) findViewById(R.id.GameSurfaceView);

        // Init maxTry
        maxTryBar = (ProgressBar) findViewById(R.id.GameSurfaceView_barTry);
        maxTryBar.setMax(maxTryValue);
        maxTryBar.setProgress(maxTryValue);
        gameView.setMaxTry(maxTryBar, maxTryValue);

        // Init chrono
        chronoText = (TextView) findViewById(R.id.GameSurfaceView_textTime);
        chronoBar = (ProgressBar) findViewById(R.id.GameSurfaceView_barTime);
        chronoBar.setMax((int)time);
        chronoBar.setProgress((int)time);
        gameView.setChrono(chronoBar, time);

        gameView.setVisibility(View.VISIBLE);

        //File management init
        FM = new FileManagement(this);
        readAppSetting();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!gameView.isWin() && !gameView.isLoose()) {
            FM.saveGameState(chronoBar.getProgress(),5, gameView.truePanel);
        }
    }

    @Override
    // On destroying the game, we gather all game data
    protected void onDestroy() {
        super.onDestroy();

        if (gameView.isTheGameFinished()) {
            Intent intent = new Intent(this, EndgameActivity.class);
            intent.putExtra("WIN", gameView.isWin());
            intent.putExtra("REMAINING_TIME", gameView.getTime());
            intent.putExtra("REMAINING_TRIES", gameView.getRemainingTries());
            startActivity(intent);
        }
    }

    private void readAppSetting(){
        HashMap<String,String> settings = FM.readAppSettings();
        gameView.setSoundState(Boolean.parseBoolean(settings.get(ExistingSettings[0])));
    }

    public void debug_toast(String input) {
        Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}