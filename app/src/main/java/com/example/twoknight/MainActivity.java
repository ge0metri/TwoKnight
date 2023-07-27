package com.example.twoknight;

import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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



    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    private StandardView gameView;

    private FloatingActionButton fab;
    private Game standardGame = new StandardGame(new StandardGameFactory(1));
    private AdView adView;
    DataSaver dataSaver = new DataSaver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set the content view to your custom GameView
        gameView = (StandardView)findViewById(R.id.gameView);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        adView = (AdView)findViewById(R.id.adView);
        gameView.addGame(standardGame);
    }
    // TODO: Add healthbar.

    public void btn10(View view) {
        Snackbar.make(view, "BAH", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show();
        standardGame.endTurn(KeyEvent.VK_UP);
        gameView.invalidate();
    }
}
