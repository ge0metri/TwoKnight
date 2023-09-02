package com.example.twoknight;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.twoknight.framework.Game;
import com.example.twoknight.framework.Hero;
import com.example.twoknight.framework.ImmovableTile;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.tiles.StandardImmovableTile;
import com.example.twoknight.tiles.StandardTile;
import com.google.gson.Gson;

public class DataSaver {

    private static final String PREF_NAME = "GameData";
    private static final String KEY_CURRENT_LEVEL = "current_level";
    private static final String KEY_BOUGHT_ITEMS = "bought_items";

    private final SharedPreferences sharedPreferences;

    public DataSaver(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveCurrentLevel(int level) {
        sharedPreferences.edit().putInt(KEY_CURRENT_LEVEL, level).apply();
    }

    public int loadCurrentLevel() {
        return sharedPreferences.getInt(KEY_CURRENT_LEVEL, 1); // Default level 1
    }

    public void saveBoughtItems(int[] items) {
        Gson gson = new Gson();
        String itemsJson = gson.toJson(items);
        sharedPreferences.edit().putString(KEY_BOUGHT_ITEMS, itemsJson).apply();
    }

    public int[] loadBoughtItems() {
        String itemsJson = sharedPreferences.getString(KEY_BOUGHT_ITEMS, null);
        if (itemsJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(itemsJson, int[].class);
        } else {
            return new int[0]; // Default: No items bought
        }
    }






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
