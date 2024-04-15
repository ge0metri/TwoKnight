package com.example.twoknight.strategy;

import com.example.twoknight.framework.MutableHero;
import com.example.twoknight.framework.Tile;

public interface HeroBrain {
    int getMaxShield();

    boolean canMerge(MutableHero standardHero, Tile other);

    int damage(MutableHero standardHero, Tile other);
}
