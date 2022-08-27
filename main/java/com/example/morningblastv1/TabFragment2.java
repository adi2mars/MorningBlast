package com.example.morningblastv1;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment implements FetchAddressTask.OnTaskCompleted {
    String quote_url;
    String quoteDescrip = "";
    String author = "";
    TextView quoteTxt;
    TextView dateTxt;
    String Weather_url;
    String News_url;
    String News_url2;
    String News_url3;
    String News_url4;
    String News_url5;

    private RecyclerView mRecyclerView2;
    private WordListAdapter4 mAdapter2;
    private final LinkedList<Model2> mWordList4 = new LinkedList<>();
    private RecyclerView mRecyclerView3;
    private WordListAdapter5 mAdapter3;
    Spinner newsSpinner;
    Button searchNews;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Location mLastLocation;
    public static String finalLocation;
    public double latitudeFinal;
    public double longtitudeFinal;
    String finalCountryCode;
    FusedLocationProviderClient mFusedLocationClient;
    private boolean mTrackingLocation = true;
    private LocationCallback mLocationCallback;
    public static TextView addressLab;
    Switch switchFar;


    private final LinkedList<Model3> mWordList5 = new LinkedList<>();


    public TabFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.tab_fragment2, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        //addressLab = view.findViewById(R.id.txtAddress);
        switchFar = view.findViewById(R.id.switchFarenheit);
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

        } else {
            // permission has been granted, continue as usual
            Log.d("MyActivity", "worked");
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLastLocation = location;
                        Log.d("MyActivity", "Location acquired: " + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
                        latitudeFinal = mLastLocation.getLatitude();
                        longtitudeFinal = mLastLocation.getLongitude();
                        Weather_url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitudeFinal + "&lon=" + longtitudeFinal + "&exclude=minutely,daily&APPID=2b01cc136b56e8b7e0705cb925435a44";
                        if(switchFar.isChecked()){
                            getWeather(true);
                        }else{
                            getWeather(false);
                        }

                        new FetchAddressTask(view.getContext(),
                                TabFragment2.this).execute(location);
                    } else {
                        Log.d("MyActivity", "fail");
                    }
                }
            });
        }
        // Initialize the location callbacks.

        mLocationCallback = new LocationCallback() {
            /**
             * This is the callback that is triggered when the
             * FusedLocationClient updates your location.
             *
             * @param locationResult The result containing the device location.
             */
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // If tracking is turned on, reverse geocode into an address
                Log.d("MyActivity", "gotlocation");
                if (mTrackingLocation) {
                    //locationResult.getLastLocation();
                    if(mLastLocation==null) {
                        mLastLocation = locationResult.getLastLocation();
                        Log.d("MyActivity", "Location acquired: " + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
                        latitudeFinal = mLastLocation.getLatitude();
                        longtitudeFinal = mLastLocation.getLongitude();
                        Weather_url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitudeFinal + "&lon=" + longtitudeFinal + "&exclude=minutely,daily&APPID=2b01cc136b56e8b7e0705cb925435a44";
                        if(switchFar.isChecked()){
                            getWeather(true);
                        }else{
                            getWeather(false);
                        }

                    }
                    new FetchAddressTask(view.getContext(), TabFragment2.this)
                            .execute(locationResult.getLastLocation());
                }
            }
        };

    switchFar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                getWeather(true);
            }else{
                getWeather(false);
            }
        }
    });
        Calendar calendar = Calendar.getInstance();
        dateTxt = view.findViewById(R.id.txtDate);
        String[] split = calendar.getTime().toString().split(" ");
        String finalDate = split[0] + " " + split[1] + " " + split[2] + " " + split[5];
        Log.d("MyActivity", finalDate);
        dateTxt.setText(finalDate);
        quoteTxt = view.findViewById(R.id.txtQOTD);
        searchNews = view.findViewById(R.id.btnSearchNews);
        quote_url = "https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Fwww.brainyquote.com%2Flink%2Fquotebr.rss";

        newsSpinner = (Spinner) view.findViewById(R.id.newsSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.types_news, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
      //  adapter.setNotifyOnChange(true); //only need to call this once
        newsSpinner.setPrompt("Pick Type of News");
        newsSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        newsSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mRecyclerView3 = view.findViewById(R.id.recyclerView4);
// Create an adapter and supply the data to be displayed.
        mAdapter3 = new WordListAdapter5(view.getContext(), mWordList5);
// Connect the adapter with the RecyclerView.
        mRecyclerView3.setAdapter(mAdapter3);
// Give the RecyclerView a default layout manager.
        mRecyclerView3.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Log.d("MyActivity", "latitude= " + latitudeFinal);
        mRecyclerView3.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));



        searchNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = newsSpinner.getSelectedItem().toString();
                int position = newsSpinner.getSelectedItemPosition();

                Log.d("MyActivity", "label: " + label);
                Log.d("MyActivity", "url: " + News_url);

                //newsSpinner.setSelection(position);
                int size = mWordList5.size();
                mWordList5.clear();
                mRecyclerView3.getAdapter().notifyItemRangeRemoved(0, size);
                if (label.contains("Top Headlines")) {
                    AsyncHTTPTask asyncHTTPTask = new AsyncHTTPTask();
                    asyncHTTPTask.execute(News_url);

                } else if (label.contains("World News")) {
                    AsyncHTTPTask asyncHTTPTask = new AsyncHTTPTask();
                    asyncHTTPTask.execute(News_url2);
                } else if(label.contains("Sports")){
                    AsyncHTTPTask asyncHTTPTask = new AsyncHTTPTask();
                    asyncHTTPTask.execute(News_url3);
                } else if(label.contains("Science")){
                    AsyncHTTPTask asyncHTTPTask = new AsyncHTTPTask();
                    asyncHTTPTask.execute(News_url4);
                } else{
                    AsyncHTTPTask asyncHTTPTask = new AsyncHTTPTask();
                    asyncHTTPTask.execute(News_url5);
                }
                //mRecyclerView3.smoothScrollToPosition(12);
                //startActivity(getIntent());
            }
        });

        mRecyclerView2 = view.findViewById(R.id.mWeatherView);
// Create an adapter and supply the data to be displayed.
        mAdapter2 = new WordListAdapter4(view.getContext(), mWordList4);
// Connect the adapter with the RecyclerView.
        mRecyclerView2.setAdapter(mAdapter2);
// Give the RecyclerView a default layout manager.
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        Weather quote = new Weather();
        String content2;
        try {
            content2 = quote.execute(quote_url).get();
            JSONObject jsonObject = new JSONObject(content2);
            String items = jsonObject.getString("items");
            //Log.d("MyActivity", "efef" + items);
            JSONArray array = new JSONArray(items);
            JSONObject itemPart = array.getJSONObject(0);
            quoteDescrip = itemPart.getString("description");
            author = itemPart.getString("title");
            String txtQuoteFinal = quoteDescrip + "\n - " + author;
            // Log.d("MyActivity", quoteDescrip + "\n" + " - " + author);
            quoteTxt.setText(txtQuoteFinal);

            //description = weatherPart.getString("description");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onTaskCompleted(String result) {
        Log.d("MyActivity", "result: " + result);
        if(finalCountryCode==null) {
            finalCountryCode = result;
            Log.d("MyActivity", "countrycode: " + finalCountryCode);
            News_url = "https://newsapi.org/v2/top-headlines?country=" + finalCountryCode + "&apiKey=fe66c98440484bd7b1194bfed831f40a";
            News_url2 = "https://newsapi.org/v2/top-headlines?sources=bbc-news,al-jazeera-english,associated-press,bloomberg,reuters&apiKey=fe66c98440484bd7b1194bfed831f40a";
            News_url3 = "https://newsapi.org/v2/top-headlines?country=" + finalCountryCode + "&category=sports&apiKey=fe66c98440484bd7b1194bfed831f40a";
            News_url4 = "https://newsapi.org/v2/top-headlines?country=" + finalCountryCode + "&category=science&category=technology&apiKey=fe66c98440484bd7b1194bfed831f40a";
            News_url5 = "https://newsapi.org/v2/top-headlines?country=" + finalCountryCode + "&category=business&apiKey=fe66c98440484bd7b1194bfed831f40a";

            AsyncHTTPTask asyncHTTPTask = new AsyncHTTPTask();
            asyncHTTPTask.execute(News_url);
            searchNews.performClick();
        }

    }


    public class Weather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class AsyncHTTPTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                String response = streamToString(urlConnection.getInputStream());
                parseResult(response);
                return result;


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String data;
        String result = "";
        while ((data = bufferedReader.readLine()) != null) {
            result += data;
        }
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    @Override
    public void onDestroyView() {
        Log.d("MyActivity", "destroyedd");
       // int position = newsSpinner.getSelectedItemPosition();
        //newsSpinner.setSelection(position);
        super.onDestroyView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d("MyActivity", "restoreddd");

        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("MyActivity", "viewwwww");

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        Log.d("MyActivity", "stoppeddd");

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("MyActivity", "destroyedddddddddd");
        //int position = newsSpinner.getSelectedItemPosition();
        //newsSpinner.setSelection(position);
        super.onDestroy();
    }

    private void parseResult(String result) {
        JSONObject response = null;
        try {
            response = new JSONObject(result);
            JSONArray articles = response.optJSONArray("articles");
            int wordListSize2 = mWordList5.size();
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.optJSONObject(i);
                JSONObject source = article.optJSONObject("source");
                String newsSource = source.optString("name");
                String title = article.optString("title");
                String url = article.optString("url");
                String[] array = title.split("-");
                String finalTitle = "";
                for (int j = 0; j < array.length - 1; j++) {
                    if (j > 0)
                        finalTitle = finalTitle + "-" + array[j];
                    else
                        finalTitle = finalTitle + array[j];

                }
                if (finalTitle.equals("")) {
                    finalTitle = array[0];
                }
                //Log.d("MyActivity", "Titles: " + finalTitle + "-" + newsSource);
                mWordList5.add(new Model3(finalTitle, newsSource, url));
            }
            //mAdapter3 = new WordListAdapter5(getContext(), mWordList5);
// Connect the adapter with the RecyclerView.
            Log.d("MyActivity", "refreshhhh");
            //mRecyclerView3.setAdapter(mAdapter3);
            mRecyclerView3.smoothScrollToPosition(0);
            mRecyclerView3.getAdapter().notifyItemInserted(wordListSize2);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(),
                            mLocationCallback,
                            null /* Looper */);

        }
    }

    private void stopTrackingLocation(){
        if (mTrackingLocation) {
            mTrackingLocation = false;
        }
    }
    public void getWeather(boolean checked){


        try {       //http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=2b01cc136b56e8b7e0705cb925435a44
            Weather weather = new Weather();
            String content;
            content = weather.execute(Weather_url).get();
            int size = mWordList4.size();
            mWordList4.clear();
            mRecyclerView2.getAdapter().notifyItemRangeRemoved(0, size);
            // Log.d("MyActivity", content);

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("hourly");
            String timeZone = jsonObject.getString("timezone");
            //Log.d("MyActivity", "fdfdf" + weatherData);
            JSONObject jsonObject2 = new JSONObject(content);
            String weatherData2 = jsonObject.getString("current");
            //Log.d("MyActivity", "fdfdf" + weatherData2);

            JSONArray array = new JSONArray(weatherData);
            String main = "";
            String description = "";
            int wordListSize2 = mWordList4.size();
            for (int i = 0; i < array.length(); i++) {
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("dt");
                String temp = weatherPart.getString("temp");
                String feelsLike = weatherPart.getString("feels_like");
                String weatherObject = weatherPart.getString("weather");
                // Log.d("MyActivity", "" +weatherObject);
                JSONArray array2 = new JSONArray(weatherObject);
                JSONObject type = array2.getJSONObject(0);
                description = type.getString("description");
                int celsiusFeelsLike;
                int celsius;
                if(checked == false) {
                    celsius = (int) Double.parseDouble(temp) - 273;
                    celsiusFeelsLike = (int) Double.parseDouble(feelsLike) - 273;

                }else{

                    celsius = (int) ((Double.parseDouble(temp) - 273)*9/5) + 32;
                    celsiusFeelsLike = (int) ((Double.parseDouble(feelsLike) - 273)*9/5) + 32;
                    Log.d("MyActivity", "far: " + celsius);
                }
                long epochTime = Integer.parseInt(main);
                Date date = new Date(epochTime * 1000);
                DateFormat format = new SimpleDateFormat("dd/MM HH:mm");
                format.setTimeZone(TimeZone.getTimeZone(timeZone));
                String formatted = format.format(date);
                //System.out.println(formatted);

                mWordList4.add(new Model2(Integer.toString(celsius), Integer.toString(celsiusFeelsLike), description, formatted));
                //description = weatherPart.getString("description");


            }

//            mAdapter2 = new WordListAdapter4(getContext(), mWordList4);
//// Connect the adapter with the RecyclerView.
//            mRecyclerView2.setAdapter(mAdapter2);
            mRecyclerView2.smoothScrollToPosition(0);
            mRecyclerView2.getAdapter().notifyItemInserted(wordListSize2);





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                Log.d("MyActivity", "working kindaaa");
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            "Location Permission Granted",
                            Toast.LENGTH_SHORT).show();
                    startTrackingLocation();
                } else {
                    Toast.makeText(getActivity(),
                            "Location Permission Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }
    @Override
    public void onPause() {
        if (mTrackingLocation) {
            stopTrackingLocation();
            mTrackingLocation = true;
        }
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.d("MyActivity", "starttt");

        if (mTrackingLocation) {
            startTrackingLocation();
            //getWeather();
        }

       // newsSpinner.setSelection(0);
        //AsyncHTTPTask asyncHTTPTask = new AsyncHTTPTask();
        //asyncHTTPTask.execute(News_url);
        //searchNews.performClick();
        super.onStart();
    }

    @Override
    public void onResume() {
        if (mTrackingLocation) {
            Log.d("MyActivity", "resuminggg");
            startTrackingLocation();
        }

       // int position = newsSpinner.getSelectedItemPosition();
        //newsSpinner.setSelection(0);
        //searchNews.performClick();

        super.onResume();
    }



}


