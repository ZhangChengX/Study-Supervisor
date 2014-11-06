package com.example.sunxinzi.keepstudy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by luzhaoqian on 11/6/2014.
 */
public class DataBaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDB.db";
    private static final int DATABASE_VERSION = 2;
    private static final String COURSE_TABLE_CREATE = "create table course (course_name text NOT NULL primary key, grade int, remark text);";
    private static final String STUDY_RECORDS_TABLE_CREATE = "create table study (ID int NOT NULL primary key autoincrement," +
            " c_name text, start_time  timestamp, end_time timestamp, study_time_length REAL, planned_time_length REAL, " +
            "latitude real, longitude real, remark text)";

    DataBaseOpenHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        try{
            db.execSQL(COURSE_TABLE_CREATE);
            db.execSQL(STUDY_RECORDS_TABLE_CREATE);
        }catch (android.database.SQLException e) {
            e.printStackTrace();
        }

    }

    public void onUpgrade(SQLiteDatabase db,int i, int j){};
}
