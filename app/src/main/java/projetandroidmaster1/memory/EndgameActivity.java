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

        long time = getIntent().getLongExtra("TIME", 0);
        TextView textView = findViewById(R.id.textValueTime);
        textView.setText(String.valueOf((double)time/1000));
    }
}
