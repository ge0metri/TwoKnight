package com.example.twoknight.framework;

public interface GameListener {
    void onLevelCleared(Game game, GameState status);

    void onPowerUse(boolean powerUsed);
}
