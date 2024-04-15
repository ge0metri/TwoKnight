package com.example.twoknight.variants;

//import java.awt.event.KeyEvent;

import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.Game;
import com.example.twoknight.framework.GameListener;
import com.example.twoknight.framework.GameState;
import com.example.twoknight.framework.Hero;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.standard.KeyEvent;
import com.example.twoknight.standard.Laser;
import com.example.twoknight.standard.PowerStrategy;
import com.example.twoknight.standard.StandardGame;
import com.example.twoknight.strategy.DifficultyHandler;

import java.util.Set;

public class AlternatingGame implements Game, Shop {

    private Game game;
    private final Shop shop;
    private Game alternatingGame;
    private int currentLevel = 1;

    public AlternatingGame() {
        this.shop = new ShopGame();
        this.game = new StandardGame(new StandardGameFactory(currentLevel, shop.getBought()));
        alternatingGame = game;
    }

    @Override
    public Hero getHero() {
        return alternatingGame.getHero();
    }

    @Override
    public GameState getGameState() {
        return alternatingGame.getGameState();
    }

    @Override
    public int getTurnNumber() {
        return alternatingGame.getTurnNumber();
    }

    @Override
    public int getLevel() {
        return currentLevel;
    }

    @Override
    public Tile[][] getField() {
        return alternatingGame.getField();
    }

    @Override
    public void endTurn(int e) {
        GameState state = alternatingGame.getGameState();
        boolean isRunning = state == GameState.SHOP || state == GameState.RUNNING || state == GameState.BEGIN;
        if ((state == GameState.WINNER) && e == KeyEvent.VK_ENTER) {
            currentLevel++;
            alternatingGame = shop;
            shop.nextShop(currentLevel, game.getBought());
        } else if (state == GameState.LOSER && e == KeyEvent.VK_ENTER) {
            game = new StandardGame(new StandardGameFactory(currentLevel, shop.getBought()));
            alternatingGame = game;
        } else {
            alternatingGame.endTurn(e);
        }
        if (alternatingGame.getGameState() == GameState.DONE) {
            int[] boughtSkills = alternatingGame.getBought();
            game = new StandardGame(new StandardGameFactory(currentLevel, boughtSkills));
            alternatingGame = game;
        }
    }

    @Override
    public int[] getBought() {
        return alternatingGame.getBought();
    }

    @Override
    public Laser getLaserState() {
        return alternatingGame.getLaserState();
    }

    @Override
    public Set<Integer> getPower() {
        return game.getPower();
    }

    @Override
    public DifficultyHandler getDifficultyHandler() {
        return null;
    }

    @Override
    public PowerStrategy getPowerStrategy() {
        return null;
    }

    @Override
    public void nextShop(int currentLevel, int[] boughtSkills) {
        shop.nextShop(currentLevel, boughtSkills);
    }

    @Override
    public int getBalance() {
        return shop.getBalance();
    }

    @Override
    public void setGameListener(GameListener listener) {

    }
}
