package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Course {
    /*心理课程编号*/
    @NotEmpty(message="心理课程编号不能为空")
    private String courseNo;
    public String getCourseNo(){
        return courseNo;
    }
    public void setCourseNo(String courseNo){
        this.courseNo = courseNo;
    }

    /*心理课程名称*/
    @NotEmpty(message="心理课程名称不能为空")
    private String courseName;
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /*心理课程图片*/
    private String coursePhoto;
    public String getCoursePhoto() {
        return coursePhoto;
    }
    public void setCoursePhoto(String coursePhoto) {
        this.coursePhoto = coursePhoto;
    }

    /*上课心理辅导老师*/
    private Teacher teacherObj;
    public Teacher getTeacherObj() {
        return teacherObj;
    }
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }

    /*心理课程学时*/
    @NotNull(message="必须输入心理课程学时")
    private Integer courseHours;
    public Integer getCourseHours() {
        return courseHours;
    }
    public void setCourseHours(Integer courseHours) {
        this.courseHours = courseHours;
    }

    /*教学大纲*/
    @NotEmpty(message="教学大纲不能为空")
    private String jxff;
    public String getJxff() {
        return jxff;
    }
    public void setJxff(String jxff) {
        this.jxff = jxff;
    }

    /*心理课程简介*/
    @NotEmpty(message="心理课程简介不能为空")
    private String kcjj;
    public String getKcjj() {
        return kcjj;
    }
    public void setKcjj(String kcjj) {
        this.kcjj = kcjj;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonCourse=new JSONObject(); 
		jsonCourse.accumulate("courseNo", this.getCourseNo());
		jsonCourse.accumulate("courseName", this.getCourseName());
		jsonCourse.accumulate("coursePhoto", this.getCoursePhoto());
		jsonCourse.accumulate("teacherObj", this.getTeacherObj().getTeacherName());
		jsonCourse.accumulate("teacherObjPri", this.getTeacherObj().getTeacherNo());
		jsonCourse.accumulate("courseHours", this.getCourseHours());
		jsonCourse.accumulate("jxff", this.getJxff());
		jsonCourse.accumulate("kcjj", this.getKcjj());
		return jsonCourse;
    }}