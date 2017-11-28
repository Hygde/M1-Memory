package projetandroidmaster1.memory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by paulv on 14/11/2017.
 * Game activity which launches the view and runs the main thread
 */



public class GameActivity extends AppCompatActivity {
    private FileManagement      FM;
    private GameSurfaceView     gameView;

    // Oncreate we directly setup the GameSurfaceView, needed to display the game
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();

        // recuperation de la vue une fois creee à partir de son id
        gameView = (GameSurfaceView) findViewById(R.id.GameSurfaceView);
        // affiche la vue
        gameView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FM = new FileManagement(this);
        FM.saveGameState(5,gameView.truePanel);
    }

    @Override
    // On destroying the game, we gather all game data
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, EndgameActivity.class);
        intent.putExtra("WIN", gameView.isWin());
        intent.putExtra("TIME", gameView.getTime());
        intent.putExtra("NBTRY", gameView.getNbTry());
        startActivity(intent);
    }

    public void debug_toast(String input) {
        Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
