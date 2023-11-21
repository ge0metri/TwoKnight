package com.example.twoknight.strategy;

import com.example.twoknight.framework.Hero;
import com.example.twoknight.framework.ImmovableTile;
import com.example.twoknight.framework.MutableGame;
import com.example.twoknight.framework.MutableHero;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.standard.GameConstants;
import com.example.twoknight.tiles.GoldenTile;
import com.example.twoknight.tiles.StandardImmovableTile;

import java.util.Random;

public class StandardDifficultyHandler implements DifficultyHandler {

    private final int currentLevel;


    private boolean useSpawnRates = false;
    private int[] boughtSkills;


    private int shieldCounter = 0;
    private Random rand = new Random();
    private int chargeTime = -1;
    int[] laserPosition;


    private final int shieldCD;

    public StandardDifficultyHandler(int currentLevel) {
        shieldCD = (int) Math.max(10 - currentLevel / 3, 5);
        this.currentLevel = currentLevel;
    }

    public StandardDifficultyHandler(int currentLevel, int[] boughtSkills) {
        shieldCD = (int) Math.max(10 - currentLevel / 3, 5);
        this.currentLevel = currentLevel;
        this.boughtSkills = boughtSkills;
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
        if(!blocksNotLaser){
            handleLaser(game);
        }

        refreshShield(hero, currentTurn);

        int blockCD = 8 + currentLevel;
        if (blocksNotLaser && currentLevel > 2 && isOffCD(currentTurn, blockCD)) {
            game.addTile(new StandardImmovableTile(4));
        }
        incrementGoldenTile(game.getField());
        game.addRandomTile();
    }

    private void handleLaser(MutableGame game) {
        int currentTurn = game.getTurnNumber();
        if (game.getLaserState() != null) {
            if (chargeTime == 0){
                game.fireLaser(laserPosition[0], laserPosition[1]);
            }
            chargeTime--;
        }
        int laserCD = 9 + currentLevel + game.checkTiles() / 2;
        if (currentLevel > 2 && isOffCD(currentTurn, laserCD)) {
            laserPosition = findMaxTile(game);
            game.beginLaser(laserPosition[0], laserPosition[1]);
            chargeTime = 2;
        }
    }

    private int[] findMaxTile(MutableGame game) {
        Tile[][] field = game.getField();
        int iMax = 0;
        int jMax = 0;
        int Max = 0;
        Tile firstTile = field[0][0];
        if (firstTile != null && !(firstTile instanceof ImmovableTile)){
            Max = firstTile.getValue();
        }
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == null || field[i][j] instanceof Hero){continue;}
                if (i == 0 && j == 0){continue;}
                if (field[i][j].getValue() > Max){
                    Max = field[i][j].getValue();
                    iMax = i;
                    jMax = j;
                }
            }
        }
        return new int[]{iMax, jMax};
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
    @Override
    public int getChargeTime() {
        return chargeTime;
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
            int rateUpgrade = boughtSkills[GameConstants.SPAWN_LUCK]; // Defined as each one is 5% increase. Starts at 4 = 10% 2 = 90%.
            // Each increase goes 4 = 10 + 5*n %. until 4 = 50%. Then upgrade to 8 = 10, 4 = 90%
            // So the values are determined by each time 45 | n*5, i.e. 9 | n, the low val is n//9 and rate is n%9.
            int low = 1<<(1+ rateUpgrade/9);
            int rateHigh = 1 + rateUpgrade%9;
            val = rand.nextInt(20) <= rateHigh ? low*2 : low;
        }
        return val;
    }
    @Override
    public boolean isUseSpawnRates() {
        return useSpawnRates;
    }

    @Override
    public void setUseSpawnRates(boolean useSpawnRates) {
        this.useSpawnRates = useSpawnRates;
    }
    @Override
    public int getShieldCounter() {
        return shieldCounter;
    }
    @Override
    public int getShieldCD() {
        return shieldCD;
    }


}
