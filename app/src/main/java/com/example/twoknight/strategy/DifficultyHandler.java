package com.example.twoknight.strategy;

import com.example.twoknight.framework.MutableGame;
import com.example.twoknight.framework.Tile;

public interface DifficultyHandler {
    void prepareNextRound(MutableGame game);
    int getCurrentLevel();

    int getTileValue();
}
