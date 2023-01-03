package com.javohirbekcoder.puzzle15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.javohirbekcoder.puzzle15.databinding.ActivitySettingsBinding;

import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadComponents();

        binding.saveFB.setOnClickListener(v -> {
            database.setDifficulty(loadDifficulty());
            database.setGameMode(loadGameMode());
            if (database.saveDatas())
                showMessage("Succesfully saved!");
            else
                showMessage("Failed saving!");
        });

        showMessage("salom olam");
    }

    private void showMessage(String message) {
        binding.saveFB.setTranslationY(-130);
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).setTextColor(Color.parseColor("#ffffff")).setBackgroundTint(Color.parseColor("#00664A")).show();
        // sleep qoyish kk
        binding.saveFB.setTranslationY(0);
    }

    private void setDefaultValues() {
        switch (database.getGameMode()){
            case 1:
                binding.threeRB.setChecked(true);
                break;
            case 2:
                binding.fourRB.setChecked(true);
                break;
            default:
                Toast.makeText(this, "Still null -> Game Mode", Toast.LENGTH_SHORT).show();
        }
        switch (database.getDifficulty()){
            case "easy":
                binding.easyRB.setChecked(true);
                break;
            case "normal":
                binding.normalRB.setChecked(true);
                break;
            case "hard":
                binding.hardRB.setChecked(true);
                break;
            default:
                Toast.makeText(this, "Still null -> Difficulty", Toast.LENGTH_SHORT).show();
        }
    }

    private int loadGameMode() {
        int ids[] = {
                R.id.threeRB,
                R.id.fourRB
        };
        int i = 0;
        for (i = 0; i < 2; i++) {
            RadioButton radioButton =  findViewById(ids[i]);
            if (radioButton.isChecked()) {
                break;
            }
        }
        switch (i){
            case 0:
                return 1;
            case 1:
                return 2;
        }
        return 0;
    }

    private String loadDifficulty() {
        int ids[] = {
                R.id.easyRB,
                R.id.normalRB,
                R.id.hardRB
        };
        int i = 0;
        for (i = 0; i < 3; i++) {
            RadioButton radioButton =  findViewById(ids[i]);
            if (radioButton.isChecked()) {
                break;
            }
        }
        switch (i){
            case 0:
                return "easy";
            case 1:
                return "normal";
            case 2:
                return "hard";
        }
        return "";
    }

    private void loadComponents() {
        sharedPreferences = this.getSharedPreferences("settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        database = new Database(sharedPreferences);

        if (database.loadSettings()) {
            showMessage("Loaded settings");
            setDefaultValues();
        }else
            showMessage("Settings not loaded!");
    }
}