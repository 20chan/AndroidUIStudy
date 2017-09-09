package com.example.philly.androiduistury;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LocationManager loc;
    LocationListener listener;
    TextView logger;

    String TAG = "GPSService";

    public static String[] PERMS = {"android.permission.INTERNET",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"};

    private boolean hasPermissions(String[] perms) {
        for(String p: perms) {
            if (checkCallingOrSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission(String[] perms) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermissions(PERMS)) requestPermission(PERMS);

        loc = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = loc.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = loc.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        logger = (TextView) findViewById(R.id.textView);
        logger.append("isGPSEnabled: " + isGPSEnabled + "\n");
        logger.append("isNetworkEnabled: " + isNetworkEnabled + "\n");

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                logger.append("latitude: " + lat + ", longitude: " + lng + "\n");
                Log.d(TAG, "onLocationChanged: lat: " + lat + ", lng: " + lng);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        try {
            loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        } catch (SecurityException e)
        {
            Log.d(TAG, e.getMessage());
        }
    }
}
