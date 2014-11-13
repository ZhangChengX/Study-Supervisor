package com.example.sunxinzi.keepstudy;

/**
 * Created by luzhaoqian on 11/9/2014.
 */
public class Course {
    private long id;

    private String name;

    private int grade;

    private String remark;

    public Course() {
    }

    public Course(String name, int grade, String remark) {
        this.name = name;
        this.grade = grade;
        this.remark = remark;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
