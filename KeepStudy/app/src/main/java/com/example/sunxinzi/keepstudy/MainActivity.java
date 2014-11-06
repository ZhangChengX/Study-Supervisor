package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final String[] m={"Data Base I","Android app develop","Analysis of Algorithm","Operation System"};
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        //connect adapter with data m
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        mSpinner.setAdapter(adapter);

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
                    Intent intent = new Intent(MainActivity.this,
                            LockScreenActivity.class);
                }
            }
        });
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement



        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
