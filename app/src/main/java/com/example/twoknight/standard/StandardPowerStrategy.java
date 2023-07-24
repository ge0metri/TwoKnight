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

import java.util.Random;

public class StandardPowerStrategy implements PowerStrategy {

    private int[] boughtSkills;
    private Integer usingSkill;
    private Tile tempTile = null;
    private Integer tempX = null;
    private Integer tempY = null;

    @Override
    public void prepareSkill(MutableGame game, int e) {
        boughtSkills = game.getBought();
        int pressedButton = e - 48;
        if (boughtSkills[(pressedButton + 9) % 10] <= 0) {
            return;
        }
        switch (e) {
            case KeyEvent.VK_1: {
                game.addTile(new StandardTile(128));
                // game.setWinner(Player.POINTER);
                // usingSkill = 1;
                break;
            }
            case KeyEvent.VK_2: {
                game.setWinner(GameState.POINTER);
                usingSkill = 2;
                break;
            }
            case KeyEvent.VK_3: {
                game.setWinner(GameState.POINTER);
                usingSkill = 3;
                break;
            }
            case KeyEvent.VK_4: {
                game.setWinner(GameState.POINTER);
                usingSkill = 4;
                break;
            }
            case KeyEvent.VK_5: {
                game.addTile(new StandardGoldenTile());
                boughtSkills[(pressedButton + 9) % 10]--;
                break;
            }
            case KeyEvent.VK_6: {
                removeGreyTiles(game);
                boughtSkills[(pressedButton + 9) % 10]--;
                break;
            }
            case KeyEvent.VK_7: { // mystery power
                Random random = new Random();
                boughtSkills[random.nextInt(6)]++;
                boughtSkills[(pressedButton + 9) % 10]--;
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
            case KeyEvent.VK_0: { // vulnerable
                ((MutableHero) game.getHero()).setVulnerable(true);
            }
        }
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
        switch (usingSkill) {
            case 1: {
                if (game.getField()[x][y] == null) {
                    game.getField()[x][y] = new StandardTile(128);
                    game.setWinner(GameState.RUNNING);
                    boughtSkills[(usingSkill + 9) % 10]--;
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
                    boughtSkills[(usingSkill + 9) % 10]--;
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
                    boughtSkills[(usingSkill + 9) % 10]--;
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
                    boughtSkills[(usingSkill + 9) % 10]--;
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
    public int getPower() {
        return usingSkill;
    }
}
