package com.example.morningblastv1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

public class daysActivity extends AppCompatActivity {
    ArrayList<String> days = new ArrayList<>();
    ArrayList<Integer> dayN = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

    }public void onRadioButtonClicked(View view) {

    }

    public void saveDays(View view) {
        String day = "None";

        Intent replyIntent = new Intent();
        replyIntent.putExtra("days", days);
        replyIntent.putExtra("daysNum",dayN);
        setResult(0, replyIntent);
        finish();

    }

    public void onCheckboxClicked(View view) {
        // Is the button now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked){
                    // Pirates are the best
                    days.add("Mo");
                    dayN.add(2);
                }

                else{
                    days.remove("Mo");
                    Object o = 2;
                    dayN.remove(o);

                }
                break;
            case R.id.checkBox2:
                if (checked){
                    // Pirates are the best
                    days.add("Tu");
                    dayN.add(3);
                }

                else{
                    days.remove("Tu");
                    Object o = 3;
                    dayN.remove(o);

                }
                break;
            case R.id.checkBox3:
                if (checked){
                    // Pirates are the best
                    days.add("We");
                    dayN.add(4);
                }

                else{
                    days.remove("We");
                    Object o = 4;
                    dayN.remove(o);

                }

                break;
            case R.id.checkBox4:
                if (checked){
                    // Pirates are the best
                    days.add("Th");
                    dayN.add(5);
                }

                else{
                    days.remove("Th");
                    Object o = 5;
                    dayN.remove(o);

                }

                break;
            case R.id.checkBox5:
                if (checked){
                    // Pirates are the best
                    days.add("Fr");
                    dayN.add(6);
                }

                else{
                    days.remove("Fr");
                    Object o = 6;
                    dayN.remove(o);

                }
                break;
            case R.id.checkBox6:
                if (checked){
                    // Pirates are the best
                    days.add("Sa");
                    dayN.add(7);
                }

                else{
                    days.remove("Sa");
                    Object o = 7;
                    dayN.remove(o);

                }
                break;
            case R.id.checkBox7:
                if (checked){
                    // Pirates are the best
                    days.add("Su");
                    dayN.add(1);
                }

                else{
                    days.remove("Su");
                    Object o = 1;
                    dayN.remove(o);

                }
                break;
        }

    }

    public void cancelFinish(View view) {
        Intent replyIntent = new Intent();
        ArrayList<String> dayEmpty = new ArrayList<>();
        replyIntent.putExtra("days", dayEmpty);
        setResult(0, replyIntent);
        finish();
    }
}
