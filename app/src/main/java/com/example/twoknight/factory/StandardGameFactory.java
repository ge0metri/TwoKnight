package com.example.twoknight.factory;

import com.example.twoknight.framework.Hero;
import com.example.twoknight.standard.GameConstants;
import com.example.twoknight.standard.StandardHero;
import com.example.twoknight.strategy.StandardDifficultyHandler;
import com.example.twoknight.strategy.DifficultyHandler;

public class StandardGameFactory implements GameFactory {
    private final DifficultyHandler difficultyHandler;
    private final int currentLevel;
    private int[] boughtSkills;

    public StandardGameFactory(int currentLevel) {
        difficultyHandler = new StandardDifficultyHandler(currentLevel);
        this.currentLevel = currentLevel;
    }

    public StandardGameFactory(int currentLevel, int[] boughtSkills) {
        difficultyHandler = new StandardDifficultyHandler(currentLevel);
        this.currentLevel = currentLevel;
        this.boughtSkills = boughtSkills;
    }

    @Override
    public DifficultyHandler getDifficultyHandler() {
        return difficultyHandler;
    }

    @Override
    public Hero buildHero() {
        int heroMaxHealth = (currentLevel <= 2) ? GameConstants.HERO_MAX_HEALTH / 2 : GameConstants.HERO_MAX_HEALTH;
        int heroStartingShield = (currentLevel <= 5) ? 0 : Math.min(1 << (currentLevel + 1), 1024); //  difficultyHandler.getShield()
        return new StandardHero(GameConstants.STANDARD_HERO_TYPE, heroMaxHealth, 0, heroStartingShield);
    }

    @Override
    public int[] getBoughtSkills() {
        return boughtSkills;
    }
}
