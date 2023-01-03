package com.javohirbekcoder.puzzle15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.javohirbekcoder.puzzle15.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Database database;
    private int timeUntill = 30;
    private int gameMode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            intent.putExtra("timeUntill", timeUntill);
            startActivity(intent);
        });
        binding.settingsBtn.setOnClickListener(v ->{
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        });

        showMessage("Salom");
    }

    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).setTextColor(Color.parseColor("#ffffff")).setBackgroundTint(Color.parseColor("#00664A")).show();
    }

    private void loadDatabase() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        database = new Database(sharedPreferences);

        if (database.loadSettings()) {
            showMessage("Loaded settings");
            setSettingtoGame();
        }else {
            showMessage("Setting not loaded");
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
                Toast.makeText(this, "Still null -> Game Mode", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Still null -> Difficulty", Toast.LENGTH_SHORT).show();
                showMessage("Still null -> Difficulty");
        }
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
    }
}