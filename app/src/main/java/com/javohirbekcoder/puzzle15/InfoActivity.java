package com.javohirbekcoder.puzzle15;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.javohirbekcoder.puzzle15.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goBackbtn.setOnClickListener(v -> onBackPressed());
        binding.policyLinear.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://javohir.onlinegroup.uz/dmca/Puzzle15_policy.html"));
            startActivity(browserIntent);
        });
        binding.checkAppVersion.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.javohirbekcoder.puzzle15"));
            startActivity(browserIntent);
        });
        binding.rateApp.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.javohirbekcoder.puzzle15"));
            startActivity(browserIntent);
        });
        setDatasToViews();
    }

    private void setDatasToViews() {
        binding.winsTV.setText(String.valueOf(getIntent().getIntExtra("wins", -1)));
    }

}