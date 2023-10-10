package com.example.twoknight.framework;

import com.example.twoknight.standard.HeroListener;

public interface GameListener extends HeroListener {
    void onLevelCleared(Game game, GameState status);

    void onPowerUse(boolean powerUsed);

    void onHighDamage();

    void addTile(int[] out);

    void onMove(int[] start, int[] end, int startValue);

    void onBeginLaser(int i, int j);

    void onFireLaser(int i, int j, boolean skipLaser);
}
