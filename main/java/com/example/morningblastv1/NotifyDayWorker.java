package com.example.morningblastv1;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

public class NotifyDayWorker extends Worker {
    long millis;
    public NotifyDayWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int []days = getInputData().getIntArray("days");
        final Calendar c = Calendar.getInstance();
         millis = getInputData().getLong("millis", -1);
        Calendar current = Calendar.getInstance();


        Log.d("MyActivity", "date:  " + current.getTime());
        for(int i =0; i<days.length;i++){
            current.setTimeInMillis(millis);
            current.set(Calendar.DAY_OF_WEEK, days[i]);
            if(c.getTimeInMillis()>current.getTimeInMillis()){
                current.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH)+7);
            }
            Log.d("MyActivity", "date:  " + current.getTime());
            if(days[i] == c.get(Calendar.DAY_OF_WEEK) && c.get(Calendar.DAY_OF_MONTH) == current.get(Calendar.DAY_OF_MONTH) && current.getTimeInMillis()>c.getTimeInMillis()){
                Log.d("MyActivity", "it workeddddd");
                runAt(current);
                Log.d("MyActivity", "current:  " + current.getTime());
            }else{
                Log.d("MyActivity", "it is still working");
            }

        }

        return null;
    }
    public void runAt(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        //long difference = cal.getTimeInMillis() - calendar.getTimeInMillis();
        String task = getInputData().getString("task");
        String time = getInputData().getString("time2");
        String spotify = getInputData().getString("SpotifyURI");
        String ringtone = getInputData().getString("RingtoneUri");
        int size = getInputData().getInt("size", -1);
        int index = getInputData().getInt("index2", -1);
        int volume = getInputData().getInt("volume", 15);
        long millisFinal = cal.getTimeInMillis();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Data descrip;
            OneTimeWorkRequest mywork2;
            descrip =
                    new Data.Builder()
                            .putString("task", task)
                            .putString("time2", time)
                            .putString("SpotifyURI", spotify)
                            .putString("RingtoneUri", ringtone)
                            .putInt("size",size)
                            .putInt("index2", index)
                            .putLong("millis", millisFinal)
                            .putInt("volume", volume)
                            .build();


            mywork2 =
                    new OneTimeWorkRequest.Builder(onTimeWorker.class)
                            .setInputData(descrip)
                       //     .setInitialDelay( difference , TimeUnit.MILLISECONDS)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                            .build();
            WorkManager.getInstance().enqueueUniqueWork("mywork2"+index, ExistingWorkPolicy.KEEP, mywork2);
        }
        else{
            Log.d("MyActivity", "recurring did not work");
        }



    }
}
