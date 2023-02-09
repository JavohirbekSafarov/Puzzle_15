package com.javohirbekcoder.puzzle15;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.javohirbekcoder.puzzle15.databinding.ActivityGameActivity3x3Binding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameActivity3x3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tiles = new int[16];
        timerUntill = getIntent().getIntExtra("timeUntill", 30);

        binding.shuffleBtn3.setOnClickListener(v -> {
            resetAll();
            bannerAdsLoadAndShow();
            binding.shuffleBtn3.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_shuffle));
        });

        binding.goBackbtn.setOnClickListener(v -> onBackPressed());


        bannerAdsLoadAndShow();
        loadViews();
        loadNumbers();
        generateNumbers();
        loadDataToViews();
        loadTimer(timerUntill);
        loadDialogs();
        //loadBestRecord();
        //loadArray();
    }

    private void bannerAdsLoadAndShow() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView3 = findViewById(R.id.adView3);
        @SuppressLint("VisibleForTests")
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);
    }

    private void loadDialogs() {
        winDialog = new Dialog(GameActivity3x3.this);
        winDialog.setCancelable(false);
        winDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        goBackDialog = new Dialog(GameActivity3x3.this);
        goBackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        loseDialog = new Dialog(GameActivity3x3.this);
        loseDialog.setCancelable(false);
        loseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
            buttons[i / 3][i % 3].setBackgroundColor(Color.parseColor("#2C7DC2"));
        }
        buttons[emptyX][emptyY].setText("");
        buttons[emptyX][emptyY].setBackgroundColor(Color.parseColor("#00000000"));
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

    private void loadTimer(int timeMinutes) {
        timer = new CountDownTimer((long) timeMinutes * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;
                timeCount++;
                int second = timeCount % 60;
                int minute = timeCount / 60;
                YoYo.with(Techniques.Pulse)
                        .duration(700)
                        .repeat(0)
                        .playOn(findViewById(R.id.timeTv3));
                binding.timeTv3.setText(String.format("%02d:%02d", minute, second));
            }

            @Override
            public void onFinish() {
                if (!isWin) {
                    isTimerRunning = false;
                    callLoseDialog();
                }
            }
        }.start();
    }

    private void callLoseDialog() {
        loseDialog.setContentView(R.layout.lose_dialog);
        Button homebtn = loseDialog.findViewById(R.id.homeBtn);
        Button newgamebtn = loseDialog.findViewById(R.id.newGameBtn);
        homebtn.setOnClickListener(v -> super.onBackPressed());
        newgamebtn.setOnClickListener(v -> {
            resetAll();
            //saveMoves();
            loseDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), GameActivity3x3.class);
            intent.putExtra("timeUntill", timerUntill);
            startActivity(intent);
            finish();
        });
        loseDialog.show();
    }

    public void btnClick(View view) {
        Button button = (Button) view;
        int x = button.getTag().toString().charAt(0) - '0';
        int y = button.getTag().toString().charAt(1) - '0';

        if ((Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x)) {
            buttons[emptyX][emptyY].setText(button.getText().toString());
            buttons[emptyX][emptyY].setBackgroundColor(Color.parseColor("#2C7DC2"));
            button.setText("");
            button.setBackgroundColor(Color.parseColor("#00000000"));
            emptyX = x;
            emptyY = y;
            checkWin();
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .repeat(0)
                    .playOn(findViewById(R.id.movesTv3));
            moves++;
            binding.movesTv3.setText(String.valueOf(moves));
        }

        //saveMoves();
    }

    @SuppressLint("SetTextI18n")
    private void callWinDialog(int moves, String time) {
        winDialog.setContentView(R.layout.win_dialog);
        Button home = winDialog.findViewById(R.id.homeBtn);
        Button newgame = winDialog.findViewById(R.id.newGameBtn);
        TextView timeTvDialog = winDialog.findViewById(R.id.timeTvDialog);
        TextView movesTvDialod = winDialog.findViewById(R.id.movesTvDialog);
        timeTvDialog.setText("Time: " + time);
        movesTvDialod.setText("Moves: " + (moves + 1));
        winDialog.show();
        home.setOnClickListener(v -> {
            resetAll();
            super.onBackPressed();
        });
        newgame.setOnClickListener(v -> {
            resetAll();
            //saveMoves();
            winDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), GameActivity3x3.class);
            intent.putExtra("timeUntill", timerUntill);
            startActivity(intent);
            finish();
        });
    }

    private void checkWin() {
        isWin = false;
        if (emptyX == 2 && emptyY == 2) {
            for (int a = 0; a < 8; a++) {
                if (buttons[a / 3][a % 3].getText().toString().equals(String.valueOf(a + 1))){
                    isWin = true;
                }
                else {
                    isWin = false;
                    break;
                }
            }
        }

        if (isWin) {

            //saveWins();
           /* if (bestRecordMoves > moves) {
                int movesOrginal = moves + 1;
                editor.putInt("recordMoves", movesOrginal);
                editor.apply();
            }*/
            timer.cancel();
            callWinDialog(moves, binding.timeTv3.getText().toString());
        }
    }

    private void resetAll() {
        emptyX = 2;
        emptyY = 2;
        timeCount = 0;
        moves = 0;
        loadNumbers();
        generateNumbers();
        timer.cancel();
        loadTimer(timerUntill);
        binding.movesTv3.setText(String.valueOf(moves));
        buttons[emptyX][emptyY].setBackgroundColor(Color.parseColor("#00000000"));
        //saveMoves();
    }

    @Override
    protected void onPause() {
        timer.cancel();
        isTimerRunning = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (!isTimerRunning)
            timer.start();
        super.onResume();

    }
}