package com.javohirbekcoder.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.javohirbekcoder.puzzle15.databinding.ActivityGameActivity3x3Binding;

import java.util.Arrays;
import java.util.Random;

public class GameActivity3x3 extends AppCompatActivity {

    private ActivityGameActivity3x3Binding binding;
    private final String arrayName = "savedTiles";

    private int moves = 0;
    private int emptyX = 2;
    private int emptyY = 2;

    private Button[][] buttons;
    private CountDownTimer timer;
    private int timeCount = 0;
    private int[] tiles;
    boolean isWin = false;
    private final Integer[] btnIds = {R.id.btn1, R.id.btn2,
            R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn0};

    private Dialog goBackDialog, winDialog, loseDialog;
    private boolean isTimerRunning = false;

    private int timerUntill = 30;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int bestRecordMoves;

    private boolean isFirstOpen = true;
    private String[] tilesArray;
    Database database;
    private AdView mAdView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameActivity3x3Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_game_activity3x3);

        //region Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView1 = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);
        //endregion Ads

        tiles = new int[16];
        timerUntill = getIntent().getIntExtra("timeUntill", 30);


        loadViews();
        loadNumbers();
        generateNumbers();
        loadDataToViews();
        //loadDialogs();
        //loadBestRecord();
        //loadArray();
    }

    private void generateNumbers() {
        int n = 8;
        Random random = new Random();
        while (n > 1) {
            int randomNum = random.nextInt(n--);
            int temp = tiles[randomNum];
            tiles[randomNum] = tiles[n];
            tiles[n] = temp;
        }
        //Log.d("Infos: ", "Tiles");
        loadDataToViews();
        if (!isSolvable())
            generateNumbers();
    }

    private void loadDataToViews() {
        for (int i = 0; i < 9; i++) {
            buttons[i / 3][i % 3].setText(String.valueOf(tiles[i]));
        }
        buttons[emptyX][emptyY].setText("");
    }

    private boolean isSolvable() {
        int countInversions = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    countInversions++;
            }
        }
        return countInversions % 2 == 0;
    }

    private void loadNumbers() {
        for (int i = 0; i < 9; i++) {
            tiles[i] = i + 1;
        }
    }

    private void loadViews() {
        buttons = new Button[3][3];
        for (int i = 0; i < 9; i++) {
            buttons[i / 3][i % 3] = (Button) findViewById(btnIds[i]);
            Log.d("Infos", " : " + i / 3 + i % 3);
        }
    }


    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).setTextColor(Color.parseColor("#ffffff")).setBackgroundTint(Color.parseColor("#00664A")).show();
    }

    public void btnClick(View view) {
        Button button = (Button) view;
        int x = button.getTag().toString().charAt(0) - '0';
        int y = button.getTag().toString().charAt(1) - '0';

        if ((Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x)) {
            buttons[emptyX][emptyY].setText(button.getText().toString());
            button.setText("");
            emptyX = x;
            emptyY = y;
            //checkWin();
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .repeat(0)
                    .playOn(findViewById(R.id.movesTv));
            moves++;
            binding.movesTv3.setText(String.valueOf(moves));
        }

        //saveMoves();
    }
}