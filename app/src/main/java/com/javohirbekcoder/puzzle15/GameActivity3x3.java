package com.javohirbekcoder.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.javohirbekcoder.puzzle15.databinding.ActivityGameActivity3x3Binding;

public class GameActivity3x3 extends AppCompatActivity {

    private ActivityGameActivity3x3Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameActivity3x3Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_game3x3commingsoon);

        //showMessage("gave time = " + getIntent().getIntExtra("timeUntill", 0));
    }


    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).setTextColor(Color.parseColor("#ffffff")).setBackgroundTint(Color.parseColor("#00664A")).show();
    }

    public void btnClick(View view) {
    }
}