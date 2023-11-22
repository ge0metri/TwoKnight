/*
 * Copyright (C) 2022. Henrik Bærbak Christensen, Aarhus University.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.example.twoknight.standard;

import android.util.Log;

import com.example.twoknight.factory.GameFactory;
import com.example.twoknight.framework.GameListener;
import com.example.twoknight.framework.GameState;
import com.example.twoknight.framework.Hero;
import com.example.twoknight.framework.ImmovableTile;
import com.example.twoknight.framework.MutableGame;
import com.example.twoknight.framework.Observable;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.strategy.DifficultyHandler;
import com.example.twoknight.tiles.GoldenTile;
import com.example.twoknight.tiles.HealingTile;
import com.example.twoknight.tiles.StandardImmovableTile;
import com.example.twoknight.tiles.StandardTile;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class StandardGame implements MutableGame {
    Random rand = new Random();
    private int currentTurn;
    private final int side0;
    private final int side1;
    private final Tile[][] currentField;


    private final Hero hero;
    private final ArrayList<int[]> emptyMap = new ArrayList<>();
    private GameState gameState = GameState.RUNNING;

    private boolean checkingAvailableMoves = false;
    private boolean isCharging = false;
    private boolean rowNotCol;
    private int laserPosition;
    private final DifficultyHandler difficultyHandler;
    private int currentLevel = 1;
    private final int[] boughtSkills;
    private boolean debugMode;
    private int pointer = 0;
    private final PowerStrategy powerStrategy = new StandardPowerStrategy();
    private GameListener listener;

    public StandardGame(GameFactory gameFactory) {
        this.difficultyHandler = gameFactory.getDifficultyHandler();
        this.boughtSkills = gameFactory.getBoughtSkills();
        this.hero = gameFactory.buildHero();
        this.currentLevel = difficultyHandler.getCurrentLevel();
        side1 = GameConstants.side1;
        side0 = GameConstants.side0;
        currentField = new Tile[side0][side1];
        for (int i = 0; i < side0; i++) {
            for (int j = 0; j < side1; j++) {
                int[] e = {i, j};
                emptyMap.add(e);
            }
        }
        beginGame();
    }

    private void beginGame() {
        addRandomTile();
        addRandomTile();
        addHeroTile();
        difficultyHandler.prepareNextRound(this);
    }

    @Override
    public Hero getHero() {
        return hero;
    }


    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public int getTurnNumber() {
        if (gameState == GameState.POINTER) {
            return pointer;
        }
        return currentTurn;
    }

    @Override
    public int getLevel() {
        return currentLevel;
    }

    @Override
    public Tile[][] getField() {
        return currentField;
    }

    @Override
    public void endTurn(int e) {
        boolean isUsingPower = !powerStrategy.getPower().isEmpty();
        boolean moved = false;
        if (debugMode) {
            debugCode(e);
            debugMode = false;
        } else if (KeyEvent.VK_0 <= e && e <= KeyEvent.VK_9 && getGameState() != GameState.POINTER) {
            boolean powerSuccess = powerStrategy.prepareSkill(this, e);
            listener.onPowerUse(powerSuccess);
            if (powerSuccess){
                powerStrategy.endTurn(this);
            }
        } else if (gameState == GameState.POINTER) {
            handlePointer(e);
        } else {
            switch (e) {
                case KeyEvent.VK_ENTER: {
                    gameState = GameState.RUNNING;
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    moved = moveUp();
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    moved = moveDown();
                    break;
                }
                case KeyEvent.VK_UP: {
                    moved = moveLeft();
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    moved = moveRight();
                    break;
                }

                case KeyEvent.VK_D: {
                    debugMode = true;
                    break;
                }
            }
            if (!movesAvailable() && gameState == GameState.RUNNING) {
                gameState = GameState.LOSER;
                listener.onLevelCleared(this, gameState);
            }
            if (moved) {
                if (isUsingPower){
                    powerStrategy.endTurn(this);
                }
                difficultyHandler.prepareNextRound(this);
            }
        }
    }

    private void handlePointer(int e) {
        int x = pointer / side1;
        int y = pointer % side1;
        System.out.println(x + ", " + y);
        switch (e) {
            case (KeyEvent.VK_LEFT): {
                if (x != 0) {
                    pointer -= side1;
                } else {
                    pointer += (side0 - 1) * side1;
                }
                break;
            }
            case KeyEvent.VK_RIGHT: {
                if (x != side0 - 1) {
                    pointer += side1;
                } else {
                    pointer -= (side0 - 1) * side1;
                }
                break;
            }
            case KeyEvent.VK_UP: {
                if (y != 0) {
                    pointer--;
                } else {
                    pointer += side1 - 1;
                }
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (y != (side1) - 1) {
                    pointer++;
                } else {
                    pointer -= side1 - 1;
                }
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                powerStrategy.cancel(this);
                break;
            }
            case KeyEvent.VK_ENTER: {
                powerStrategy.useSkill(this, pointer);
                break;
            }
        }
    }


    private void debugCode(int e) {
        switch (e) {
            case KeyEvent.VK_1: {
                break;
            }
            case KeyEvent.VK_2: {
                addTile(new StandardImmovableTile());
                break;
            }
            case KeyEvent.VK_3: {
                int[] out = getRandomPos();
                if (out == null) return;
                currentField[out[0]][out[1]] = new StandardTile(128);
                emptyMap.remove(new int[]{out[0], out[1]});
                break;
            }
            case KeyEvent.VK_4: {
                addTile(new StandardImmovableTile(8));
                break;
            }
            case KeyEvent.VK_5: {
                addTile(new HealingTile(2));
                break;
            }
            case KeyEvent.VK_W: {
                gameState = GameState.WINNER;
                break;
            }
        }
    }

    @Override
    public int checkTiles() {
        int numberTiles = 0;
        for (Tile[] row : currentField) {
            for (Tile tile : row) {
                if (tile instanceof ImmovableTile) {
                } else if (tile != null) numberTiles += 1;
            }
        }
        return numberTiles;
    }


    @Override
    public int[] getBought() {
        return boughtSkills;
    }

    @Override
    public void setWinner(GameState who) {
        gameState = who;
    }

    @Override
    public void addRandomTile() {
        int[] out = getRandomPos();
        if (out == null) return;
        // int val = rand.nextInt(10) == 0 ? 4 : 2; Depricated
        int val = difficultyHandler.getTileValue();
        if (listener != null){listener.addTile(out);}
        currentField[out[0]][out[1]] = new StandardTile(val);
        emptyMap.remove(new int[]{out[0], out[1]});
    }

    private int[] getRandomPos() {
        int pos = rand.nextInt(side0 * side1);
        int row = 0, col = 0;
        for (int i = 0; i < side0 * side1; i++) {
            pos = (pos + 1) % (side0 * side1);
            row = pos / side1;
            col = pos % side1;
            if (currentField[row][col] == null) {
                break;
            }
        }
        if (currentField[row][col] != null) {
            return null;
        }
        int[] out = {row, col};
        return out;
    }

    private void addHeroTile() {
        int[] out = getRandomPos();
        if (out == null) return;
        currentField[out[0]][out[1]] = (Tile) hero;
        emptyMap.remove(new int[]{out[0], out[1]});
    }

    @Override
    public void addTile(Tile tile) {
        int[] out = getRandomPos();
        if (out == null) {
            return;
        }
        currentField[out[0]][out[1]] = tile;
        emptyMap.remove(new int[]{out[0], out[1]});
    }

    private void removeRandomLine(boolean rowNotCol, int side) {
        if (rowNotCol) {
            for (int i = 0; i < side1; i++) {
                if (!(currentField[side][i] instanceof Hero)) {
                    currentField[side][i] = null;
                }
            }
        } else {
            for (int i = 0; i < side0; i++) {
                if (!(currentField[i][side] instanceof Hero)) {
                    currentField[i][side] = null;
                }
            }
        }
    }

    @Override
    public void beginLaser(int i, int j) {
        if (!isCharging) {
            listener.onBeginLaser(i,j);
            isCharging = true;
        }
    }

    @Override
    public void fireLaser(int i, int j) {
        //removeRandomLine(rowNotCol, laserPosition);
        boolean skipLaser = powerStrategy.getPower().contains(GameConstants.PAUSE_POWER);
        listener.onFireLaser(i,j, skipLaser);
        isCharging = false;
        if (!skipLaser){
            currentField[i][j] = null;
            if (checkTiles() == 0 && gameState != GameState.WINNER) {
                gameState = GameState.LOSER;
                listener.onLevelCleared(this, gameState);
            }
        }
    }

    @Override
    public Laser getLaserState() {
        if (!isCharging) {
            return null;
        }
        return new Laser(rowNotCol, laserPosition);
    }

    @Override
    public Set<Integer> getPower() {
        return powerStrategy.getPower();
    }

    @Override
    public DifficultyHandler getDifficultyHandler() {
        return difficultyHandler;
    }

    @Override
    public PowerStrategy getPowerStrategy() {
        return powerStrategy;
    }

    boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }

    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;
        for (int i = 0; i < side0 * side1; i++) {
            int j = Math.abs(countDownFrom - i);

            int r = j / side1;
            int c = j % side1;

            if (currentField[r][c] == null || currentField[r][c] instanceof ImmovableTile) {
                continue;
            }

            int nextR = r + yIncr;
            int nextC = c + xIncr;
            boolean shouldAdd = false;
            if (currentField[r][c] instanceof StandardTile){
                shouldAdd = true;
            }
            int[] start = {r,c};
            int startValue = currentField[r][c].getValue();
            while (nextR >= 0 && nextR < side0 && nextC >= 0 && nextC < side1) {

                Tile next = currentField[nextR][nextC];
                Tile curr = currentField[r][c];

                if (next == null) {

                    if (checkingAvailableMoves)
                        return true;

                    currentField[nextR][nextC] = curr;
                    currentField[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;

                } else if (next.canMergeWith(curr)) {

                    if (checkingAvailableMoves)
                        return true;
                    if (curr instanceof GoldenTile && next instanceof StandardTile) {
                        currentField[nextR][nextC] = curr;
                        currentField[r][c] = null;
                        curr.mergeWith(next);
                    }
                    int value = next.mergeWith(curr);
                    boolean isShieldBlock = value == 0;
                    if (isShieldBlock) {
                        currentField[nextR][nextC] = null;
                    }
                    currentField[r][c] = null;
                    int[] e = {r, c};
                    emptyMap.remove(e);
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;
                    break;
                } else
                    break;
            }
            int[] end = {nextR-yIncr,nextC-xIncr};
            boolean startIsEnd = start[0] == end[0] && start[1] == end[1];
            boolean endIsStandard = currentField[end[0]][end[1]] instanceof StandardTile;
            if (shouldAdd && moved && !startIsEnd && endIsStandard){
                listener.onMove(start, end, startValue);
            }
        }

        if (moved) {
            currentTurn++;
            int health = getHero().getHealth();
            if (health > 0) {
                clearMerged();
            } else {
                gameState = GameState.WINNER;
                listener.onLevelCleared(this, gameState);
            }
        }
        return moved;
    }

    @Override
    public boolean moveUp() {
        return move(0, -1, 0);
    }

    @Override
    public boolean moveDown() {
        return move(side0 * side1 - 1, 1, 0);
    }

    @Override
    public boolean moveLeft() {
        return move(0, 0, -1);
    }

    @Override
    public boolean moveRight() {
        return move(side0 * side1 - 1, 0, 1);
    }

    @Override
    public void clearMerged() {
        for (Tile[] row : currentField)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }

    @Override
    public void skipLaser() {
        isCharging = false;
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
        ((Observable) hero).setGameListener(listener);
    }
    @Override
    public GameListener getGameListener() {
        return listener;
    }
}
