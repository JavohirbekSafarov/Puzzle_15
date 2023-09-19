package com.javohirbekcoder.puzzle15;/*
Created by Javokhirbek on 19/09/2023 at 13:49
*/

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);

    }

    @NonNull
    @Override
    public Result doWork() {
        showNotification();
        return Result.success();
    }

    private void showNotification() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Alerts")
                .setSmallIcon(R.drawable.puzzle2)
                .setContentText("Come and have fun with us")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(100, builder.build());
    }

    public static void setNotification(Context context){

        WorkManager.getInstance().cancelAllWork();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 24, TimeUnit.HOURS)
                .setInitialDelay(24, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
        Log.d("AAA", "setNotification");
    }
}
