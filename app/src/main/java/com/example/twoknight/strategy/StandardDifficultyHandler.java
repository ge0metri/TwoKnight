package com.example.twoknight.strategy;

import com.example.twoknight.framework.MutableGame;
import com.example.twoknight.framework.MutableHero;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.tiles.GoldenTile;
import com.example.twoknight.tiles.StandardImmovableTile;

public class StandardDifficultyHandler implements DifficultyHandler {

    private final int currentLevel;
    private int shieldCounter = 0;

    public StandardDifficultyHandler(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    @Override
    public void prepareNextRound(MutableGame game) {
        MutableHero hero = (MutableHero) game.getHero();
        int currentTurn = game.getTurnNumber();
        boolean blocksNotLaser = currentLevel % 2 == 0;
        if (currentTurn == 0) {
            game.addTile(new StandardImmovableTile());
            if (blocksNotLaser && currentLevel > 1) {
                game.addTile(new StandardImmovableTile(8));
                game.addTile(new StandardImmovableTile());
            }
        }
        if (game.getLaserState() != null) {
            game.fireLaser();
        }
        int laserCD = 9 + currentLevel + game.checkTiles() / 2;
        if (!blocksNotLaser && currentLevel > 2 && isOffCD(currentTurn, laserCD)) {
            game.beginLaser();
        }

        refreshShield(hero, currentTurn);

        int blockCD = 8 + currentLevel;
        if (blocksNotLaser && currentLevel > 2 && isOffCD(currentTurn, blockCD)) {
            game.addTile(new StandardImmovableTile(4));
        }
        incrementGoldenTile(game.getField());
    }

    private void incrementGoldenTile(Tile[][] field) {
        for (Tile[] row : field) {
            for (Tile tile : row) {
                if (tile instanceof GoldenTile) {
                    GoldenTile goldenTile = (GoldenTile) tile;
                    goldenTile.increment();
                }
            }
        }
    }

    private void refreshShield(MutableHero hero, int currentTurn) {
        int maxShield = Math.min(1 << (currentLevel + 1), 1024);
        int shieldCD = (int) Math.max(10 - currentLevel / 3, 5);
        if (hero.getShield() == 0) {
            shieldCounter++;
        }
        if (shieldCounter > shieldCD) {
            hero.setVulnerable(false);
            hero.setShield(maxShield);
            shieldCounter = 0;
        }
    }

    private static boolean isOffCD(int currentTurn, int coolDown) {
        return currentTurn % coolDown == coolDown - 1;
    }

    @Override
    public int getCurrentLevel() {
        return currentLevel;
    }

}
