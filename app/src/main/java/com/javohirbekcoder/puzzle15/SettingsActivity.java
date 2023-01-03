package com.javohirbekcoder.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.javohirbekcoder.puzzle15.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}