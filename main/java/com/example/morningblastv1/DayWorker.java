package com.example.morningblastv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class DayWorker extends Worker {
    public String task;
    public String time;
    public  int index;
    public  String spotifyUri;
    public  String ringtoneUri;
    public int size;
    public static AlarmManager alarmManager;
    public static PendingIntent notifyPendingIntent2;
    public static SpotifyAppRemote mSpotifyAppRemote2;
    public int volume;
    public DayWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        task = getInputData().getString("task");
        time = getInputData().getString("time2");
        index = getInputData().getInt("index2", -1);
        spotifyUri = getInputData().getString("SpotifyURI");
        ringtoneUri = getInputData().getString("RingtoneUri");
        size = getInputData().getInt("size", -1);
        volume = getInputData().getInt("volume", 100);
        long millis = getInputData().getLong("millis", -1);
        Log.d("MyActivity", "reached here" + spotifyUri);
        Intent notifyIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        notifyIntent.putExtra("task", task);
        notifyIntent.putExtra("time", time);
        notifyIntent.putExtra("index", index);
        notifyIntent.putExtra("SpotifyUri", spotifyUri);
        notifyIntent.putExtra("RingtoneUri", ringtoneUri);
        notifyIntent.putExtra("size", size);
        notifyIntent.putExtra("volume", volume);
        notifyPendingIntent2 = PendingIntent.getBroadcast
                (getApplicationContext(), index, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getApplicationContext().getSystemService
                (ALARM_SERVICE);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        Log.d("MyActivity", "time:  " + c.getTime());
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), notifyPendingIntent2);
        //createNotificationChannel();
        return Result.success();
    }
}
