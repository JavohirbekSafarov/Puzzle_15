package com.javohirbekcoder.puzzle15;

import static com.javohirbekcoder.puzzle15.MyWorker.setNotification;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.WorkManager;

import com.javohirbekcoder.puzzle15.databinding.ActivityMainBinding;

import java.util.Calendar;

import kotlin.jvm.Throws;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Database database;
    private int timeUntill = 30;
    private int gameMode = 2;
    public static int wins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding.playGamebtn.setEnabled(true);

        createNotificationFromWorkManager();

        //createNotification();

        binding.playGamebtn.setOnClickListener(v -> {
            Intent intent = null;
            switch (gameMode){
                case 1:
                    intent = new Intent(getApplicationContext(), GameActivity3x3.class);
                    break;
                case 2:
                    intent = new Intent(getApplicationContext(), GameActivity.class);
                    break;
            }
            assert intent != null;
            intent.putExtra("timeUntill", timeUntill);
            startActivity(intent);
        });


        binding.settingsBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingsActivity.class)));
        binding.infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            intent.putExtra("wins", wins);
            startActivity(intent);
        });

        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1
                );
            }
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }*/
            return;
        }
    }

    private void createNotificationFromWorkManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(
                    "Alerts",
                    "Alerts",
                    NotificationManager.IMPORTANCE_DEFAULT
            ));
        }else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify();
        }

        setNotification(this);
    }

   /* private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "daily_notification",
                    "Daily Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        scheduleDailyNotification();
    }

    private void scheduleDailyNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set the time for the daily notification (e.g., 9:00 AM)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 45);

        // Schedule the daily notification
        alarmManager.set(
                AlarmManager.RTC,
                calendar.getTimeInMillis(),
                pendingIntent
        );


    }*/


    private void loadDatabase() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("settings", MODE_PRIVATE);
        database = new Database(sharedPreferences);

        if (database.loadSettings()) {
            setSettingtoGame();
        }
    }

    private void setSettingtoGame() {
        switch (database.getGameMode()){
            case 1:
                gameMode = 1;
                break;
            case 2:
                gameMode = 2;
                break;
        }
        switch (database.getDifficulty()){
            case "easy":
                timeUntill = 30;
                break;
            case "normal":
                timeUntill = 15;
                break;
            case "hard":
                timeUntill = 3;
                break;
        }
        wins = database.getWins();
        //Toast.makeText(this, "wins: " + wins, Toast.LENGTH_SHORT).show();
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
        binding.menuOpened.setVisibility(View.INVISIBLE);
        binding.menuClosed.setVisibility(View.VISIBLE);
        loadDatabase();
        binding.playGamebtn.setEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Permissions granted; proceed with your feature
            } else {
                // Permissions not granted; inform the user or handle it accordingly
            }
        }
    }

}