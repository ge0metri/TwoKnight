package com.example.twoknight.tiles;

import com.example.twoknight.framework.ShopTile;
import com.example.twoknight.framework.Tile;

public class StandardShopTile implements ShopTile {

    private final String type;
    private final String button;
    private final int cost;
    private boolean merged;

    public StandardShopTile(String type, String button, int cost) {
        this.type = type;
        this.button = button;
        this.cost = cost;
    }

    @Override
    public String getDescription() {
        return type;
    }

    @Override
    public String getButton() {
        return button;
    }


    @Override
    public int getValue() {
        return cost;
    }

    @Override
    public void setMerged(boolean m) {
        merged = m;
    }

    @Override
    public boolean canMergeWith(Tile other) {
        return merged;
    }

    @Override
    public int mergeWith(Tile other) {
        return 0;
    }

    @Override
    public boolean getMerged() {
        return merged;
    }
}
