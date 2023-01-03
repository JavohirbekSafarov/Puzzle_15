package com.javohirbekcoder.puzzle15;

import android.content.SharedPreferences;

/**
 * Created by Javohirbek on 03.01.2023.
 * My email: safarovjavohirbek3@gmail.com
 */
public class Database {
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


    public boolean saveDatas(){
        if (gameMode == 0 || difficulty.isEmpty())
            return false;
        else {
            editor.putInt("gameMode", gameMode);
            editor.putString("difficulty", difficulty);
            return editor.commit();
        }
    }

    public boolean loadSettings(){
        gameMode = sharedPreferences.getInt("gameMode", 0);
        difficulty = sharedPreferences.getString("difficulty", null);
        return difficulty != null;
    }
}
