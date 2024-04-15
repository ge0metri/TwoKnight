package com.example.twoknight.tiles;

import com.example.twoknight.framework.Tile;

public class StandardTile implements Tile {
    private boolean merged;
    private int value;

    public StandardTile(int i) {
        this.value = i;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setMerged(boolean m) {
        merged = m;
    }

    @Override
    public boolean canMergeWith(Tile other) {
        return !merged && other != null && !other.getMerged() && Math.abs(value) == Math.abs(other.getValue());
    }

    @Override
    public int mergeWith(Tile other) {
        if (canMergeWith(other)) {
            value += other.getValue();
            merged = true;
            return value;
        }
        return -1;
    }

    @Override
    public boolean getMerged() {
        return merged;
    }
}
