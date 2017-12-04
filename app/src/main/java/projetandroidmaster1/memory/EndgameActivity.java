package projetandroidmaster1.memory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class EndgameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        TextView textView;

        boolean win = getIntent().getBooleanExtra("WIN", false);
        textView = findViewById(R.id.textWin);
        if (win) {
            textView.setText(R.string.EndgameActivity_textview_win);
        }
        else {
            textView.setText(R.string.EndgameActivity_textview_lose);
        }

        long time = getIntent().getLongExtra("REMAINING_TIME", 0);
        textView = findViewById(R.id.textValueTime);
        textView.setText(String.valueOf((double)time/100));

        int tries = getIntent().getIntExtra("REMAINING_TRIES", 0);
        textView = findViewById(R.id.textValueScore);
        textView.setText(String.valueOf(tries));
    }
}
