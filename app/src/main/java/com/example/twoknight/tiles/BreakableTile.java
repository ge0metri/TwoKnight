package com.example.twoknight.tiles;

import com.example.twoknight.framework.ImmovableTile;
import com.example.twoknight.framework.Tile;

public class BreakableTile implements ImmovableTile {

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public void setMerged(boolean m) {

    }

    @Override
    public boolean canMergeWith(Tile other) {
        return false;
    }

    @Override
    public int mergeWith(Tile other) {
        return 0;
    }

    @Override
    public boolean getMerged() {
        return false;
    }
}
