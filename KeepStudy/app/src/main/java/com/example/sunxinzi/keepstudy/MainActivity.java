package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class MainActivity extends Activity implements LocationListener {

    private static String TAG = "Keep Study";

    //this data will be replaced by DB data
    private static final String[] m = {"Data Base I", "Android app develop", "Analysis of Algorithm", "Operation System"};

    private ArrayAdapter<String> adapter;
    private DataBaseOpenHelper mDBOpenHelper;
    private SQLiteDatabase mDB;
    private SimpleCursorAdapter mCruserAdapter;

    private LocationManager mLocationManager;
    private Location mLocation;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create DB and table
        mDBOpenHelper = new DataBaseOpenHelper(this);
        mDB = mDBOpenHelper.getWritableDatabase();


        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        //connect adapter with data m
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);
        mSpinner.setAdapter(adapter);

        final TimePicker mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);

        Button mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mLocation != null) {
                    longitude = mLocation.getLongitude();
                    latitude = mLocation.getLatitude();
                    log("Longitude: " + longitude + "\n" + "Latitude: " + latitude);
                } else {
                    log("No available location found.");
                }

                Toast.makeText(MainActivity.this, mTimePicker.getCurrentHour() + ":" + mTimePicker.getCurrentMinute()
                        , Toast.LENGTH_LONG).show();
                if ((mTimePicker.getCurrentHour() == 0) && (mTimePicker.getCurrentMinute() == 0)) {
                    Toast.makeText(MainActivity.this, "Please set the timer first!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intentLockScreen = new Intent(MainActivity.this, LockScreenActivity.class);
                    startActivity(intentLockScreen);
                }
            }
        });


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
        //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
        mLocation = getLocation(MainActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        mLocationManager.removeUpdates(this);

    }

    public static Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.
                getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //if (location == null) {
        //    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //}
        log(TAG + "------location: " + location);
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            log("经度：" + location.getLongitude() + "\n" + "纬度" + location.getLatitude());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
        switch (item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case R.id.setting:
                //go to setting Activity
                Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intentSetting);
            case R.id.report:
                //go to report Activity
                Intent intentReport = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intentReport);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}
