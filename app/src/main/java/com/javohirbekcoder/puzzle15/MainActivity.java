package com.javohirbekcoder.puzzle15;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.javohirbekcoder.puzzle15.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playGamebtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GameActivity.class));
        });
        binding.settingsBtn.setOnClickListener(v ->{
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        });
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
        binding.asosiyLinear.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpa_minus));
        binding.menuOpened.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_closing));
        binding.menuOpened.setVisibility(View.INVISIBLE);
        binding.menuClosed.setVisibility(View.VISIBLE);
    }
}