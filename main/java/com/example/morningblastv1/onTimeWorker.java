package com.example.morningblastv1;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.morningblastv1.MainActivity.mSpotifyAppRemote;

public class onTimeWorker extends Worker {
    private static final int NOTIFICATION_ID = 123;
    private NotificationManager mNotificationManager;
    // Notification ID.
    // Notification channel ID.
    public String task;
    public String time;
    public  int index;
    public  String spotifyUri;
    public  String ringtoneUri;
   public int size;
   public static AlarmManager alarmManager;
   public static PendingIntent notifyPendingIntent;
   public static SpotifyAppRemote mSpotifyAppRemote2;
   public int volume;


    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    public onTimeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        mSpotifyAppRemote2 = mSpotifyAppRemote;
         task = getInputData().getString("task");
         time = getInputData().getString("time2");
         index = getInputData().getInt("index2", -1);
         spotifyUri = getInputData().getString("SpotifyURI");
         ringtoneUri = getInputData().getString("RingtoneUri");
          size = getInputData().getInt("size", -1);
          volume = getInputData().getInt("volume", 100);
          int[] days = getInputData().getIntArray("days");
        long millis = getInputData().getLong("millis", -1);
        Log.d("MyActivity", "reached here" + spotifyUri);
        Intent notifyIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        notifyIntent.putExtra("task", task);
        notifyIntent.putExtra("time", time);
        notifyIntent.putExtra("millis", millis);
        notifyIntent.putExtra("days", days);
        notifyIntent.putExtra("index", index);
        notifyIntent.putExtra("SpotifyUri", spotifyUri);
        notifyIntent.putExtra("RingtoneUri", ringtoneUri);
        notifyIntent.putExtra("size", size);
        notifyIntent.putExtra("volume", volume);
        notifyPendingIntent = PendingIntent.getBroadcast
                (getApplicationContext(), NOTIFICATION_ID+index, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

         alarmManager = (AlarmManager) getApplicationContext().getSystemService
                (ALARM_SERVICE);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        Log.d("MyActivity", "time:  " + c.getTime());
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), notifyPendingIntent);
        createNotificationChannel();
        return Result.success();
    }

    public void createNotificationChannel() {
        Log.d("MyActivity", "reached here too");
        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to " +
                    "stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
