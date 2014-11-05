package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


public class MainActivity extends Activity {

    private int hours = 0;
    private int minutes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
        TimePicker mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        Button mButton = (Button) findViewById(R.id.button);

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                MainActivity.this.hours = hourOfDay;
                MainActivity.this.minutes = minute;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((MainActivity.this.hours == 0) && (MainActivity.this.minutes == 0)) {
                    Toast.makeText(MainActivity.this, "Please set the timer first!", Toast.LENGTH_LONG);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
