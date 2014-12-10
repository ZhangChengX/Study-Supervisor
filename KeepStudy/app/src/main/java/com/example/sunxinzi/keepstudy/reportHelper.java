package com.example.sunxinzi.keepstudy;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Cheng on 11/12/2014.
 */
public class ReportHelper {

    SQLiteDatabase db;
    DataBaseOpenHelper dbHelper;
    Context context;

    public ReportHelper(Context context) {
        this.context = context;
    }

    /**
     * get all courses and grades, return HashMap
     */
    public HashMap getCourses() {
        HashMap mHashMap = new HashMap();
        dbHelper = new DataBaseOpenHelper(this.context);
        db = dbHelper.getReadableDatabase();
        String sql = "select * from " + DataBaseOpenHelper.COURSE_TABLE_NAME;
        Log.i("Log", sql);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Course course =  new Course();
            course.setId(cursor.getLong(0));
            course.setName(cursor.getString(cursor.getColumnIndex("course_name")));
            course.setGrade(cursor.getColumnIndex("grade"));
            mHashMap.put(cursor.getLong(0), course);
        }
        return mHashMap;
    }

    /**
     * get all courses and grades, return ArrayList
     * @return
     */
    public List getCourseList() {
        dbHelper = new DataBaseOpenHelper(this.context);
        db = dbHelper.getReadableDatabase();
        String sql = "select * from " + DataBaseOpenHelper.COURSE_TABLE_NAME;
        Log.i("Log", sql);
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList mArrayList = new ArrayList();
        while (cursor.moveToNext()) {
            Course course = new Course();
            course.setId(cursor.getLong(0));
            course.setName(cursor.isNull(1) ? "" : cursor.getString(1));
            course.setGrade(cursor.isNull(2) ? 0 : cursor.getInt(2));
            mArrayList.add(course);
        }
        return mArrayList;
    }

    /**
     * get total hours
     * @return
     */
    public HashMap getTotalHours() {
        HashMap mHashMap = new HashMap();
        dbHelper = new DataBaseOpenHelper(this.context);
        db = dbHelper.getReadableDatabase();
        String sql = "select _id, c_name, sum(study_time_length) from " + DataBaseOpenHelper.STUDY_TABLE_NAME + " group by c_name";
        Log.i("Log", sql);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            StudyInf studyInf =  new StudyInf();
            studyInf.setId(cursor.getLong(0));
            studyInf.setCourseName(cursor.getString(cursor.getColumnIndex("c_name")));
            studyInf.setStudyTimeLength(cursor.getLong(2));
            mHashMap.put(cursor.getLong(0), studyInf);
        }
        return mHashMap;
    }

    /**
     *
     */
    public HashMap getLocationSet() {
        HashMap mHashMap = new HashMap();
        dbHelper = new DataBaseOpenHelper(this.context);
        db = dbHelper.getReadableDatabase();
        String sql = "select _id, c_name, latitude, longitude, study_time_length " +
                     "from " + DataBaseOpenHelper.STUDY_TABLE_NAME;
        Log.i("Log", sql);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            StudyInf studyInf =  new StudyInf();
            studyInf.setId(cursor.getLong(0));
            studyInf.setCourseName(cursor.getString(1));
            studyInf.setLatitude(cursor.getDouble(2));
            studyInf.setLongitude(cursor.getDouble(3));
            studyInf.setStudyTimeLength(cursor.getFloat(4));
            mHashMap.put(cursor.getLong(0), studyInf);
        }
        return mHashMap;
    }

    /**
     *
     * @param s
     * @return
     */
    public String formatTime(float s) {
        int second = (int) s;
        int hour = second / 3600;
        second = second % 3600;
        int minte = second / 60;
        second = second % 60;
        //return hour + "hour " + minte + "minute " + second + "second";
        return hour + minte/60 + " hour";
    }
    /**
     *
     * @param location
     * @return
     */
    public String buildGoogleMap(HashMap location) {
        String marker = "";
        String myLatlng = "";
        Iterator mIterator = location.entrySet().iterator();
        int i = 1;
        while (mIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) mIterator.next();
            StudyInf val = (StudyInf) entry.getValue();
            marker += "var marker" + i + " = new google.maps.Marker({" +
                      "position: new google.maps.LatLng(" + val.getLatitude() + "," + val.getLongitude() + ")," +
                      "map: map," +
                      "title: '" + val.getCourseName() + "'" +
                      "});";
            marker += "var infowindow" + i + " = new google.maps.InfoWindow({" +
                      "  content: 'Course Name:" + val.getCourseName() + " <br> Study Length:" + formatTime(val.getStudyTimeLength()) + "'" +
                      "});";
            marker += "google.maps.event.addListener(marker" + i + ", 'click', function() {" +
                      "  infowindow" + i + ".open(map,marker" + i + ");" +
                      "});";
            if (1 == i) {
                myLatlng += "new google.maps.LatLng(" + val.getLatitude() + "," + val.getLongitude() + ");";
            }
            i++;
        }
        String html ="<!doctype html> <html> <head> <meta charset='utf-8'>" +
                "<meta name='viewport' content='initial-scale=1.0, user-scalable=no'>" +
                "<style>html, body, #map-canvas { height: 100%; margin: 0px; padding: 0px } </style>" +
                "<script src='http://maps.google.com/maps/api/js?key=AIzaSyBtwlFYGTYu0ovv9x24R-D0y9nOMRhUAYo'></script>" +
                "</head>" +
                "<body>" +
                "<div id='map-canvas'>Loading...</div>" +
                "<script>" +
                "function initialize() {" +
                "  var myLatlng = " + myLatlng +
                "  var mapOptions = {" +
                "  zoom: 10," +
                "  center: myLatlng" +
                "  };" +
                "  var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);" +
                "  var marker = new google.maps.Marker({" +
                "  position: myLatlng," +
                "  map: map" +
                "  });" +
                    marker +
                "}" +
                "google.maps.event.addDomListener(window, 'load', initialize);" +
                "</script>" +
                "</body> </html>";
        return html;
    }

}
