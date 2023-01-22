package com.javohirbekcoder.puzzle15;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.javohirbekcoder.puzzle15.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Database database;
    private int timeUntill = 30;
    private int gameMode = 2;
    public static int wins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //region Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //endregion Ads

        binding.playGamebtn.setEnabled(true);


        binding.playGamebtn.setOnClickListener(v -> {
/*
            //add circular animation
            int x = binding.getRoot().getWidth() / 2;
            int y = binding.getRoot().getHeight() * 3 / 4;
            int startRadius = 0;
            int endRadius = Math.max(binding.getRoot().getWidth(), binding.getRoot().getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(binding.getRoot(), x,y,startRadius, endRadius).setDuration(500);
            binding.getRoot().setVisibility(View.VISIBLE);
            binding.playGamebtn.setEnabled(false);
            anim.start();

            //add wait until animation finished
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 500);*/
            Intent intent = null;
            switch (gameMode){
                case 1:
                    intent = new Intent(getApplicationContext(), GameActivity3x3.class);
                    break;
                case 2:
                    intent = new Intent(getApplicationContext(), GameActivity.class);
                    break;
            }
            intent.putExtra("timeUntill", timeUntill);
            startActivity(intent);
        });


        binding.settingsBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingsActivity.class)));
        binding.infoBtn.setOnClickListener(v -> {
            //startActivity(new Intent(getApplicationContext(), InfoActivity.class));
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            intent.putExtra("wins", wins);
            startActivity(intent);
        });
    }

    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).setTextColor(Color.parseColor("#ffffff")).setBackgroundTint(Color.parseColor("#00664A")).show();
    }

    private void loadDatabase() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        database = new Database(sharedPreferences);

        if (database.loadSettings()) {
            //showMessage("Loaded settings");
            setSettingtoGame();
        }else {
            showMessage("Setting are not available...");
        }
    }

    private void setSettingtoGame() {
        switch (database.getGameMode()){
            case 1:
                gameMode = 1;
                break;
            case 2:
                gameMode = 2;
                break;
            default:
                showMessage("Still null -> Game Mode");
        }
        switch (database.getDifficulty()){
            case "easy":
                timeUntill = 30;
                break;
            case "normal":
                timeUntill = 15;
                break;
            case "hard":
                timeUntill = 3;
                break;
            default:
                showMessage("Still null -> Difficulty");
        }
        wins = database.getWins();
        //Toast.makeText(this, "wins: " + wins, Toast.LENGTH_SHORT).show();
    }

    public void menuOpen(View view) {
        binding.asosiyLinear.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpa_pilus));
        binding.menuOpened.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_opening));
        binding.menuOpened.setVisibility(View.VISIBLE);
        binding.menuClosed.setVisibility(View.INVISIBLE);
    }

    public void menuClose(View view) {
        binding.asosiyLinear.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpa_minus));
        binding.menuOpened.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_closing));
        binding.menuOpened.setVisibility(View.INVISIBLE);
        binding.menuClosed.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.menuOpened.setVisibility(View.INVISIBLE);
        binding.menuClosed.setVisibility(View.VISIBLE);
        loadDatabase();
        binding.playGamebtn.setEnabled(true);
    }
}