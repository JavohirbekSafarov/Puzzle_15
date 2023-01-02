package com.javohirbekcoder.puzzle15;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

    private Dialog goBackDialog, winDialog;
    private boolean isTimerRunning = false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int bestRecordMoves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tiles = new int[16];

        binding.goBackbtn.setOnClickListener(v -> onBackPressed());
        binding.shuffleBtn.setOnClickListener(v -> {
            generateNumbers();
            timeCount = 0;
            loadTimer();
            moves = 0;
            binding.movesTv.setText(String.valueOf(moves));
        });

        loadTimer();
        loadViews();
        loadNumbers();
        generateNumbers();
        loadDataToViews();
        loadDialogs();
        loadBestRecord();
    }



    private void loadTimer() {
        timer = new CountDownTimer(30 * 60 * 1000, 1000) { //30 minut
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;
                timeCount++;
                int second = timeCount % 60;
                int minute = timeCount / 60;
                YoYo.with(Techniques.Pulse)
                        .duration(700)
                        .repeat(0)
                        .playOn(findViewById(R.id.timeTv));
                binding.timeTv.setText(String.format("%02d:%02d", minute, second));
            }

            @Override
            public void onFinish() {
                if (!isWin) {
                    isTimerRunning = false;
                    Toast.makeText(GameActivity.this, "You are lose!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void loadViews() {
        buttons = new Button[4][4];
        for (int i = 0; i < 16; i++) {
            buttons[i / 4][i % 4] = (Button) findViewById(btnIds[i]);
        }
    }

    private void loadNumbers() {
        for (int i = 0; i < 16; i++) {
            tiles[i] = i + 1;
        }
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
        loadDataToViews();
        if (!isSolvable())
            generateNumbers();
    }

    private void loadDialogs() {
        winDialog = new Dialog(GameActivity.this);
        goBackDialog = new Dialog(GameActivity.this);
        winDialog.setCancelable(false);
    }

    private void loadBestRecord() {
        sharedPreferences = getSharedPreferences("records", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bestRecordMoves = sharedPreferences.getInt("recordMoves", 99999999);
        if (bestRecordMoves == 99999999)
            binding.recordTV.setText("You have not best record!");
        else
            binding.recordTV.setText("Best record: " + bestRecordMoves);
    }



    private void callGoBackDialog() {
        goBackDialog.setContentView(R.layout.go_back_dialog);
        Button yesBtn = goBackDialog.findViewById(R.id.yesBtn);
        Button noBtn = goBackDialog.findViewById(R.id.noBtn);
        goBackDialog.show();
        yesBtn.setOnClickListener(v -> super.onBackPressed());
        noBtn.setOnClickListener(v -> goBackDialog.dismiss());
    }

    private void loadDataToViews() {
        emptyX = 3;
        emptyY = 3;
        for (int i = 0; i < 16; i++) {
            buttons[i / 4][i % 4].setText(String.valueOf(tiles[i]));
        }
        buttons[emptyX][emptyY].setText("");
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
            moves++;
            binding.movesTv.setText(String.valueOf(moves));
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
            if (bestRecordMoves > moves){
                int movesOrginal = moves + 1;
                editor.putInt("recordMoves", movesOrginal);
                editor.apply();
                Toast.makeText(this, "" + movesOrginal, Toast.LENGTH_SHORT).show();
            }
            callWinDialog(moves, binding.timeTv.getText().toString());
            timer.cancel();
        }
    }

    private void callWinDialog(int moves, String time) {
        winDialog.setContentView(R.layout.win_dialog);
        Button home = winDialog.findViewById(R.id.homeBtn);
        Button newgame = winDialog.findViewById(R.id.newGameBtn);
        TextView timeTvDialog = winDialog.findViewById(R.id.timeTvDialog);
        TextView movesTvDialod = winDialog.findViewById(R.id.movesTvDialog);
        timeTvDialog.setText("Time: " + time);
        movesTvDialod.setText("Moves: " + (moves + 1));
        winDialog.show();
        home.setOnClickListener(v -> super.onBackPressed());
        newgame.setOnClickListener(v -> {
            winDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), GameActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        callGoBackDialog();
    }

    @Override
    protected void onPause() {
        timer.cancel();
        isTimerRunning = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (!isTimerRunning)
            timer.start();
        super.onResume();
    }
}