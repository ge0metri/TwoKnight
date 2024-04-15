package com.example.twoknight.variants;

import com.example.twoknight.framework.Game;

public interface Shop extends Game {
    void nextShop(int currentLevel, int[] boughtSkills);

    int getBalance();
}
