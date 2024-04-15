package com.example.twoknight.factory;

import com.example.twoknight.framework.Hero;
import com.example.twoknight.strategy.DifficultyHandler;

public interface GameFactory {
    DifficultyHandler getDifficultyHandler();

    Hero buildHero();

    int[] getBoughtSkills();
}
