package com.puckman.ololo.puckman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newGame(View view){
        Intent game = new Intent(this,GameView.class);
        startActivity(game);
    }

    public void options(View view) {
        Intent options = new Intent(this,OptionsActivity.class);
        startActivity(options);
    }

    public void about(View view){
        Intent about = new Intent(this,AboutActivity.class);
        startActivity(about);
    }

    public void exit(View view){
        this.finishAffinity();
    }
}
