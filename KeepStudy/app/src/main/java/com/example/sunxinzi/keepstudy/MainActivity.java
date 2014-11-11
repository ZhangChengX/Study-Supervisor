package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    //this data will be replaced by DB data
    //private static final String[] m = {"Data Base I", "Android app develop", "Analysis of Algorithm", "Operation System"};

    private SimpleAdapter adapter;
    private DataBaseOpenHelper dbHelper;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TimePicker mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        Button mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
    }

    protected void onResume() {
        super.onResume();

        //show the courses after user add or del course form setting view
        //keep the course data fresh
        courses = GetAllCourses();
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        //connect adapter with data m
        adapter = new SimpleAdapter(this, setForListView(courses), R.layout.simple_spinner, new String[] {"course"}, new int[] { R.id.spinnertextview});
        mSpinner.setAdapter(adapter);
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
            case R.id.report:
                //go to report Activity
                Intent intentReport = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intentReport);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public List GetAllCourses(){
        dbHelper = new DataBaseOpenHelper(getApplicationContext());
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();
        Course course;
        List<Course> courses = new ArrayList<Course>();
        Cursor cursor = mDB.rawQuery("select * from " + DataBaseOpenHelper.COURSE_TABLE_NAME, null);


        while(cursor.moveToNext()){
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

    public List<Map<String, String>> setForListView(List<Course> c){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map;

        for(int i=0; i< c.size(); i++){
            map = new HashMap<String, String>();
            map.put("id",Long.toString((c.get(i)).getId()));
            map.put("course", (c.get(i)).getName());
            list.add(map);
        }

        return list;
    }

}
