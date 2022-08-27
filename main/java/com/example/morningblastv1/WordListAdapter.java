package com.example.morningblastv1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static com.example.morningblastv1.TabFragment1.mPreferences;
//import static com.example.morningblastv1.MainActivity.mPreferences;
import static com.example.morningblastv1.onTimeWorker.alarmManager;
import static com.example.morningblastv1.onTimeWorker.notifyPendingIntent;

public class WordListAdapter extends
        RecyclerView.Adapter<WordListAdapter.WordViewHolder>  {
    public static LinkedList<Model> mWordList;
    private LayoutInflater mInflater;
    private static Context context;
    int counter =0;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public WordListAdapter(Context context,
                           LinkedList<Model> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        context = parent.getContext();
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordViewHolder holder,  int position) {
        String mCurrent = mWordList.get(position).time;
        holder.timeView.setText(mCurrent);
        holder.dateView.setText(mWordList.get(position).date);
        holder.titleView.setText(mWordList.get(position).title);
        holder.simpleSwitch.setChecked(mWordList.get(position).checked);
        final Calendar c = Calendar.getInstance();
        holder.simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                counter++;
                if (isChecked) {
                    mWordList.get(position).checked = true;
                 //   if(mWordList.get(position).dayNum.isEmpty()){

                        Log.d("MyActivity", "initial  " + mWordList.get(position).spotifyURI);
                        if(mWordList.get(position).date.contains("Today's Date")){
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY, mWordList.get(position).calendar.get(Calendar.HOUR_OF_DAY));
                            c.set(Calendar.HOUR_OF_DAY, mWordList.get(position).calendar.get(Calendar.HOUR_OF_DAY));
                            c.set(Calendar.MINUTE, mWordList.get(position).calendar.get(Calendar.MINUTE));
                            c.set(Calendar.SECOND, mWordList.get(position).calendar.get(Calendar.SECOND));
                            Log.d("MyActivity","runat : " + c.getTime());
                            runAt(c, position);
                        }else if(!mWordList.get(position).dayNum.isEmpty()){
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY, mWordList.get(position).calendar.get(Calendar.HOUR_OF_DAY));
                            c.set(Calendar.HOUR_OF_DAY, mWordList.get(position).calendar.get(Calendar.HOUR_OF_DAY));
                            c.set(Calendar.MINUTE, mWordList.get(position).calendar.get(Calendar.MINUTE));
                            c.set(Calendar.SECOND, mWordList.get(position).calendar.get(Calendar.SECOND));
                            runAt(c, position, mWordList.get(position).dayNum);
                        }
                        else {
                            runAt(mWordList.get(position).calendar, position);
                        }
                /*    }else{
                        String ringtone;
                        if(mWordList.get(position).uri == null){
                            ringtone = "None";
                        }else{
                            ringtone = mWordList.get(position).uri.toString();
                        }
                            Object days[] = mWordList.get(position).dayNum.toArray();
                            int size = days.length;
                            int []dayFin = new int[size];
                            for(int i =0; i<days.length; i++){
                                dayFin[i] = Integer.parseInt(days[i].toString());
                            }
                            periodicDescrip =
                                    new Data.Builder()
                                            .putString("task", mWordList.get(position).title)
                                            .putString("time2", mWordList.get(position).time)
                                            .putString("SpotifyURI", mWordList.get(position).spotifyURI)
                                            .putString("RingtoneUri", ringtone)
                                            .putInt("size",mWordList.size())
                                            .putInt("index2", position+100)
                                            .putIntArray("days", dayFin)
                                            .putLong("millis", mWordList.get(position).calendar.getTimeInMillis())
                                            .putInt("volume", mWordList.get(position).volume)
                                            .build();
                            Log.d("MyActivity", "recurr calendar: " + mWordList.get(position).calendar.getTime());
                            PeriodicWorkRequest periodicWork;
                            Calendar c = Calendar.getInstance();
                            Calendar a = mWordList.get(position).calendar;
                            if(c.get(Calendar.HOUR_OF_DAY)==23 && c.get(Calendar.MINUTE)>= 45 && a.get(Calendar.HOUR_OF_DAY)!= 23 && !(a.get(Calendar.MINUTE)>=45)) {
                                int delay = 60 - c.get(Calendar.MINUTE);
                                periodicWork = new PeriodicWorkRequest.Builder(NotifyDayWorker.class, 15, TimeUnit.MINUTES)
                                        .setInitialDelay(delay, TimeUnit.MINUTES)
                                        .setInputData(periodicDescrip)
                                        .build();
                                WorkManager.getInstance(context).enqueueUniquePeriodicWork("MyActivity" + position, ExistingPeriodicWorkPolicy.REPLACE, periodicWork);
                                Log.d("MyActivity", "exception");
                            }else{
                                periodicWork = new PeriodicWorkRequest.Builder(NotifyDayWorker.class, 15, TimeUnit.MINUTES)
                                        .setInputData(periodicDescrip)
                                        .build();
                                WorkManager.getInstance(context).enqueueUniquePeriodicWork("MyActivity" + position, ExistingPeriodicWorkPolicy.REPLACE, periodicWork);
                                Log.d("MyActivity", "not exception , normal");
                            }
                    }*/
                } else {
                    // The toggle is disabled
                    //Log.d("MyActivity", "cancelled");
                    mWordList.get(position).checked = false;
                    cancelAlarm(position);
                    if(!mWordList.get(position).dayNum.isEmpty()){
                        cancelRecurring(position);
                    }
                }
            }
        });
        /*if(!mWordList.get(position).dayNum.isEmpty() && counter>0){
            holder.simpleSwitch.setChecked(true);
        }*/


    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }


    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView timeView;
        public TextView dateView;
        public TextView titleView;
        final WordListAdapter mAdapter;
        Switch simpleSwitch;
        ImageButton closeBtn;
        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            timeView = itemView.findViewById(R.id.txtTime);
            dateView = itemView.findViewById(R.id.txtDate);
            titleView = itemView.findViewById(R.id.txtTitle);
            simpleSwitch = itemView.findViewById(R.id.switch1);
            closeBtn = itemView.findViewById(R.id.btnClose);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        Log.d("MyActivity", "answer: " + getAdapterPosition());
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDeleteClick(getAdapterPosition());
                            cancelRecurring(getAdapterPosition());
                            cancelAlarm(position);
                            //removeAt(getAdapterPosition());
                            //mWordList.remove(getAdapterPosition());
                            //notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), mWordList.size());
                            if(position ==0){
                                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                preferencesEditor.putInt("Size", 0);
                                preferencesEditor.apply();
                            }

                            //wordItemView.setText("Enter in Task");
                            //prioritiesView.setText(getAdapterPosition());
                            //TimeManageView.setText(0);
                        }
                    }
                }
            });


            this.mAdapter = adapter;
        }
    }
    private void cancelRecurring(int position){
        WorkManager.getInstance().cancelUniqueWork("MyActivity"+position);
    }

    private void cancelAlarm(int position) {
        // alarmManager.cancel(broadcast);
        try {
            WorkManager.getInstance().cancelUniqueWork("mywork2"+position);
            alarmManager.cancel(notifyPendingIntent);

        }catch (Exception NullPointerException){
            return;
        }
    }
    static OneTimeWorkRequest mywork2;
    static Data descrip;
    Data periodicDescrip;
    public static void runAt(Calendar cal, int position, ArrayList day) {
        Calendar calendar = Calendar.getInstance();
        long difference = cal.getTimeInMillis() - calendar.getTimeInMillis();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String ringtone;
            if(mWordList.get(position).uri == null){
                 ringtone = "None";
            }else{
                ringtone = mWordList.get(position).uri.toString();
            }
            int []dayFin;
            Object days[] =day.toArray();
            int size = days.length;
            dayFin = new int[size];
            for(int i =0; i<days.length; i++){
                dayFin[i] = Integer.parseInt(days[i].toString());
            }
            Arrays.sort(dayFin);

            int count = -1;
            for(int i = 0; i<dayFin.length;  i++){
                if(dayFin[i]>=cal.get(Calendar.DAY_OF_WEEK)) {
                    if(dayFin[i]==cal.get(Calendar.DAY_OF_WEEK) && calendar.getTimeInMillis()< cal.getTimeInMillis()) {
                        count = dayFin[i];
                        i = dayFin.length;
                    }else if(dayFin[i]!=cal.get(Calendar.DAY_OF_WEEK)){
                        count = dayFin[i];
                        i = dayFin.length;
                    }
                }
            }
            if(count==-1){
                count = dayFin[0];
                Log.d("MyActivity", "calend: " + cal.getTime());
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+7);
                Log.d("MyActivity", "calend2: " + cal.getTime());

            }
            cal.set(Calendar.DAY_OF_WEEK, count);
            Log.d("MyActivity", "count: " + count);

            Log.d("MyActivity", "recurring calendar: " + cal.getTime());
            descrip =
                    new Data.Builder()
                            .putString("task", mWordList.get(position).title)
                            .putString("time2", mWordList.get(position).time)
                            .putLong("millis", cal.getTimeInMillis())
                            .putString("SpotifyURI", mWordList.get(position).spotifyURI)
                            .putString("RingtoneUri", ringtone)
                            .putInt("size",mWordList.size())
                            .putInt("index2", position+100)
                            .putInt("volume", mWordList.get(position).volume)
                            .putIntArray("days", dayFin)
                            .build();


            mywork2 =
                    new OneTimeWorkRequest.Builder(onTimeWorker.class)
                            .setInputData(descrip)
                           // .setInitialDelay( difference , TimeUnit.MILLISECONDS)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                            .build();
            WorkManager.getInstance().enqueueUniqueWork("mywork2"+position, ExistingWorkPolicy.KEEP, mywork2);
        }
        else{
            Toast.makeText(context, "Need Android 8 or Higher", Toast.LENGTH_SHORT).show();
        }
    }
    public static void runAt(Calendar cal, int position, int[]day,LinkedList<Model> mWordList) {
        Calendar calendar = Calendar.getInstance();
        long difference = cal.getTimeInMillis() - calendar.getTimeInMillis();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String ringtone;
            if(mWordList.get(position).uri == null){
                ringtone = "None";
            }else{
                ringtone = mWordList.get(position).uri.toString();
            }
            int []dayFin = day;

            int count = -1;
            for(int i = 0; i<dayFin.length;  i++){
                if(dayFin[i]>=cal.get(Calendar.DAY_OF_WEEK)) {
                    if(dayFin[i]==cal.get(Calendar.DAY_OF_WEEK) && calendar.getTimeInMillis()< cal.getTimeInMillis()) {
                        count = dayFin[i];
                        i = dayFin.length;
                    }else if(dayFin[i]!=cal.get(Calendar.DAY_OF_WEEK)){
                        count = dayFin[i];
                        i = dayFin.length;
                    }
                }
            }
            if(count==-1){
                count = dayFin[0];
                Log.d("MyActivity", "calend: " + cal.getTime());
                cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+7);
                Log.d("MyActivity", "calend2: " + cal.getTime());

            }
            cal.set(Calendar.DAY_OF_WEEK, count);
            Log.d("MyActivity", "count: " + count);

            Log.d("MyActivity", "recurring calendar: " + cal.getTime());
            descrip =
                    new Data.Builder()
                            .putString("task", mWordList.get(position).title)
                            .putString("time2", mWordList.get(position).time)
                            .putLong("millis", cal.getTimeInMillis())
                            .putString("SpotifyURI", mWordList.get(position).spotifyURI)
                            .putString("RingtoneUri", ringtone)
                            .putInt("size",mWordList.size())
                            .putInt("index2", position+100)
                            .putInt("volume", mWordList.get(position).volume)
                            .putIntArray("days", dayFin)
                            .build();


            mywork2 =
                    new OneTimeWorkRequest.Builder(onTimeWorker.class)
                            .setInputData(descrip)
                            // .setInitialDelay( difference , TimeUnit.MILLISECONDS)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                            .build();
            WorkManager.getInstance().enqueueUniqueWork("mywork2"+position, ExistingWorkPolicy.KEEP, mywork2);
        }
        else{
            Toast.makeText(context, "Need Android 8 or Higher", Toast.LENGTH_SHORT).show();
        }
    }
    public void runAt(Calendar cal, int position) {
        Calendar calendar = Calendar.getInstance();
        long difference = cal.getTimeInMillis() - calendar.getTimeInMillis();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String ringtone;
            if(mWordList.get(position).uri == null){
                ringtone = "None";
            }else{
                ringtone = mWordList.get(position).uri.toString();
            }
            descrip =
                    new Data.Builder()
                            .putString("task", mWordList.get(position).title)
                            .putString("time2", mWordList.get(position).time)
                            .putLong("millis", mWordList.get(position).calendar.getTimeInMillis())
                            .putString("SpotifyURI", mWordList.get(position).spotifyURI)
                            .putString("RingtoneUri", ringtone)
                            .putInt("size",mWordList.size())
                            .putInt("index2", position+100)
                            .putInt("volume", mWordList.get(position).volume)
//                            .putIntArray("days", null)
                            .build();


            mywork2 =
                    new OneTimeWorkRequest.Builder(onTimeWorker.class)
                            .setInputData(descrip)
                            // .setInitialDelay( difference , TimeUnit.MILLISECONDS)// Use this when you want to add initial delay or schedule initial work to `OneTimeWorkRequest` e.g. setInitialDelay(2, TimeUnit.HOURS)
                            .build();
            WorkManager.getInstance().enqueueUniqueWork("mywork2"+position, ExistingWorkPolicy.KEEP, mywork2);
        }
        else{
            Toast.makeText(context, "Need Android 8 or Higher", Toast.LENGTH_SHORT).show();
        }
    }

}