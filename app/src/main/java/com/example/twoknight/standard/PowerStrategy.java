package com.example.twoknight.standard;

import com.example.twoknight.framework.MutableGame;

public interface PowerStrategy {
    boolean prepareSkill(MutableGame standardGame, int e);

    void useSkill(MutableGame standardGame, int pointer);

    void cancel(MutableGame game);

    int getPower();
}
