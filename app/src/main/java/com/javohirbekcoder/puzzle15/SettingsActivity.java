package com.javohirbekcoder.puzzle15;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.javohirbekcoder.puzzle15.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadComponentsSettings();

        binding.saveFB.setOnClickListener(v -> {
            database.setDifficulty(loadDifficulty());
            database.setGameMode(loadGameMode());
            database.setVibratorOn(binding.vibtaror.isChecked());

            if (database.saveDatas())
                showMessage("Successfully saved!");
            else
                showMessage("Failed saving!");
        });

        binding.goBackbtn.setOnClickListener(v -> onBackPressed());
    }

    private void showMessage(String message) {
        binding.saveFB.setTranslationY(-130);
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).setTextColor(Color.parseColor("#ffffff")).setBackgroundTint(Color.parseColor("#00664A")).show();
        Handler handler = new Handler();
        handler.postDelayed(() -> binding.saveFB.setTranslationY(0), 3100);
    }

    private void setDefaultValues() {
        switch (database.getGameMode()) {
            case 1:
                binding.threeRB.setChecked(true);
                break;
            case 2:
                binding.fourRB.setChecked(true);
                break;
            default:
                Toast.makeText(this, "Still null -> Game Mode", Toast.LENGTH_SHORT).show();
        }
        switch (database.getDifficulty()) {
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

        binding.vibtaror.setChecked(database.isVibratorOn());
    }

    private int loadGameMode() {
        int[] ids = {
                R.id.threeRB,
                R.id.fourRB
        };
        int i;
        for (i = 0; i < 2; i++) {
            RadioButton radioButton = findViewById(ids[i]);
            if (radioButton.isChecked()) {
                break;
            }
        }
        return i + 1;
    }

    private String loadDifficulty() {
        int[] ids = {
                R.id.easyRB,
                R.id.normalRB,
                R.id.hardRB
        };
        int i;
        for (i = 0; i < 3; i++) {
            RadioButton radioButton = findViewById(ids[i]);
            if (radioButton.isChecked()) {
                break;
            }
        }
        switch (i) {
            case 0:
                return "easy";
            case 1:
                return "normal";
            case 2:
                return "hard";
        }
        return "";
    }

    private void loadComponentsSettings() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("settings", MODE_PRIVATE);
        database = new Database(sharedPreferences);

        if (database.loadSettings())
            setDefaultValues();
        else
            showMessage("Setting are not available...");
    }
}