package com.example.twoknight.strategy;

import com.example.twoknight.framework.MutableGame;
import com.example.twoknight.framework.Tile;

public interface DifficultyHandler {
    void prepareNextRound(MutableGame game);

    boolean isBlocksNotLaser();

    int getMaxShield();

    int getCurrentLevel();

    int getTileValue();

    boolean isUseSpawnRates();

    void setUseSpawnRates(boolean useSpawnRates);
}
