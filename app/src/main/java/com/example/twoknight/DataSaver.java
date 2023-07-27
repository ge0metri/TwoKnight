package com.example.twoknight;

import com.example.twoknight.framework.Game;
import com.example.twoknight.framework.Hero;
import com.example.twoknight.framework.ImmovableTile;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.tiles.StandardImmovableTile;
import com.example.twoknight.tiles.StandardTile;

public class DataSaver {
    public String[] saveField(Game game){
        Tile[][] field = game.getField();
        String[] s = new String[field.length*field[0].length];
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++){
                if (field[i][j] == null){
                    s[(i*field[j].length)+j] = "0";
                } else if (field[i][j] instanceof Hero) {
                    s[(i*field[j].length)+j] = "hero";
                } else if (field[i][j] instanceof ImmovableTile) {
                    s[(i * field[j].length) + j] = "I" + field[i][j].getValue();
                } else {
                    s[(i * field[j].length) + j] = "T" + field[i][j].getValue();
                }
            }
        }
        return s;
    }
    public Tile[][] loadField(String[] s, Hero hero){
        Tile[][] field = new Tile[4][4]; //TODO: not hardcode this
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++){
                int index = (i * field[j].length) + j;
                if (s[index].equals("0")){
                    field[i][j] = null;
                } else if (s[index].equals("hero")) {
                    field[i][j] = hero;
                } else if (s[index].contains("I")) {
                    field[i][j] = new StandardImmovableTile(Integer.parseInt(s[index].substring(1)));
                } else {
                    field[i][j] = new StandardTile(Integer.parseInt(s[index].substring(1)));
                }
            }
        }
        return field;
    }
}
