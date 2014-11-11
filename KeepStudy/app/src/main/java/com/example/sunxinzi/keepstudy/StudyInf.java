package com.example.sunxinzi.keepstudy;

import java.sql.Timestamp;
import android.location.Location;

/**
 * Created by luzhaoqian on 11/9/2014.
 */
public class StudyInf {

    private long id;

    private int courseId;

    private  String courseName;

    private Timestamp startTime;

    private Timestamp endTime;

    private float studyTimeLenth;

    private float plannedTimeLenth;

    private  Location location;

    private String remark;

    public StudyInf() {
    }

    public StudyInf(int courseId, String courseName, float plannedTimeLenth, Location location, String remark){
        this.courseId = courseId;
        this.courseName = courseName;
        this.plannedTimeLenth = plannedTimeLenth;
        this.location = location;
        this.remark = remark;
    }

    public long getId(){return id;}

    public void setId(long id){this.id = id;}

    public  int getCourseId(){return courseId;}

    public void setCourseId(int courseId){this.courseId = courseId;}

    public String getCourseName(){ return courseName;}

    public void setCourseName() { this.courseName = courseName;}

    public Timestamp getStartTime() {return startTime;}

    public void setStartTime(Timestamp startTime) {this.startTime = startTime;}

    public Timestamp getEndTime(){return endTime;}

    public void setEndTime(Timestamp endTime){this.endTime = endTime;}

    public float getStudyTimeLenth(){return  studyTimeLenth;}

    public void setStudyTimeLenth(float studyTimeLenth){this.studyTimeLenth = studyTimeLenth;}

    public float getPlannedTimeLenth(){return plannedTimeLenth;}

    public void setPlannedTimeLenth(float plannedTimeLenth){this.plannedTimeLenth = plannedTimeLenth;}

    public Location getLocation(){return location;}

    public void setLocation(Location location){this.location = location;}

    public String getRemark(){return remark;}

    public void setRemark(String remark){this.remark = remark;}
}
