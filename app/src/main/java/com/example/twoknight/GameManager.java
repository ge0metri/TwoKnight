package com.example.twoknight;

import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.Game;
import com.example.twoknight.standard.StandardGame;
import com.example.twoknight.variants.AlternatingGame;

public class GameManager {
    private static GameManager instance;
    private Game game;

    private GameManager() {
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }


    public Game getGame() {
        return game;
    }
    public Game setGame(int currentLevel, int[] boughtPower) {
        return game = new StandardGame(new StandardGameFactory(currentLevel, boughtPower));
    }

    public Game setGame(int currentLevel) {
        //Depricated method
        return game = new StandardGame(new StandardGameFactory(currentLevel));
    }

    public void deleteGame() {
        game = null;
    }
}