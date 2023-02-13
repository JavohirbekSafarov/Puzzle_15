package com.javohirbekcoder.puzzle15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

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

        getWindow().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding.playGamebtn.setEnabled(true);


        binding.playGamebtn.setOnClickListener(v -> {
            Intent intent = null;
            switch (gameMode){
                case 1:
                    intent = new Intent(getApplicationContext(), GameActivity3x3.class);
                    break;
                case 2:
                    intent = new Intent(getApplicationContext(), GameActivity.class);
                    break;
            }
            assert intent != null;
            intent.putExtra("timeUntill", timeUntill);
            startActivity(intent);
        });


        binding.settingsBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingsActivity.class)));
        binding.infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            intent.putExtra("wins", wins);
            startActivity(intent);
        });
    }

    private void loadDatabase() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("settings", MODE_PRIVATE);
        database = new Database(sharedPreferences);

        if (database.loadSettings()) {
            setSettingtoGame();
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