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

    public static final String COURSE_TABLE_NAME = "Course";
    public static final String STUDY_TABLE_NAME = "Study";

    public static class CourseInf{
        public static String ID = "_id";
        public static String COURSE_NAME = "course_name";
        public static String GRADE = "grade";
        public static String REMARK = "remark";
    }

    public static class StudyInf{
        public static String ID = "_id";
        public static String COURSE_ID = "c_id";
        public static String COURSE_NAME = "c_name";
        public static String START_TIME = "start_time";
        public static String END_TIME = "end_time";
        public static String STUDY_TIME_LENGTH = "study_time_length";
        public static String PLANNED_TIME_LENGTH = "planned_time_length";
        public static String LATITUDE = "latitude";
        public static String LONGITUDE = "longitude";
        public static String REMARK = "remark";
    }

    private static final String COURSE_TABLE_CREATE = "create table " + COURSE_TABLE_NAME + " (" +
            CourseInf.ID + " INTEGER primary key autoincrement," +
            CourseInf.COURSE_NAME + " text, " +
            CourseInf.GRADE + " int, " +
            CourseInf.REMARK + " text);";
    private static final String STUDY_RECORDS_TABLE_CREATE = "create table " + STUDY_TABLE_NAME + "(" +
            StudyInf.ID + " INTEGER primary key autoincrement," +
            StudyInf.COURSE_ID + " int, " +
            StudyInf.COURSE_NAME + " text, " +
            StudyInf.START_TIME + " timestamp, " +
            StudyInf.END_TIME + " timestamp, " +
            StudyInf.STUDY_TIME_LENGTH + " REAL, " +
            StudyInf.PLANNED_TIME_LENGTH + " REAL, " +
            StudyInf.LATITUDE + " real, " +
            StudyInf.LONGITUDE + " real, " +
            StudyInf.REMARK + " text)";

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
