package com.example.twoknight;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.twoknight.databinding.ActivityMainBinding;
import com.example.twoknight.framework.Game;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    // TODO: 1) Make dataSaver do field also?
    // TODO 2) refactor shop
    // TODO 3) difficulty handler
    // TODO: 4) implement visual for powers
    // TODO: 5) adjust difficulty, and make powers either fully roguelike or forever
    // TODO: 6) More stuff later probably
    // TODO: if unlimited -> add odds for tiles
    // TODO: Make shop art
    // TODO: Add small feedback animations - shake, puffs stuff
    // TODO: Maybe balance stuff like hero at the edge early later in middle
    // TOD: 3) implement tutorial --ok--
    // TOD 1) Make end screen --ok--

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameManager.getInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameManager.getInstance().deleteGame();
    }
}
