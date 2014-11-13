package com.example.sunxinzi.keepstudy;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
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
     *
     * @param startDate
     * @param endDate
     */
    public void getTotalHours(String startDate, String endDate) {

    }

    /**
     * 
     * @param startDate
     * @param endDate
     */
    public void getLocationSet(String startDate, String endDate) {

    }

}
