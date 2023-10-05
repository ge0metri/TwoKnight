package com.example.twoknight.variants;

//import java.awt.event.KeyEvent;

import com.example.twoknight.errors.InsufficientFunds;
import com.example.twoknight.framework.GameListener;
import com.example.twoknight.framework.GameState;
import com.example.twoknight.framework.Hero;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.standard.GameConstants;
import com.example.twoknight.standard.KeyEvent;
import com.example.twoknight.standard.Laser;
import com.example.twoknight.strategy.DifficultyHandler;
import com.example.twoknight.tiles.StandardImmovableTile;
import com.example.twoknight.tiles.StandardShopTile;

import java.util.Collections;
import java.util.Set;

public class ShopGame implements Shop {
    Tile[][] tiles = new Tile[GameConstants.side0][GameConstants.side1];
    int side0 = tiles.length;
    int side1 = tiles[0].length;
    private int pointer = 0;
    private GameState state = GameState.SHOP;
    private final int[] boughtSkills = new int[side0 * side1];
    private int balance = 0;

    public ShopGame() {
        fillField(tiles);
    }

    @Override
    public Hero getHero() {
        return null;
    }

    @Override
    public GameState getGameState() {
        return state;
    }

    @Override
    public int getTurnNumber() {
        // Return pointer here. Not pretty.
        return pointer;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public Tile[][] getField() {
        return tiles;
    }

    @Override
    public void endTurn(int e) {
        int x = pointer / side1;
        int y = pointer % side1;
        switch (e) {
            case KeyEvent.VK_LEFT: {
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
            case KeyEvent.VK_Q: {
                balance++;
                break;
            }
            case KeyEvent.VK_ENTER: {
                try {
                    buyTile(pointer);
                } catch (InsufficientFunds ex) {
                    System.out.println(ex);
                }
                break;
            }
        }

    }

    private void buyTile(int pointer) throws InsufficientFunds {
        int x = pointer / side1;
        int y = pointer % side1;
        if (x == side0 - 1 && y == side1 - 1) {
            state = GameState.DONE;
            return;
        }
        Tile item = tiles[x][y];
        if (item == null) {
        } else if (balance >= item.getValue()) {
            balance -= tiles[x][y].getValue();
            boughtSkills[pointer]++;
        } else {
            throw new InsufficientFunds();
        }
    }

    private void fillField(Tile[][] tiles) {
        for (int i = 0; i < 10; i++) {
            tiles[i / side1][i % side1] = new StandardShopTile(GameConstants.powerDescription[(i + 1) % 10], "[" + (i + 1) % 10 + "]", GameConstants.powerCost[(i + 1) % 10]);
        }
        tiles[side0 - 1][side1 - 1] = new StandardImmovableTile();
    }

    @Override
    public Laser getLaserState() {
        return null;
    }

    @Override
    public Set<Integer> getPower() {
        return Collections.singleton(0);
    }

    @Override
    public DifficultyHandler getDifficultyHandler() {
        return null;
    }

    @Override
    public int[] getBought() {
        return boughtSkills;
    }

    @Override
    public void nextShop(int currentLevel, int[] boughtSkills) {
        pointer = 0;
        state = GameState.SHOP;
        balance *= 2; // stonks
        balance += currentLevel - 1;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public void setGameListener(GameListener listener) {

    }
}
