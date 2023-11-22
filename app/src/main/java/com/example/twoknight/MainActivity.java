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

    // TODO: difficulty handler (Balance)
    // TODO: Add easter egg? (credits)
    // TODO: Add small feedback animations - shake, puffs stuff
    // TODO: add banner

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
