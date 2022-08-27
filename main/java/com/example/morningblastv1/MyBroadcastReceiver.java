package com.example.morningblastv1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static com.example.morningblastv1.MainActivity.mSpotifyAppRemote;
import static com.example.morningblastv1.AlarmReceiver.index;

//import static com.example.morningblastv1.NotifyWorker.mSpotifyAppRemote;
import static com.example.morningblastv1.AlarmReceiver.ringtoneFin;
import static com.example.morningblastv1.WordListAdapter.mWordList;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        dismiss(context);
    }
    public void dismiss(Context context){

//        RingtoneManager ringtoneManager = new RingtoneManager(context);
//        ringtoneManager.stopPreviousRingtone();
        Log.d("MyActivity", "spotify:  " + mWordList.get(index-100).spotifyURI);
        if(mWordList.get(index-100).spotifyURI.contains("None")){
            ringtoneFin.stop();
        }else{
            mSpotifyAppRemote.getPlayerApi().pause();
        }
       /* Intent notificationIntent = new Intent();
        notificationIntent.setClassName("com.example.morningblastv1", "com.example.morningblastv1.MainActivity");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //notificationIntent.setAction("OPEN_TAB_1");
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        context.startActivity(notificationIntent);*/
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(index);

    }
}
