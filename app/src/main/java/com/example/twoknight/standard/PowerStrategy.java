package com.example.twoknight.standard;

import com.example.twoknight.framework.MutableGame;

import java.util.Set;

public interface PowerStrategy {
    boolean prepareSkill(MutableGame standardGame, int e);

    void useSkill(MutableGame standardGame, int pointer);

    void cancel(MutableGame game);

    Set<Integer> getPower();

    void endTurn(MutableGame standardGame);
}
