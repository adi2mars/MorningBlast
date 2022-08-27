package com.example.morningblastv1;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.morningblastv1.TabFragment2.addressLab;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {
    private final String TAG = FetchAddressTask.class.getSimpleName();
    private Context mContext;
    private OnTaskCompleted mListener;
    Location location;

    interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
    FetchAddressTask(Context applicationContext, OnTaskCompleted listener) {
        mContext = applicationContext;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(mContext,
                Locale.getDefault());
         location = locations[0];
        List<Address> addresses = null;
        String resultMessage = "";
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address
                    1);
            if (addresses == null || addresses.size() == 0) {
                    Log.d("MyActivity", "No Address Found");
            }else {
                // If an address is found, read it into resultMessage
                Address address = addresses.get(0);
                ArrayList<String> addressParts = new ArrayList<>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressParts.add(address.getAddressLine(i));
                    Log.d("MyActivity", "code " + address);
                }
                String total = address.getLocality() +", " + address.getAdminArea();

                //addressLab.setText(total);
                Log.d("MyActivity", "final adress = " + total);
                resultMessage = address.getCountryCode();
                //resultMessage = TextUtils.join("\n", addressParts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values
            Log.e(TAG, "invalid" + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        return resultMessage;
    }
    @Override
    protected void onPostExecute(String address) {
        mListener.onTaskCompleted(address);
        super.onPostExecute(address);    }
}