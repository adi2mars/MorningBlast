package com.example.morningblastv1;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Calendar;

public class Model {
    public String time;
    public String date;
    public String title;
    boolean checked;
    Calendar calendar;
    ArrayList dayNum;
    Uri uri;
    String spotifyURI;
    int volume;
    Model(String time, String date, String title, boolean checked, Calendar calendar, ArrayList dayNum, Uri uri, String spotifyURI, int volume) {
        this.time = time;
        this.date = date;
        this.title = title;
        this.checked = checked;
        this.calendar = calendar;
        this.dayNum = dayNum;
        this.uri = uri;
        this.spotifyURI = spotifyURI;
        this.volume = volume;
    }

}
