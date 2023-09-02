package com.example.twoknight;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.twoknight.databinding.ActivityMainBinding;
import com.example.twoknight.framework.Game;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    // TOD: 1) Make data saver thing. Prob only level and money/power --ok--
    // TODO: 1.5) Clean up data loader. I need a good idea of how to make it
    // TODO: 2) Make menu screen -semi done-
    // TODO: 3) implement tutorial, probably by fragments at some point
    // TODO: 4) implement visual for powers
    // TODO: 5) adjust difficulty, and make powers either fully rougelike or forever
    // TODO: 6) More stuff later probably
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private DataSaver dataSaver;
    private Game game;
    private int currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSaver = new DataSaver(getApplicationContext());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.MenuFragment);
    }

    private void loadGame(DataSaver dataSaver) {

    }

    public void btn10(View view) {
        Snackbar.make(view, "BAH", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab8)
                .setAction("Action", null).show();
    }
    public void saveGame(){
    }
    public void startGame(){
    }

}
