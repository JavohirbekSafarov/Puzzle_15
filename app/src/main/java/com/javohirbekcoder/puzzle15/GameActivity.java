package com.javohirbekcoder.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.javohirbekcoder.puzzle15.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goBackbtn.setOnClickListener(v -> onBackPressed());


    }
}