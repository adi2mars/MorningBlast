package com.example.morningblastv1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static com.example.morningblastv1.AlarmReceiver.ringtoneFin;
import static com.example.morningblastv1.MainActivity.mSpotifyAppRemote;

import static com.example.morningblastv1.AlarmReceiver.index;
//import static com.example.morningblastv1.NotifyWorker.mSpotifyAppRemote;
//import static com.example.morningblastv1.NotifyWorker.ringtone;
import static com.example.morningblastv1.WordListAdapter.mWordList;

public class MyBroadcastReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        dismiss(context);
    }
    public void dismiss(Context context){
        Log.d("MyActivity", "Snoozeeee");
//        RingtoneManager ringtoneManager = new RingtoneManager(context);
//        ringtoneManager.stopPreviousRingtone();
        if(mWordList.get(index-100).spotifyURI.contains("None")){
            ringtoneFin.stop();
        }else{
            mSpotifyAppRemote.getPlayerApi().pause();
        }
        /*Intent notificationIntent = new Intent();
        notificationIntent.setClassName("com.example.morningblastv1", "com.example.morningblastv1.MainActivity");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //notificationIntent.setAction("OPEN_TAB_1");
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        context.startActivity(notificationIntent);*/
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(index);
        OneTimeWorkRequest myworkNotif;
        Data descrip;
        int position = index-100;
        String ringtone;
        if(mWordList.get(position).uri == null){
            ringtone = "None";
        }else{
            ringtone = mWordList.get(position).uri.toString();
        }
        Calendar snooze = mWordList.get(position).calendar;
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE)+5);
        System.out.println("now: " + now.getTime());

        // snooze.set(snooze.get(Calendar.YEAR), snooze.get(Calendar.M        ONTH), snooze.get(Calendar.DAY_OF_MONTH), snooze.get(Calendar.HOUR_OF_DAY), snooze.get(Calendar.MINUTE)+5);
        descrip =
                new Data.Builder()
                        .putString("task", mWordList.get(position).title)
                        .putString("time2", mWordList.get(position).time)
                        .putLong("millis", now.getTimeInMillis())
                        .putString("SpotifyURI", mWordList.get(position).spotifyURI)
                        .putString("RingtoneUri", ringtone)
                        .putInt("size",mWordList.size())
                        .putInt("index2", position+100)
                        .build();


        myworkNotif =
                new OneTimeWorkRequest.Builder(onTimeWorker.class)
                        .setInputData(descrip)
                      //  .setInitialDelay( 5 , TimeUnit.MINUTES)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                        .build();
        WorkManager.getInstance().enqueueUniqueWork("mywork2"+(position*-1), ExistingWorkPolicy.REPLACE, myworkNotif);

    }
}
