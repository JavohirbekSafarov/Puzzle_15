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
    public static boolean isVibratorOn = true;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public Database(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public int getGameMode() {
        return gameMode;
    }

    public boolean isVibratorOn() {
        return isVibratorOn;
    }

    public void setVibratorOn(boolean vibratorOn) {
        isVibratorOn = vibratorOn;
    }

    public void setGameMode(int gameMode) {
        Database.gameMode = gameMode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        Database.difficulty = difficulty;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public boolean saveDatas() {
        if (gameMode == 0 || difficulty.isEmpty())
            return false;
        else {
            editor.putInt("gameMode", gameMode);
            editor.putString("difficulty", difficulty);
            editor.putBoolean("vibrator", isVibratorOn);
            editor.apply();
            return true;
        }
    }

    public void saveWins() {
        editor.putInt("wins", wins);
        editor.apply();
    }

    public boolean loadSettings() {
        wins = sharedPreferences.getInt("wins", 0);
        gameMode = sharedPreferences.getInt("gameMode", 0);
        difficulty = sharedPreferences.getString("difficulty", null);
        isVibratorOn = sharedPreferences.getBoolean("vibrator", true);
        return difficulty != null;
    }
}
