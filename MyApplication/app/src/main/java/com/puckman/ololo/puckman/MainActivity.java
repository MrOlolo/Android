package com.puckman.ololo.puckman;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // System.out.println("Stop?");
    }

    @Override
    protected void onPause() {
        super.onPause();
       // System.out.println("Pause?");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // System.out.println("Destroy?");
    }

    @Override
    protected void onResume() {
        super.onResume();
       // System.out.println("Resume?");
    }

    public void newGame(View view){
        Intent game = new Intent(this,GameActivity.class);
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

