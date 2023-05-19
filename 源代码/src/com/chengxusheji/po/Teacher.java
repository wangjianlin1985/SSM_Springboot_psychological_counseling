package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Teacher {
    /*教师工号*/
    @NotEmpty(message="教师工号不能为空")
    private String teacherNo;
    public String getTeacherNo(){
        return teacherNo;
    }
    public void setTeacherNo(String teacherNo){
        this.teacherNo = teacherNo;
    }

    /*登录密码*/
    @NotEmpty(message="登录密码不能为空")
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*教师姓名*/
    @NotEmpty(message="教师姓名不能为空")
    private String teacherName;
    public String getTeacherName() {
        return teacherName;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /*教师性别*/
    @NotEmpty(message="教师性别不能为空")
    private String teacherSex;
    public String getTeacherSex() {
        return teacherSex;
    }
    public void setTeacherSex(String teacherSex) {
        this.teacherSex = teacherSex;
    }

    /*教师照片*/
    private String teacherPhoto;
    public String getTeacherPhoto() {
        return teacherPhoto;
    }
    public void setTeacherPhoto(String teacherPhoto) {
        this.teacherPhoto = teacherPhoto;
    }

    /*入职日期*/
    @NotEmpty(message="入职日期不能为空")
    private String comeDate;
    public String getComeDate() {
        return comeDate;
    }
    public void setComeDate(String comeDate) {
        this.comeDate = comeDate;
    }

    /*教师介绍*/
    @NotEmpty(message="教师介绍不能为空")
    private String teacherDesc;
    public String getTeacherDesc() {
        return teacherDesc;
    }
    public void setTeacherDesc(String teacherDesc) {
        this.teacherDesc = teacherDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonTeacher=new JSONObject(); 
		jsonTeacher.accumulate("teacherNo", this.getTeacherNo());
		jsonTeacher.accumulate("password", this.getPassword());
		jsonTeacher.accumulate("teacherName", this.getTeacherName());
		jsonTeacher.accumulate("teacherSex", this.getTeacherSex());
		jsonTeacher.accumulate("teacherPhoto", this.getTeacherPhoto());
		jsonTeacher.accumulate("comeDate", this.getComeDate().length()>19?this.getComeDate().substring(0,19):this.getComeDate());
		jsonTeacher.accumulate("teacherDesc", this.getTeacherDesc());
		return jsonTeacher;
    }}