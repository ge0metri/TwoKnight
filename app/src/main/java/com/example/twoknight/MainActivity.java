package com.example.twoknight;

import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.twoknight.databinding.ActivityMainBinding;
import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.Game;
import com.example.twoknight.standard.KeyEvent;
import com.example.twoknight.standard.StandardGame;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    // TODO: 1) Make data saver thing. Prob only level and money/power
    // TODO: 2) Make menu screen
    // TODO: 3) implement tutorial, probably by fragments at some point
    // TODO: 4) implement visual for powers
    // TODO: 5) adjust difficulty, and make powers either fully rougelike or forever
    // TODO: 6) More stuff later probably
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    private FloatingActionButton fab;
    private Game standardGame = new StandardGame(new StandardGameFactory(1));
    private AdView adView;
    DataSaver dataSaver = new DataSaver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set the content view to your custom GameView
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.GameFragment);
    }
    public void btn10(View view) {
        Snackbar.make(view, "BAH", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab8)
                .setAction("Action", null).show();
    }

}
