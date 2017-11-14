package projetandroidmaster1.memory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by paulv on 14/11/2017.
 * Game activity which launches the view and runs the main thread
 */



public class GameActivity extends AppCompatActivity {

    private GameSurfaceView gameView;

    // Oncreate we directly setup the GameSurfaceView, needed to display the game
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recuperation de la vue une voie cree à partir de son id
        gameView = (GameSurfaceView) findViewById(R.id.GameSurfaceView);
        // rend visible la vue
        gameView.setVisibility(View.VISIBLE);

    }
}
