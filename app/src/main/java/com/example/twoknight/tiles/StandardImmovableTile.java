package com.example.twoknight.tiles;

import com.example.twoknight.framework.ImmovableTile;
import com.example.twoknight.framework.Tile;

public class StandardImmovableTile implements ImmovableTile {
    private int value = 0;

    public StandardImmovableTile(int val) {
        value = val;
    }

    public StandardImmovableTile() {
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setMerged(boolean m) {

    }

    @Override
    public boolean canMergeWith(Tile other) {
        if (value == 0) {
            return false;
        }
        return other.getValue() == value;
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
