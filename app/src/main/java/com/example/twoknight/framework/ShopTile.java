package com.example.twoknight.framework;

/**
 * Subclass of Tile, to add functionality to shop tiles.
 */
public interface ShopTile extends Tile {
    String getDescription();

    String getButton();
}
