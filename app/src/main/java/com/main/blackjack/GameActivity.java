package com.main.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class GameActivity extends AppCompatActivity {

    private GameView2 gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button stand = (Button)findViewById(R.id.standBtn);
        Button hit = (Button)findViewById(R.id.hitBtn);
        Button split = (Button)findViewById(R.id.splitBtn);
        Button doubleDown = (Button)findViewById(R.id.doubleBtn);
        SeekBar betBar = (SeekBar)findViewById(R.id.seekBar);

        LinearLayout surface = (LinearLayout)findViewById(R.id.surface);
        surface.addView(new GameView2(this, stand, hit, split, doubleDown, betBar));


//        Button stand = (Button)findViewById(R.id.standBtn);
//        stand.setText(R.string.app_name);

        //gameView = new GameView2(this);


    }

}
