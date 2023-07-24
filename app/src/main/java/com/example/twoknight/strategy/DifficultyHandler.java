package com.example.twoknight.strategy;

import com.example.twoknight.framework.MutableGame;

public interface DifficultyHandler {
    void prepareNextRound(MutableGame game);

    int getCurrentLevel();
}
