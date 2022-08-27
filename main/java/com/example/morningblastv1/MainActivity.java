package com.example.morningblastv1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.example.morningblastv1.TabFragment1.mPreferences;
import static com.example.morningblastv1.TabFragment1.mWordList;
import static com.example.morningblastv1.WordListAdapter.runAt;

public class MainActivity extends AppCompatActivity {
    private static final String REDIRECT_URI = "com.morningblastv1://callback";
   public static SpotifyAppRemote mSpotifyAppRemote;
   public static String sharedText  = "none";
    public static int position  = -1;
    public static int[] days;
    public static long millis =0;
    private String sharedPrefFile =    "com.example.morningblastv1";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Intent intent = onNewIntent();
        Log.d("MyActivity", "plss get it pls  " + intent.getCategories());
        String sharedText = intent.getStringExtra("spotify");
        Log.d("MyActivity", "plss get it pls  " + sharedText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            DialogFragment newFragment = new StartingDialog();
            newFragment.show(getSupportFragmentManager(), "missiles");
            Toast.makeText(MainActivity.this, "Run only once", Toast.LENGTH_LONG)
                    .show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).apply();
        androidx.appcompat.widget.Toolbar toolbar =
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
// Set the text for each tab.
        TabLayout.Tab tab2 = tabLayout.newTab().setText(R.string.tab_label2);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));

// Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
// Use PagerAdapter to manage page views in fragments.
        // Use PagerAdapter to manage page views in fragments.
// Each page is represented by its own fragment.
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
// Setting a listener for clicks.
// Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new
                                                   TabLayout.OnTabSelectedListener() {
                                                       @Override
                                                       public void onTabSelected(TabLayout.Tab tab) {
                                                           viewPager.setCurrentItem(tab.getPosition());
                                                       }

                                                       @Override
                                                       public void onTabUnselected(TabLayout.Tab tab) {
                                                       }

                                                       @Override
                                                       public void onTabReselected(TabLayout.Tab tab) {
                                                       }
                                                   });
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        if(mWordList.size()==0) {
            for (int i = 0; i < mPreferences.getInt("Size", -1); i++) {
                String time = mPreferences.getString("time" + i, "Error");
                Log.d("MyActivity", "saved22" + "" + mPreferences.getInt("Size", -1));

                String date = mPreferences.getString("date" + i, "Error");
                String title = mPreferences.getString("title" + i, "Error");
                boolean checked = mPreferences.getBoolean("checked" + i, false);
                long millis = mPreferences.getLong("calendar" + i, -1);
                int volume = mPreferences.getInt("volume"+i, 100);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(millis);
                String arraylist = mPreferences.getString("dayNum" + i, "Error");
                String uri = mPreferences.getString("uri" + i, "None");
                Uri uriFinal;
                if(!uri.contains("None")){
                    uriFinal = Uri.parse(uri);
                }else{
                    uriFinal = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
                Log.d("MyActivity", "saved22      " + uri);

                String spotifyUri = mPreferences.getString("SpotifyURI" + i, "Error");
                arraylist = arraylist.replaceAll("[\\[\\](){}]","");
                ArrayList<Integer> daysList = new ArrayList();
                if(arraylist.contains(",")){
                    String [] array = arraylist.split(", ");
                    for(int j =0;j<array.length; j++){
                        // Log.d("MyActivity", "days:  " + array[j]);
                        daysList.add(Integer.parseInt(array[j]));
                    }
                }

                mWordList.add(new Model(time,date,title,checked,calendar,daysList, uriFinal,spotifyUri, volume));
                Log.d("MyActivity", "saved22      " + spotifyUri);

            }
        }
        position = getIntent().getIntExtra("index", -1);
        millis = getIntent().getLongExtra("millis", -1);
        days = getIntent().getIntArrayExtra("days");
        if(position!=-1 && mWordList.get(position).dayNum!=null && mWordList.get(position).dayNum.size()>0){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);
            //ArrayList al = new ArrayList(Arrays.asList(days));
            runAt(c, position, days, mWordList);
        }
        Log.d("MyActivity", "what  " + millis+days);
        if(sharedText == null || sharedText.contains("none")){
            sharedText = getIntent().getStringExtra("spotify");
            Log.d("MyActivity", "what  " + sharedText);

        }
        ConnectionParams connectionParams =
                new ConnectionParams.Builder("23293212525c40958dbc061ac7bf3cf8")
                        .setRedirectUri(REDIRECT_URI)
                        //.showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        if(sharedText != null){
                        if(!sharedText.contains("none")){
                            if(sharedText.contains("News")){
                                String[]split = sharedText.split("News");
                                Log.d("MyActivity", "testing" + split[0]);
                                mSpotifyAppRemote.getPlayerApi().skipToIndex(split[0],0);
                            }else{
                                Log.d("MyActivity", "why liek thiss  " + sharedText);

                                mSpotifyAppRemote.getPlayerApi().play(sharedText);


                            }
                            sharedText = "none";
                            getIntent().removeExtra("spotify");
                            Log.d("MyActivity", "plssssssssss  " + sharedText);

                        }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });



      /*  if(getIntent().getAction().equals("OPEN_TAB_1")) {
            tabLayout.setScrollPosition(1,0f,true);
            viewPager.setCurrentItem(1);

            //tab.select();
            // Open Tab
        }else{
            tabLayout.setScrollPosition(0,0f,true);
            viewPager.setCurrentItem(0);
        }*/





    }
    @Override
    protected void onPause() throws NullPointerException{
        super.onPause();
        try {
            //  Block of code to try
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            //SharedPreferences.Editor preferencesEditor2 = mPreferences.edit();

            // ...
            //preferencesEditor.putInt("Size", mWordList.size());
            for (int i = 0; i < mWordList.size(); i++) {
                preferencesEditor.putInt("Size", mWordList.size());
                preferencesEditor.putString("time" + i, mWordList.get(i).time);
                preferencesEditor.putString("date" + i, mWordList.get(i).date);
                preferencesEditor.putString("title" + i, mWordList.get(i).title);
                preferencesEditor.putBoolean("checked" + i, mWordList.get(i).checked);
                preferencesEditor.putLong("calendar" + i,mWordList.get(i).calendar.getTimeInMillis());
                preferencesEditor.putString("dayNum" + i, mWordList.get(i).dayNum.toString());
                preferencesEditor.putInt("volume"+i, mWordList.get(i).volume);
                //preferencesEditor.putString("spotify", mSpotifyAppRemote.toString());
                if(!mWordList.get(i).spotifyURI.equals("None")){
                    preferencesEditor.putString("SpotifyURI" + i, mWordList.get(i).spotifyURI);
                    preferencesEditor.putString("uri" + i, "None");
                }else{
                    if(mWordList.get(i).uri!=null){
                        preferencesEditor.putString("uri" + i, mWordList.get(i).uri.toString());
                    }else{
                        preferencesEditor.putString("uri" + i, "None");
                    }
                    preferencesEditor.putString("SpotifyURI" + i, "None");
                }
                preferencesEditor.apply();
            }
        }
        catch(Exception e) {
            //  Block of code to handle errors
            Log.d("MyActvity", "didn't work");
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MyActivity", "startttttttttttttt");

        //SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        // We will start writing our code here.
        // Set the connection parameters

        //Log.d("MyActivity", "launched"+ mSpotifyAppRemote.toString());

    }
    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
        //SpotifyAppRemote.disconnect(mSpotifyAppRemote);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    public void addAlarm(View view) {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode==0){
                    if(data!=null) {
                        int hour = data.getIntExtra("hour", -1);
                        int minute = data.getIntExtra("minute", -1);
                        String date = data.getStringExtra("date");
                        String title = data.getStringExtra("title");
                        ArrayList dayNum = data.getIntegerArrayListExtra("daysNum");
                        Uri uri = data.getParcelableExtra("ringTone");
                        String spotifyURI = data.getStringExtra("spotifyURI");
                        int volume = data.getIntExtra("volume", 100);

                        Calendar c = Calendar.getInstance();

                        if (date.contains("/")) {
                            String[] array = date.split("/");
                            c.set(Integer.parseInt(array[2]), Integer.parseInt(array[0]) - 1, Integer.parseInt(array[1]), hour, minute, 0);
//                        c.set(Calendar.MONTH, Integer.parseInt(array[0]));
//                        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(array[1]));
//                        c.set(Calendar.YEAR, Integer.parseInt(array[2]));
                        } else {
                            c.set(Calendar.HOUR_OF_DAY, hour);
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.SECOND, 0);
                            Calendar current = Calendar.getInstance();
                            if(c.getTimeInMillis()<current.getTimeInMillis()){
                                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1);
                            }
                        }
                        String time;
                        String minuteFin;
                        if(c.get(Calendar.MINUTE)<10){
                            minuteFin = "0"+c.get(Calendar.MINUTE);
                        }else{
                            minuteFin = c.get(Calendar.MINUTE) +"";
                        }
                        if(c.get(Calendar.AM_PM) ==1){
                             time = c.get(Calendar.HOUR) + " : " + minuteFin + " PM";
                        }else{
                             time = c.get(Calendar.HOUR) + " : " + minuteFin + " AM";
                        }
                        Log.d("MyActivity", "time =" + c.get(Calendar.AM_PM));
                        if(mWordList.size()==0){
                            //mWordList.addLast(new Model(time, date, title, true, c, dayNum, uri, spotifyURI));
                            mWordList.addLast(new Model(time, date, title, false, c, dayNum, uri, spotifyURI, volume));
                        }else{
                            mWordList.addLast(new Model(time, date, title, false, c, dayNum, uri, spotifyURI, volume));
                        }
                        Log.d("MyActivity", "test: " + mWordList.get(0).date);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
