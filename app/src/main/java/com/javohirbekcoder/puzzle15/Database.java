package com.javohirbekcoder.puzzle15;

import android.content.SharedPreferences;

/**
 * Created by Javohirbek on 03.01.2023.
 * My email: safarovjavohirbek3@gmail.com
 */
public class Database {
    private int wins = 0;
    private static int gameMode = 0;
    private static String difficulty;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Database(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public boolean saveDatas(){
        if (gameMode == 0 || difficulty.isEmpty())
            return false;
        else {
            editor.putInt("gameMode", gameMode);
            editor.putString("difficulty", difficulty);
            return editor.commit();
        }
    }

    public void saveWins(){
        editor.putInt("wins", wins);
        editor.commit();
    }

    public boolean loadSettings(){
        wins = sharedPreferences.getInt("wins", 0);
        gameMode = sharedPreferences.getInt("gameMode", 0);
        difficulty = sharedPreferences.getString("difficulty", null);
        return difficulty != null;
    }
}
