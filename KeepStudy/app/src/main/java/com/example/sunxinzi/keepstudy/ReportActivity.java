package com.example.sunxinzi.keepstudy;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    }
}
