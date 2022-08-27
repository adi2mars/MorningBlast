package com.example.morningblastv1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.morningblastv1.TabFragment1.mWordList;
import static com.example.morningblastv1.WordListAdapter.runAt;


public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 123;
    private NotificationManager mNotificationManager;
    // Notification ID.
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    public static Ringtone ringtoneFin;
    String task;
    String time;
    public static int index;
    public static SpotifyAppRemote mSpotifyAppRemote3;
    public String spotifyUri;
    String ringtoneUri;
    int size;
    int volume;
    long millis;
    int[] days;
    private static final String REDIRECT_URI = "com.morningblastv1://callback";



    /**
     * Called when the BroadcastReceiver receives an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //AsyncTaskSpotify weather = new AsyncTaskSpotify();
      //  weather.execute(context);
        //String a = mSpotifyAppRemote3.toString();
       // Log.d("MyActivity", "Con"+ a);

        spotifyUri = intent.getStringExtra("SpotifyUri");
        index = intent.getIntExtra("index", -1);
         millis = intent.getLongExtra("millis", -1);
         days = intent.getIntArrayExtra("days");
        Log.d("MyActivity", "mil: " + millis + days);

        //Intent launch_intent = new  Intent(context, MainActivity.class);
       // launch_intent.setComponent(new ComponentName("com.example.morningblastv1","com.example.morningblastv1.MainActivity"));
        Intent launch_intent = context
                .getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        //launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launch_intent.putExtra("spotify", spotifyUri);
        launch_intent.putExtra("index", index-100);
        launch_intent.putExtra("millis", millis);
        launch_intent.putExtra("days", days);
        launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launch_intent.setAction("action");
        launch_intent.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(launch_intent);
        Log.d("MyActivity", "Con"+ spotifyUri);
        //


        //launchIntent.putExtra("some_data", "value");
        task = intent.getStringExtra("task");
        time = intent.getStringExtra("time");

        volume = intent.getIntExtra("volume", 15);

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d("MyActivity", "volume: " + maxVolume);

        audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        ringtoneUri = intent.getStringExtra("RingtoneUri");
        size = intent.getIntExtra("size", -1);
        Log.d("MyActivity", "why not here mannnn" + spotifyUri);
        mNotificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        // Deliver the notification.
        sendNotification2(context);
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(Context context) {
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MainActivity.class);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent
                        .FLAG_UPDATE_CURRENT);
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher2_foreground)
                .setContentTitle("Hello World")
                .setContentText("On Time???")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    public void sendNotification2(Context context) {




        /*mPreferences = getApplicationContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        int position = mPreferences.getInt("Position5",-1);
        Log.d("MyActivity", "position:  " + position);
        String spotifyUri = mPreferences.getString("SpotifyURI"+position,"None");
        String ringtoneUri = mPreferences.getString("uri"+position, "None");
        int size = mPreferences.getInt("Size", 0);
        Log.d("MyActivity", "position:  " + size + spotifyUri + ringtoneUri);*/

        //alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

       /* Intent notificationIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        notificationIntent.putExtra("size7", mWordList.size());
        PendingIntent broadcast2 = PendingIntent.getBroadcast(getApplicationContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        //Calendar cal = Calendar.getInstance();
        //long difference = time - cal.getTimeInMillis();
        //cal.add(Calendar.SECOND, time);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        //Toast.makeText(this,"Works123 "+cal.getTimeInMillis(), Toast.LENGTH_SHORT).show();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        //notificationIntent.setAction("OPEN_TAB_1");
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);



        Notification.Builder builder = new Notification.Builder(context);
        /*String description = "";
        for(int i = 0; i<a;i++) {
            if (i != (a - 1)) {
                description = description + mPreferences.getString("Task"+i,"") + ", " ;
            }else{
                description = description + mPreferences.getString("Task"+i,"");

            }
        }*/
        Intent snoozeIntent = new Intent(context, MyBroadcastReceiver.class);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        snoozeIntent.putExtra("id", index);

        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        Intent snoozeIntent2 = new Intent(context, MyBroadcastReceiver2.class);
        //snoozeIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        snoozeIntent2.putExtra("id", index);
        PendingIntent snoozePendingIntent2 =
                PendingIntent.getBroadcast(context, 2, snoozeIntent2, 0);
        Notification notification = builder
                .setContentTitle("Alarm: " + task)
                .setContentText("Time: " + time)
                .setTicker("New Message Alert!")
                .setSmallIcon(R.drawable.ic_launcher2_foreground)
                .addAction(R.drawable.ic_launcher2_foreground,"Dismiss", snoozePendingIntent)
                .addAction(R.drawable.ic_launcher2_foreground,"Snooze",snoozePendingIntent2)
                .setContentIntent(notificationPendingIntent).build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(PRIMARY_CHANNEL_ID + index);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID+index,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            notificationManager.createNotificationChannel(notificationChannel);
            Log.d("MyActivity", "notification coming" + "     " + index);
        }
        notificationManager.notify(index, notification);

        if(size!=0){
            if(spotifyUri.contains("None")){
                Uri alarmUri;
                if(ringtoneUri.contains("None")){
                    alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    Log.d("MyActivity", "none");
                }else{
                    alarmUri = Uri.parse(ringtoneUri);
                }
                ringtoneFin = RingtoneManager.getRingtone(context, alarmUri);
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtoneFin.setVolume(volume);
                    Log.d("MyActivity", "volume: " + volume);
                }*/
                ringtoneFin.play();
            }else{

                if(spotifyUri.contains("News")){
                    String[]split = spotifyUri.split("News");
                    Log.d("MyActivity", "testing" + split[0]);
                    //mSpotifyAppRemote.getPlayerApi().skipToIndex(split[0],0);
                }else{
                    //mSpotifyAppRemote.getPlayerApi().play(spotifyUri);
                }
            }
            int index2 = index-100;

            Calendar c = Calendar.getInstance();
//            c.set(Calendar.HOUR_OF_DAY, mWordList.get(index2).calendar.get(Calendar.HOUR_OF_DAY));
//            c.set(Calendar.HOUR_OF_DAY, mWordList.get(index2).calendar.get(Calendar.HOUR_OF_DAY));
//            c.set(Calendar.MINUTE, mWordList.get(index2).calendar.get(Calendar.MINUTE));
//            c.set(Calendar.SECOND, mWordList.get(index2).calendar.get(Calendar.SECOND));
            c.setTimeInMillis(millis);
            ArrayList al = new ArrayList(Arrays.asList(days));
            //runAt(c, index2, al);
//            Log.d("MyActivity", "spot::"+ mWordList.get(index2).spotifyURI);

        }


        Log.d("MyActivity", "pos6"+ index);
    }


    public class AsyncTaskSpotify extends AsyncTask<Context, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("MyActivity", "worked kinda whyyy" + mSpotifyAppRemote3.toString());
        }

        @Override
        protected Void doInBackground(Context... contexts) {
            ConnectionParams connectionParams =
                    new ConnectionParams.Builder("23293212525c40958dbc061ac7bf3cf8")
                            .setRedirectUri(REDIRECT_URI)
                            //.showAuthView(true)
                            .build();
            Looper.prepare();
            SpotifyAppRemote.connect(contexts[0], connectionParams,
                    new Connector.ConnectionListener() {

                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote3 = spotifyAppRemote;
                            Log.d("MyActivity", "Connected!!!!!!!!! Yay!");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.d("MyActivity", "failedddddddd");

                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });

           // return mSpotifyAppRemote.toString();
            return null;
        }
    }
}
