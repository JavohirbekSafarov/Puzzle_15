package com.javohirbekcoder.puzzle15;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView playgameBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playgameBtn = (ImageView) findViewById(R.id.playGamebtn);

        playgameBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GameActivity.class));
        });
    }
}