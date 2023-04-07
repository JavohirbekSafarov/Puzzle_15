package com.javohirbekcoder.puzzle15;

import static com.javohirbekcoder.puzzle15.Database.isVibratorOn;
import static com.javohirbekcoder.puzzle15.MainActivity.wins;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.javohirbekcoder.puzzle15.databinding.ActivityGameActivity3x3Binding;

import java.util.Objects;
import java.util.Random;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class GameActivity3x3 extends AppCompatActivity {

    private ActivityGameActivity3x3Binding binding;
    private final String arrayName = "savedTiles";
    private InterstitialAd mInterstitialAd;

    private int moves = 0;
    private int emptyX = 2;
    private int emptyY = 2;

    private Button[][] buttons;
    private CountDownTimer timer;
    private Handler handler;
    private int timeCount = 0;
    private int[] tiles;
    boolean isWin = false;
    private final Integer[] btnIds = {R.id.btn1, R.id.btn2,
            R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn0};

    private Dialog winDialog, loseDialog;
    private boolean isTimerRunning = false;

    private int timerUntil = 30;
    private SharedPreferences.Editor editor;
    private int bestRecordMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameActivity3x3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tiles = new int[16];
        timerUntil = getIntent().getIntExtra("timeUntill", 30);
        handler = new Handler();

        binding.shuffleBtn3.setOnClickListener(v -> {
            if (isVibratorOn)
                vibration();
            resetAll();
            bannerAdsLoadAndShow();
            binding.shuffleBtn3.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_shuffle));
        });

        binding.goBackbtn.setOnClickListener(v -> onBackPressed());

        bannerAdsLoadAndShow();
        loadInterstitialAd();
        loadViews();
        loadNumbers();
        generateNumbers();
        loadDataToViews();
        loadDialogs();
        loadBestRecord();
        loadArray();
    }

    private void bannerAdsLoadAndShow() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView3 = findViewById(R.id.adView3);
        @SuppressLint("VisibleForTests")
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);
    }

    //todo app open ad qoshish, timer teskari sanash
    public void loadArray() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("saveTiles3", MODE_PRIVATE);

        boolean isFirstOpen = sharedPreferences.getBoolean("isFirstOpen", true);

        if (!isFirstOpen) {
            moves = sharedPreferences.getInt("moves", 0);
            binding.movesTv3.setText(String.valueOf(moves));

            timeCount = sharedPreferences.getInt("time", 0);


            for (int i = 0; i < 9; i++) {
                if (sharedPreferences.getString(arrayName + "_" + i, "").equals("0")) {
                    emptyX = i / 3;
                    emptyY = i % 3;
                }
                tiles[i] = Integer.parseInt(sharedPreferences.getString(arrayName + "_" + i, ""));
            }
            loadDataToViews();
        }
        loadTimer(timerUntil);
    }

    @SuppressLint("SetTextI18n")
    private void loadBestRecord() {
        SharedPreferences sharedPreferences = getSharedPreferences("records", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bestRecordMoves = sharedPreferences.getInt("recordMoves3", 99999999);
        if (bestRecordMoves == 99999999)
            binding.recordTV3.setText("You have not best record!");
        else
            binding.recordTV3.setText("Best record: " + bestRecordMoves);
    }

    private void loadDialogs() {
        winDialog = new Dialog(GameActivity3x3.this);
        winDialog.setCancelable(false);
        winDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
            @SuppressLint("DefaultLocale")
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
        };
    }

    private void callLoseDialog() {
        loseDialog.setContentView(R.layout.lose_dialog);
        Button homebtn = loseDialog.findViewById(R.id.homeBtn);
        Button newgamebtn = loseDialog.findViewById(R.id.newGameBtn);
        homebtn.setOnClickListener(v -> {
            super.onBackPressed();
            resetAll();
            handler = null;
        });
        newgamebtn.setOnClickListener(v -> {
            resetAll();
            saveMoves();
            handler = null;
            loseDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), GameActivity3x3.class);
            intent.putExtra("timeUntill", timerUntil);
            startActivity(intent);
            finish();
        });
        loseDialog.show();
    }

    private void saveMoves() {
        String[] tilesArray = new String[9];
        for (int i = 0; i < 9; i++) {
            Button btn = (Button) findViewById(btnIds[i]);
            if (btn.getText() == null || btn.getText() == "") {
                tilesArray[i] = "0";
            } else
                tilesArray[i] = String.valueOf(btn.getText());
        }
        saveArray(tilesArray);
    }

    public void saveArray(String[] array) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("saveTiles3", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < array.length; i++) {
            editor.putString(arrayName + "_" + i, array[i]);
        }
        editor.putInt("time", timeCount);
        editor.putInt("moves", moves);
        editor.putBoolean("isFirstOpen", false);
        editor.apply();
    }

    public void btnClick(View view) {
        Button button = (Button) view;
        int x = button.getTag().toString().charAt(0) - '0';
        int y = button.getTag().toString().charAt(1) - '0';

        if (isVibratorOn)
            vibration();

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

        saveMoves();
    }

    @SuppressLint("SetTextI18n")
    private void callWinDialog(int moves, String time) {
        winDialog.setContentView(R.layout.win_dialog);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(Objects.requireNonNull(AppCompatResources.getDrawable(this, R.drawable.cup)), true);
        EmitterConfig emitterConfig = new Emitter(300, MILLISECONDS).max(300);
        KonfettiView konfettiView = winDialog.findViewById(R.id.konfettiview);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .shapes(Shape.Circle.INSTANCE, Shape.Square.INSTANCE, drawableShape)
                        .spread(360)
                        .position(0, 0.5, 1, 1)
                        .sizes(new Size(8, 50, 10))
                        .timeToLive(3000)
                        .fadeOutEnabled(true)
                        .build()
        );
        Button home = winDialog.findViewById(R.id.homeBtn);
        Button newgame = winDialog.findViewById(R.id.newGameBtn);
        TextView timeTvDialog = winDialog.findViewById(R.id.timeTvDialog);
        TextView movesTvDialod = winDialog.findViewById(R.id.movesTvDialog);
        timeTvDialog.setText("Time: " + time);
        movesTvDialod.setText("Moves: " + (moves + 1));
        winDialog.show();
        home.setOnClickListener(v -> {
            resetAll();
            handler = null;
            super.onBackPressed();
        });
        newgame.setOnClickListener(v -> {
            resetAll();
            saveMoves();
            winDialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), GameActivity3x3.class);
            intent.putExtra("timeUntill", timerUntil);
            handler = null;
            startActivity(intent);
            finish();
        });
    }

    private void checkWin() {
        isWin = false;
        if (emptyX == 2 && emptyY == 2) {
            for (int a = 0; a < 8; a++) {
                if (buttons[a / 3][a % 3].getText().toString().equals(String.valueOf(a + 1))) {
                    isWin = true;
                } else {
                    isWin = false;
                    break;
                }
            }
        }

        if (isWin) {

            saveWins();
            if (bestRecordMoves > moves) {
                int movesOrginal = moves + 1;
                editor.putInt("recordMoves3", movesOrginal);
                editor.apply();
            }
            timer.cancel();
            callWinDialog(moves, binding.timeTv3.getText().toString());
        }
    }

    private void saveWins() {
        SharedPreferences prefsDatabase = this.getSharedPreferences("settings", MODE_PRIVATE);

        Database database = new Database(prefsDatabase);
        database.loadSettings();
        wins = database.getWins();
        wins++;
        database.setWins(wins);
        database.saveWins();

        showInterstitialAd();
        //loadInterstitialAd();
    }

    private void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(GameActivity3x3.this);
        }
    }

    private void loadInterstitialAd() {

        String TAG = "AD-Interstitial";

        AdRequest adRequest = new AdRequest.Builder().build();

        MobileAds.initialize(this, initializationStatus -> {
        });

        InterstitialAd.load(this, "ca-app-pub-8399176622985245/8029199454", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                        if (handler != null)
                            handler.postDelayed(() -> {
                                loadInterstitialAd();
                            }, 10000);
                    }
                });
    }

    private void resetAll() {
        emptyX = 2;
        emptyY = 2;
        timeCount = 0;
        moves = 0;
        loadNumbers();
        generateNumbers();
        loadTimer(timerUntil);
        binding.movesTv3.setText(String.valueOf(moves));
        buttons[emptyX][emptyY].setBackgroundColor(Color.parseColor("#00000000"));
        saveMoves();
    }

    @Override
    protected void onPause() {
        timer.cancel();
        isTimerRunning = false;
        handler = null;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (!isTimerRunning)
            timer.start();
        handler = new Handler();
        super.onResume();
    }

    private void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(5, VibrationEffect.DEFAULT_AMPLITUDE));

        } else {
            //deprecated in API 26
            v.vibrate(5);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }
}