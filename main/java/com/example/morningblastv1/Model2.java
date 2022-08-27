package com.example.morningblastv1;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Calendar;

public class Model2 {
    public String temp;
    public String feelsLike;
    public String descrip;
    String hour;
    Model2(String temp, String feelsLike, String descrip, String hour) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.descrip = descrip;
        this.hour = hour;

    }

}
