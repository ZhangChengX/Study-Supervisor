package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements LocationListener {

    private static String TAG = "Keep Study";

    //this data will be replaced by DB data
    //private static final String[] m = {"Data Base I", "Android app develop", "Analysis of Algorithm", "Operation System"};

    private SimpleAdapter adapter;
    private DataBaseOpenHelper dbHelper;
    private List<Course> courses;

    private StudyInf studyInf;

    private LocationManager mLocationManager;
    private Location mLocation;

    private String courseName;
    private long courseId;
    private double longitude;
    private double latitude;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TimePicker mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(1);
        mTimePicker.setCurrentMinute(30);

        final ImageButton mButton = (ImageButton)findViewById(R.id.ImageButton_start);
        mButton.setBackground(getResources().getDrawable(R.drawable.play));
        //final Button mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mLocation != null) {
                    longitude = mLocation.getLongitude();
                    latitude = mLocation.getLatitude();
                    log("Longitude: " + longitude + "\n" + "Latitude: " + latitude);

                } else {
                    log("No available location found.");
                    longitude = 0;
                    latitude = 0;
                }

                hour = mTimePicker.getCurrentHour();
                minute = mTimePicker.getCurrentMinute();

                //Toast.makeText(MainActivity.this, mTimePicker.getCurrentHour() + ":" + mTimePicker.getCurrentMinute()
                //        , Toast.LENGTH_LONG).show();
                if (courseName == null) {

                    Toast.makeText(MainActivity.this, "Please choose the course!", Toast.LENGTH_LONG).show();

                } else if ((hour == 0) && (minute == 0)) {

                    Toast.makeText(MainActivity.this, "Please set the timer first!", Toast.LENGTH_LONG).show();

                } else {

                    AlertDialog.Builder build = new AlertDialog.Builder(
                            MainActivity.this)
                            .setTitle("Start Confirm")
                            .setMessage("I swear to GOD! I will keep learning  and never touch my phone!")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setNegativeButton("No,I don't",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            // TODO Auto-generated method stub
                                            arg0.dismiss();
                                        }
                                    })
                            .setPositiveButton("Yes, I do",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub
                                            dialog.dismiss();

                                            Toast.makeText(MainActivity.this, mTimePicker.getCurrentHour() + ":" + mTimePicker.getCurrentMinute(), Toast.LENGTH_LONG).show();

                                            //insert a new study inf into data base
                                            studyInf = new StudyInf();
                                            studyInf.setCourseId(courseId);
                                            studyInf.setCourseName(courseName);
                                            studyInf.setStartTimeHour(hour);
                                            studyInf.setStartTimeMinute(minute);
                                            studyInf.setLongitude(longitude);
                                            studyInf.setLatitude(latitude);
                                            InsertStudyIfo(studyInf);

                                            log(TAG + "--------" + "course name: " + courseName + "\n" + "course id: " + courseId + "\n" + "hour: " + hour + "\n" +
                                                    "minute: " + minute + "\n" + "longitude: " + longitude + "\n" + "latitude: " + latitude);

                                            Intent intentLockScreen = new Intent(MainActivity.this, LockScreenActivity.class);
                                            intentLockScreen.putExtra("hour", hour);
                                            intentLockScreen.putExtra("minute", minute);
                                            startActivity(intentLockScreen);
                                        }
                                    });
                    build.show();
                }
            }
        });


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
        //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
        mLocation = getLocation(getApplicationContext());

    }

    protected void onResume() {
        super.onResume();

        //show the courses after user add or del course form setting view
        //keep the course data fresh
        courses = GetAllCourses();
        final Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        //Typeface tf = Typeface.createFromAsset(getAssets(), "DavysCrappyWrit.ttf");

        //connect adapter with data m
        adapter = new SimpleAdapter(this, setForListView(courses), R.layout.simple_spinner,
                new String[]{"course"}, new int[]{R.id.spinnertextview});
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                courseName = courses.get(position).getName();
                courseId = courses.get(position).getId();

                log(TAG + "-------" + "course Name: " + courseName);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);

        //show the courses after user add or del course form setting view
        //keep the course data fresh
        courses = GetAllCourses();
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        //connect adapter with data m
        adapter = new SimpleAdapter(this, setForListView(courses), R.layout.simple_spinner, new String[]{"course"}, new int[]{R.id.spinnertextview});
        mSpinner.setAdapter(adapter);
    }

    public static Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.
                getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //if (location == null) {
        //    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //}
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            log("Longitude:" + location.getLongitude() + "\n" + "Latitude:" + location.getLatitude());
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
                Intent intentSetting = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.report:
                //go to report Activity
                Intent intentReport = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intentReport);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public List GetAllCourses() {
        dbHelper = new DataBaseOpenHelper(getApplicationContext());
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();
        Course course;
        List<Course> courses = new ArrayList<Course>();
        Cursor cursor = mDB.rawQuery("select * from " + DataBaseOpenHelper.COURSE_TABLE_NAME, null);


        while (cursor.moveToNext()) {
            course = new Course();
            course.setId(cursor.getLong(0));
            course.setName(cursor.isNull(1) ? "" : cursor.getString(1));
            course.setGrade(cursor.isNull(2) ? 0 : cursor.getInt(2));
            course.setRemark(cursor.isNull(3) ? "" : cursor.getString(3));

            courses.add(course);
        }

        cursor.close();
        mDB.close();

        return courses;
    }

    public void InsertStudyIfo(StudyInf studyInf) {
        dbHelper = new DataBaseOpenHelper(MainActivity.this);
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        try{
            mDB.execSQL("INSERT INTO " + DataBaseOpenHelper.STUDY_TABLE_NAME +
                    "(c_id, c_name, start_time_hour, start_time_minute, longitude, latitude, remark) VALUES(?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{studyInf.getCourseId(), studyInf.getCourseName(), studyInf.getStartTimeHour(),
                    studyInf.getStartTimeMinute(), studyInf.getLongitude(), studyInf.getLatitude(), studyInf.getRemark()});
        }catch (android.database.SQLException e) {
            e.printStackTrace();
        }
        mDB.close();
    }

    public List<Map<String, String>> setForListView(List<Course> c) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map;

        for (int i = 0; i < c.size(); i++) {
            map = new HashMap<String, String>();
            map.put("id", Long.toString((c.get(i)).getId()));
            map.put("course", (c.get(i)).getName());
            list.add(map);
        }

        return list;
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
