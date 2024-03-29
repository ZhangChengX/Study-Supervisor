package com.example.sunxinzi.keepstudy;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by SunXinzi on 14/11/5.
 */

public class SettingActivity extends ListActivity {

    private DataBaseOpenHelper dbHelper;
    private SimpleAdapter  adapter;
    List<Course> courses;
    Course course;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setFooterDividersEnabled(true);
        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null);
        getListView().addFooterView(footerView);
//        courses = GetAllCourses();
//
//        //setting SimpleAdapter,create two textBoxes and one button for delete
//        adapter = SetViewAdapter(courses);
//
//        setListAdapter(adapter);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(SettingActivity.this);

                new AlertDialog.Builder(SettingActivity.this).setTitle("Input Course Name").
                        setView(editText).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (editText.getText() == null || editText.getText().toString().equals("")) {
                            Toast.makeText(SettingActivity.this, "Please input a eligible course name!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            course = new Course();
                            course.setName(editText.getText().toString());

                            InsertCourse(course);

                            //reset view,the system should call onResume() automatically, but it doesn't!
                            //So, here I call the onResume() manually to refresh the UI
                            onResume();

                        }

                    }
                }).setNegativeButton("No", null).show();
            }
        });

    }

    protected void onResume() {
        super.onResume();
        courses = GetAllCourses();
        adapter = SetViewAdapter(courses);
        setListAdapter(adapter);
    }

    public List GetAllCourses(){
        dbHelper = new DataBaseOpenHelper(SettingActivity.this);
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

    public void DeleteCourse(String id){
        dbHelper = new DataBaseOpenHelper(SettingActivity.this);
        SQLiteDatabase mDB = dbHelper.getReadableDatabase();
        try{
            mDB.execSQL("DELETE FROM " + DataBaseOpenHelper.COURSE_TABLE_NAME + " WHERE _id= " + id + ";");
        }catch (android.database.SQLException e) {
            e.printStackTrace();
        }

        mDB.close();
    }

    public void InsertCourse(Course course) {
        dbHelper = new DataBaseOpenHelper(SettingActivity.this);
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        try{
        mDB.execSQL("INSERT INTO " + DataBaseOpenHelper.COURSE_TABLE_NAME + "(course_name, grade, remark) VALUES(?, ?, ?)",
                new Object[] { course.getName(), course.getName(), course.getRemark()});
        }catch (android.database.SQLException e) {
            e.printStackTrace();
        }
        mDB.close();
    }

    public void UpdateCourse(Course course){
        dbHelper = new DataBaseOpenHelper(SettingActivity.this);
        SQLiteDatabase mDB = dbHelper.getWritableDatabase();
        try{
        mDB.execSQL("UPDATE " + DataBaseOpenHelper.COURSE_TABLE_NAME + " SET course_name=?, grade=?, remark=? WHERE _id=?",
                new Object[] {course.getName(), course.getGrade(), course.getRemark(), course.getId()});
        }catch (android.database.SQLException e) {
            e.printStackTrace();
        }
        mDB.close();
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

    public SimpleAdapter SetViewAdapter(List<Course> course){

       return new SimpleAdapter(this, setForListView(courses), R.layout.setting_course, new String[] {"course", "id"}, new int[] { R.id.course_name, R.id.course_id}) {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            //For adding delete button listener
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ImageButton button = (ImageButton) v.findViewById(R.id.imageButton_del);
                button.setBackground(getResources().getDrawable(R.drawable.circle_delete));

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(SettingActivity.this).setTitle("Delete this course?").setNegativeButton("No", null).
                                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String id = ((HashMap<String, String>) getItem(position)).get("id");
                                        DeleteCourse(id);

                                        //reset view,the system should call onResume() automatically, but it doesn't!
                                        //So, here I call the onResume() manually to refresh the UI
                                        onResume();
                                    }
                                }).show();



                    }
                });

                return v;
            }
        };
    }
}

