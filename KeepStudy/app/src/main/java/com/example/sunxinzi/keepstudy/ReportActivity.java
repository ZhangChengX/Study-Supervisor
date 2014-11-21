package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by SunXinzi on 14/11/5.
 */

public class ReportActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);

        // init spinner
        String[] reportName = {"Courses Report", "Total Hours", "Studied Location"};
        Spinner spinner = (Spinner) findViewById(R.id.spinnerReport);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                reportName
        );
        // set up style of dropdown list
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // add adapter into spinner
        spinner.setAdapter(adapter);
        // listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (i) {
                    case 0:
                        Log.i("Log", "Selected:" + i);
                        fragmentTransaction.replace(R.id.fragmentContainer, new CourseFragment());
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        Log.i("Log", "Selected:" + i);
                        fragmentTransaction.replace(R.id.fragmentContainer, new TotalHoursFragment());
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        Log.i("Log", "Selected:" + i);
                        fragmentTransaction.replace(R.id.fragmentContainer, new StudiedLocationFragment());
                        fragmentTransaction.commit();
                        break;
                    //default:
                    //    Log.i("Log", "Default Selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
