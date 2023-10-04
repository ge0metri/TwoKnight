package com.example.twoknight.strategy;

import com.example.twoknight.framework.MutableGame;
import com.example.twoknight.framework.MutableHero;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.tiles.GoldenTile;
import com.example.twoknight.tiles.StandardImmovableTile;

import java.util.Random;

public class StandardDifficultyHandler implements DifficultyHandler {

    private final int currentLevel;
    private boolean useSpawnRates = false;
    private int[] boughtSkills;
    private int shieldCounter = 0;
    private Random rand = new Random();

    public StandardDifficultyHandler(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public StandardDifficultyHandler(int currentLevel, int[] boughtSkills) {
        this.currentLevel = currentLevel;
        this.boughtSkills = boughtSkills;
        useSpawnRates = true;
    }

    @Override
    public void prepareNextRound(MutableGame game) {
        MutableHero hero = (MutableHero) game.getHero();
        int currentTurn = game.getTurnNumber();
        boolean blocksNotLaser = isBlocksNotLaser();
        if (currentTurn == 0) {
            game.addTile(new StandardImmovableTile());
            if (blocksNotLaser && currentLevel > 1) {
                game.addTile(new StandardImmovableTile(8));
                game.addTile(new StandardImmovableTile());
            }
        }
        if (game.getLaserState() != null) {
            //game.fireLaser();
        }
        int laserCD = 9 + currentLevel + game.checkTiles() / 2;
        if (!blocksNotLaser && currentLevel > 2 && isOffCD(currentTurn, laserCD)) {
            //game.beginLaser();
        }

        refreshShield(hero, currentTurn);

        int blockCD = 8 + currentLevel;
        if (blocksNotLaser && currentLevel > 2 && isOffCD(currentTurn, blockCD)) {
            game.addTile(new StandardImmovableTile(4));
        }
        incrementGoldenTile(game.getField());
    }
    @Override
    public boolean isBlocksNotLaser() {
        boolean blocksNotLaser = currentLevel % 2 == 0;
        return blocksNotLaser;
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
        int maxShield = getMaxShield();
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
    @Override
    public int getMaxShield() {
        return Math.min(1 << (int) (1.25 * (Math.sqrt(currentLevel) + 1)), 1024);
    }

    private static boolean isOffCD(int currentTurn, int coolDown) {
        return currentTurn % coolDown == coolDown - 1;
    }

    @Override
    public int getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public int getTileValue() {
        int val = rand.nextInt(10) == 0 ? 4 : 2;
        if (useSpawnRates){
            int rateUpgrade = boughtSkills[0]; // Defined as each one is 5% increase. Starts at 4 = 10% 2 = 90%.
            // Each increase goes 4 = 10 + 5*n %. until 4 = 50%. Then upgrade to 8 = 10, 4 = 90%
            // So the values are determined by each time 45 | n*5, i.e. 9 | n, the low val is n//9 and rate is n%9.
            int low = 1<<(1+ rateUpgrade/9);
            int rateHigh = 1 + rateUpgrade%9;
            val = rand.nextInt(20) <= rateHigh ? low*2 : low;
        }
        return val;
    }

}
