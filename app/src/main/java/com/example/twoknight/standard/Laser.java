package com.example.twoknight.standard;

public class Laser {
    private int height = 121 * (GameConstants.side0) + 15;
    private int width = 121 * (GameConstants.side1) + 15;
    private final int y;
    private final int x;
    private final boolean rowNotCol;
    private final int laserPosition;

    public Laser(boolean rowNotCol, int laserPosition) {
        this.rowNotCol = rowNotCol;
        this.laserPosition = laserPosition;

        int yOffset = 50;
        int xOffset = 100;
        if (rowNotCol) {
            y = yOffset + 20 + laserPosition * 121;
            x = xOffset - 5;
            height = 96;
            width = width + 10;
        } else {
            x = xOffset + 20 + laserPosition * 121;
            y = yOffset - 5;
            height = height + 10;
            width = 96;
        }
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getY() {
        return y;
    }

    public boolean getLaserPosition(int row, int col) {
        if (rowNotCol && row == laserPosition) {
            return true;
        } else return !rowNotCol && col == laserPosition;
    }

}
