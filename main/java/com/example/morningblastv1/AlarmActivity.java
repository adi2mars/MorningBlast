package com.example.morningblastv1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    TimePicker alarmTimePicker;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    TextView datePick;
    EditText titleEntry;
    TextView repeat;
    ArrayList dayNum = new ArrayList();
    Ringtone ringtone;
     Uri uri;
     String finalURI = "None";
     Button calendarBtn;
    private static final String CLIENT_ID = "23293212525c40958dbc061ac7bf3cf8";
    private static final String REDIRECT_URI = "redirect://callback";
    public int State = 0;
    int progressChanged = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarmTimePicker = findViewById(R.id.timePicker);
        datePick = findViewById(R.id.txtDatePick);
        titleEntry = findViewById(R.id.editTitle);
        repeat = findViewById(R.id.txtRepeat);
        calendarBtn = findViewById(R.id.btnCalendar);
        SeekBar simpleSeekBar = (SeekBar) findViewById(R.id.seekBar); // initiate the Seek bar
        simpleSeekBar.setMax(15);
        simpleSeekBar.setProgress(15);
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(AlarmActivity.this, "Volume is :" + progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal2 = Calendar.getInstance();
                final int year2 = cal2.get(Calendar.YEAR);
                final int month2 = cal2.get(Calendar.MONTH);
                final int day2 = cal2.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog2 = new DatePickerDialog(
                        AlarmActivity.this,
                        mDateSetListener2,
                        year2, month2, day2);
                //dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();

            }
        });
        mDateSetListener2 = (view1, year, month, dayOfMonth) -> {

            month = month + 1;
            Log.d("MyActivity", "onDateSet: mm/dd/yyy: " + month + "/" + dayOfMonth + "/" + year);

            String date2 = month + "/" + dayOfMonth + "/" + year;
            datePick.setText(date2);
            //sendNotification(cal2);

        };

    }



    public void Save(View view) {
        int hour = alarmTimePicker.getCurrentHour();
        int minute = alarmTimePicker.getCurrentMinute();
        String date = datePick.getText().toString();
        String title = titleEntry.getText().toString();
        if(date.equals("None")){
            date = repeat.getText().toString();

        }
        Intent replyIntent = new Intent();
        Log.d("MyActivity", "values: " + finalURI);
        replyIntent.putExtra("hour", hour);
        replyIntent.putExtra("minute", minute);
        replyIntent.putExtra("date", date);
        replyIntent.putExtra("title", title);
        replyIntent.putExtra("daysNum", dayNum);
        replyIntent.putExtra("ringTone", uri);
        replyIntent.putExtra("spotifyURI", finalURI);
        replyIntent.putExtra("volume", progressChanged);

        setResult(0, replyIntent);
        finish();

    }

    public void recurringDays(View view) {
        Intent intent = new Intent(this, daysActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode==0) {
                    if (data != null) {
                        ArrayList days = data.getStringArrayListExtra("days");
                        String day = "";
                        if (days.isEmpty()) {
                            day = "None";
                        } else {
                            for (int i = 0; i < days.size(); i++) {
                                day = day + days.get(i) + ",";
                            }
                            dayNum = data.getIntegerArrayListExtra("daysNum");
                            datePick.setText("None");
                        }

                        Log.d("MyActivity", "days: " + day);
                        repeat.setText(day);
                    }
                }
                break;
            case RingtoneManager.TYPE_NOTIFICATION:
                if (resultCode == RESULT_OK) {
                    uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                     ringtone = RingtoneManager.getRingtone(this, uri);
                    Log.d("MyActivity","title: " + ringtone.getTitle(this));
                }
                break;
            case 1:
                if(resultCode == 1){
                    finalURI = data.getStringExtra("FinalURI");
                }
        }
    }

    public void pickRingTone(View view) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, RingtoneManager.TYPE_NOTIFICATION);
    }

    public void cancel(View view) {
        finish();
    }

    public void playSpotify(View view) {
       Intent intent = new Intent(this, UseSpotify.class);
       startActivityForResult(intent,1);
    }

}
