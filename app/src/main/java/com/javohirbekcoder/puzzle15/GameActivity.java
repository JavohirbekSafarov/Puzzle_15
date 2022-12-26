package com.javohirbekcoder.puzzle15;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.javohirbekcoder.puzzle15.databinding.ActivityGameBinding;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private int moves = 0;
    private int emptyX = 3;
    private int emptyY = 3;
    private Button[][] buttons;
    private CountDownTimer timer;
    private int timeCount = 0;
    private int[] tiles;
    boolean isWin = false;
    private final Integer[] btnIds = {R.id.button1, R.id.button2,
            R.id.button3, R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9, R.id.button10,
            R.id.button11, R.id.button12, R.id.button13, R.id.button14,
            R.id.button15, R.id.button16};

    private Dialog goBackDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.goBackbtn.setOnClickListener(v -> onBackPressed());
        loadTimer();
        loadViews();
        loadNumbers();
        generateNumbers();
        loadDataToViews();

        goBackDialog = new Dialog(GameActivity.this);
    }

    private void callGoBackDialog() {
        goBackDialog.setContentView(R.layout.go_back_dialog);
        Button yesBtn = goBackDialog.findViewById(R.id.yesBtn);
        Button noBtn = goBackDialog.findViewById(R.id.noBtn);
        goBackDialog.show();
        yesBtn.setOnClickListener(v -> super.onBackPressed());
        noBtn.setOnClickListener(v -> goBackDialog.dismiss());

    }

    private void loadTimer() {
        timer = new CountDownTimer(30 * 60 * 1000, 1000) { //30 minut
            @Override
            public void onTick(long millisUntilFinished) {
                timeCount++;
                int second = timeCount % 60;
                int minute = timeCount / 60;
                YoYo.with(Techniques.Pulse)
                        .duration(700)
                        .repeat(0)
                        .playOn(findViewById(R.id.timeTv));
                binding.timeTv.setText(String.format("%02d:%02d", minute, second));
                //binding.timeTv.setText(String.format("%02d:%02d:%02d", hour, minute, second));
            }

            @Override
            public void onFinish() {
                if (!isWin) {
                    Toast.makeText(GameActivity.this, "You are lose!", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    private void loadDataToViews() {
        emptyX = 3;
        emptyY = 3;
        for (int i = 0; i < 16; i++) {
            buttons[i / 4][i % 4].setText(String.valueOf(tiles[i]));
        }
        buttons[emptyX][emptyY].setText("");
    }

    private void generateNumbers() {
        int n = 15;
        Random random = new Random();
        while (n > 1) {
            int randomNum = random.nextInt(n--);
            int temp = tiles[randomNum];
            tiles[randomNum] = tiles[n];
            tiles[n] = temp;
        }
        if (!isSolvable())
            generateNumbers();
    }

    private boolean isSolvable() {
        int countInversions = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    countInversions++;
            }
        }
        return countInversions % 2 == 0;
    }

    private void loadNumbers() {
        tiles = new int[16];
        for (int i = 0; i < 16; i++) {
            tiles[i] = i + 1;
        }
    }

    private void loadViews() {
        buttons = new Button[4][4];
        for (int i = 0; i < 16; i++) {
            buttons[i / 4][i % 4] = (Button) findViewById(btnIds[i]);
        }
    }


    public void btnClick(View view) {
        Button button = (Button) view;
        int x = button.getTag().toString().charAt(0) - '0';
        int y = button.getTag().toString().charAt(1) - '0';

        if ((Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x)) {
            buttons[emptyX][emptyY].setText(button.getText().toString());
            button.setText("");
            emptyX = x;
            emptyY = y;
            checkWin();
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .repeat(0)
                    .playOn(findViewById(R.id.movesTv));
            binding.movesTv.setText(String.valueOf(++moves));
        }
    }

    private void checkWin() {
        isWin = false;
        if (emptyX == 3 && emptyY == 3) {
            for (int i = 0; i < 15; i++) {
                if (buttons[i / 4][i % 4].getText().toString().equals(String.valueOf(i + 1)))
                    isWin = true;
                else {
                    isWin = false;
                    break;
                }
            }
        }

        if (isWin) {
            Toast.makeText(this, "Win!! moves" + moves, Toast.LENGTH_SHORT).show();
            timer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        callGoBackDialog();
    }
}