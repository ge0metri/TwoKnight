package com.example.twoknight.standard;

//import java.awt.event.KeyEvent;

import com.example.twoknight.errors.IllegalMove;
import com.example.twoknight.framework.GameState;
import com.example.twoknight.framework.Hero;
import com.example.twoknight.framework.ImmovableTile;
import com.example.twoknight.framework.MutableGame;
import com.example.twoknight.framework.MutableHero;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.tiles.StandardGoldenTile;
import com.example.twoknight.tiles.StandardTile;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class StandardPowerStrategy implements PowerStrategy {

    private int[] boughtSkills;
    private Set<Integer> usingSkill = new HashSet<>();
    private Tile tempTile = null;
    private Integer tempX = null;
    private Integer tempY = null;
    private int[] activeTurn = new int[]{0,0,0,0,0,0,0,0,0,0};

    public StandardPowerStrategy() {
    }

    @Override
    public boolean prepareSkill(MutableGame game, int e) {
        boughtSkills = game.getBought();
        int powerIndex = e; // - 48; for standard
        if (boughtSkills[powerIndex] <= 0) {
             return false;
         }
        switch (e) {
            case GameConstants.SPAWN_LUCK: {
                game.getDifficultyHandler().setUseSpawnRates(true);
                usingSkill.add(GameConstants.SPAWN_LUCK);
                activeTurn[GameConstants.SPAWN_LUCK] = game.getTurnNumber();
                break;
            }
            case KeyEvent.VK_1: {
                game.addTile(new StandardTile(128));
                // game.setWinner(Player.POINTER);
                // usingSkill = 1;
                break;
            }
            case KeyEvent.VK_2: {
                game.setWinner(GameState.POINTER);
                usingSkill.add(2);
                break;
            }
            case KeyEvent.VK_3: {
                game.setWinner(GameState.POINTER);
                usingSkill.add(3);
                break;
            }
            case 12: {
                game.setWinner(GameState.POINTER);
                usingSkill.add(4);
                break;
            }
            case 11: {
                game.addTile(new StandardGoldenTile());
                boughtSkills[(powerIndex + 9) % 10]--;
                break;
            }
            case KeyEvent.VK_6: {
                removeGreyTiles(game);
                boughtSkills[(powerIndex + 9) % 10]--;
                break;
            }
            case KeyEvent.VK_7: { // mystery power
                Random random = new Random();
                boughtSkills[random.nextInt(6)]++;
                boughtSkills[(powerIndex + 9) % 10]--;
                break;
            }
            case KeyEvent.VK_8: { // remove shield
                ((MutableHero) game.getHero()).setShield(0);
                break;
            }
            case KeyEvent.VK_9: { // skip laser
                if (game.getLaserState() != null) {
                    game.skipLaser();
                } else {
                    throw new RuntimeException(new IllegalMove());
                }
                break;
            }
            case GameConstants.CLEAR_POWER: { // vulnerable
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
                            field[iMax][jMax] = null;
                            iMax = i;
                            jMax = j;
                        } else {
                            field[i][j] = null;
                        }
                    }
                }
                boughtSkills[GameConstants.CLEAR_POWER]--;
                break;
                //((MutableHero) game.getHero()).setVulnerable(true);
            }
        }
        return true;
    }

    private void removeGreyTiles(MutableGame game) {
        for (int i = 0; i < game.getField().length; i++) {
            for (int j = 0; j < game.getField()[0].length; j++) {
                if (game.getField()[i][j] instanceof ImmovableTile && !(game.getField()[i][j] instanceof Hero)) {
                    game.getField()[i][j] = null;
                }
            }
        }
    }

    @Override
    public void useSkill(MutableGame game, int pointer) {
        int x = pointer / game.getField().length;
        int y = pointer % game.getField()[0].length;
        boolean illegalMove = false;
        int skill = 0;
        switch (skill) {
            case 1: {
                if (game.getField()[x][y] == null) {
                    game.getField()[x][y] = new StandardTile(128);
                    game.setWinner(GameState.RUNNING);
                    boughtSkills[1]--;
                    usingSkill = null;
                } else {
                    illegalMove = true;
                }
                break;
            }
            case 2: {
                if (game.getField()[x][y] instanceof StandardTile) {
                    StandardTile standardTile = (StandardTile) game.getField()[x][y];
                    game.getField()[x][y] = new StandardTile(standardTile.getValue() * 2);
                    game.setWinner(GameState.RUNNING);
                    boughtSkills[2]--;
                    usingSkill = null;
                } else {
                    illegalMove = true;
                }
                break;
            }
            case 3: {
                if (tempTile == null && game.getField()[x][y] instanceof StandardTile) {
                    StandardTile standardTile = (StandardTile) game.getField()[x][y];
                    tempTile = standardTile;
                    tempX = x;
                    tempY = y;
                } else if (game.getField()[x][y] instanceof StandardTile) {
                    StandardTile standardTile = (StandardTile) game.getField()[x][y];
                    game.getField()[x][y] = tempTile;
                    game.getField()[tempX][tempY] = standardTile;
                    boughtSkills[2]--;
                    tempX = null;
                    tempY = null;
                    tempTile = null;
                    usingSkill = null;
                    game.setWinner(GameState.RUNNING);
                } else {
                    illegalMove = true;
                }
                break;
            }
            case 4: {
                if (!(game.getField()[x][y] instanceof Hero)) {
                    game.getField()[x][y] = null;
                    game.setWinner(GameState.RUNNING);
                    boughtSkills[4]--;
                    usingSkill = null;
                } else {
                    illegalMove = true;
                }
                break;
            }
        }
        if (illegalMove) {
            throw new RuntimeException(new IllegalMove());
        }

    }

    @Override
    public void cancel(MutableGame game) {
        game.setWinner(GameState.RUNNING);
        tempX = null;
        tempY = null;
        tempTile = null;
        usingSkill = null;
    }

    @Override
    public Set<Integer> getPower() {
        return usingSkill;
    }

    @Override
    public void endTurn(MutableGame game) {
        boolean isSpawnLuckOver = game.getTurnNumber() > activeTurn[GameConstants.SPAWN_LUCK]
                + boughtSkills[GameConstants.SPAWN_LUCK_LENGTH]; //TODO: Add variable time. And visual?
        if (isSpawnLuckOver){
            game.getDifficultyHandler().setUseSpawnRates(false);
            usingSkill.remove(GameConstants.SPAWN_LUCK);
        }
    }
}
