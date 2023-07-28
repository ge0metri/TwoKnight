package com.example.twoknight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.twoknight.framework.Game;

public class HealthView extends View {
    private Game game;

    public HealthView(Context context) {
        super(context);
    }
    public HealthView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public void addGame(Game game){
        this.game = game;
    }

}
