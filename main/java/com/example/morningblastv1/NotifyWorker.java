package com.example.morningblastv1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static com.example.morningblastv1.MainActivity.mSpotifyAppRemote;
//import static com.example.morningblastv1.TabFragment1.mPreferences;

public class NotifyWorker extends Worker {    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    public static Ringtone ringtone;
    public static int index2;
    //public static SpotifyAppRemote mSpotifyAppRemote;

    private static final String REDIRECT_URI = "com.morningblastv1://callback";

    private String sharedPrefFile =    "com.example.android.morningblastv1";



    @NonNull
    @Override
    public Result doWork() {
        // Method to trigger an instant notification
        /*ConnectionParams connectionParams =
                new ConnectionParams.Builder("23293212525c40958dbc061ac7bf3cf8")
                        .setRedirectUri(REDIRECT_URI)
                        //.showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getApplicationContext(), connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });*/
        /*Thread thread = new Thread(new Runnable() {
            public Handler mHandler;

            @Override
            public void run() {
                // Heavy task
                ConnectionParams connectionParams =
                        new ConnectionParams.Builder("23293212525c40958dbc061ac7bf3cf8")
                                .setRedirectUri(REDIRECT_URI)
                                //.showAuthView(true)
                                .build();
                Looper.prepare();

                mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        // process incoming messages here
                    }
                };

                Looper.loop();
                SpotifyAppRemote.connect(getApplicationContext(), connectionParams,
                        new Connector.ConnectionListener() {

                            @Override
                            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                mSpotifyAppRemote = spotifyAppRemote;
                                Log.d("MainActivity", "Connected! Yay!");
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Log.e("MainActivity", throwable.getMessage(), throwable);

                                // Something went wrong when attempting to connect! Handle errors here
                            }
                        });
            }
        });
        thread.start();*/
        //new Test().start();
        sendNotification2();
        Log.d("MyActivity", "notif sent");

        return Result.success();
        // (Returning RETRY tells WorkManager to try this task again
        // later; FAILURE says not to try again.)

    }
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";
    //public static SpotifyAppRemote mSpotifyAppRemote;

    public void sendNotification2( ) {
        String task = getInputData().getString("task");
        String time = getInputData().getString("time2");
         index2 = getInputData().getInt("index2", -1);
         String spotifyUri = getInputData().getString("SpotifyURI");
        String ringtoneUri = getInputData().getString("RingtoneUri");
        int size = getInputData().getInt("size", -1);



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
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        //notificationIntent.setAction("OPEN_TAB_1");
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (getApplicationContext(), 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        /*String description = "";
        for(int i = 0; i<a;i++) {
            if (i != (a - 1)) {
                description = description + mPreferences.getString("Task"+i,"") + ", " ;
            }else{
                description = description + mPreferences.getString("Task"+i,"");

            }
        }*/
        Intent snoozeIntent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        snoozeIntent.putExtra("id", index2);

        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 0, snoozeIntent, 0);

        Intent snoozeIntent2 = new Intent(getApplicationContext(), MyBroadcastReceiver2.class);
        //snoozeIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        snoozeIntent2.putExtra("id", index2);
        PendingIntent snoozePendingIntent2 =
                PendingIntent.getBroadcast(getApplicationContext(), 2, snoozeIntent2, 0);
        Notification notification = builder
                .setContentTitle("Alarm: " + task)
                .setContentText("Time: " + time)
                .setTicker("New Message Alert!")
                .setSmallIcon(R.drawable.ic_launcher2_foreground)
                .addAction(R.drawable.ic_launcher2_foreground,"Dismiss", snoozePendingIntent)
                .addAction(R.drawable.ic_launcher2_foreground,"Snooze",snoozePendingIntent2)
                .setContentIntent(notificationPendingIntent).build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(PRIMARY_CHANNEL_ID + index2);
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID+index2,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            notificationManager.createNotificationChannel(notificationChannel);
            Log.d("MyActivity", "notification coming");
        }
        if(size!=0){
            if(spotifyUri.contains("None")){
                Uri alarmUri;
                if(ringtoneUri.contains("None")){
                    alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    Log.d("MyActivity", "none");
                }else{
                    alarmUri = Uri.parse(ringtoneUri);
                }
                ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
                ringtone.play();
            }else{

                if(spotifyUri.contains("News")){
                    String[]split = spotifyUri.split("News");
                    Log.d("MyActivity", "testing" + split[0]);
                    mSpotifyAppRemote.getPlayerApi().skipToIndex(split[0],0);
                }else{
                    mSpotifyAppRemote.getPlayerApi().play(spotifyUri);
                }
            }

        }

        notificationManager.notify(index2, notification);
        Log.d("MyActivity", "pos6"+ index2);
    }

    public void dismiss(){
        ringtone.stop();
    }
    public class Test extends Thread {
        @Override
        public void run() {

            ConnectionParams connectionParams =
                    new ConnectionParams.Builder("23293212525c40958dbc061ac7bf3cf8")
                            .setRedirectUri(REDIRECT_URI)
                            //.showAuthView(true)
                            .build();

            SpotifyAppRemote.connect(getApplicationContext(), connectionParams,
                    new Connector.ConnectionListener() {

                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("MainActivity", "Connected! Yay!");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("MainActivity", throwable.getMessage(), throwable);

                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });        }
    }




}
