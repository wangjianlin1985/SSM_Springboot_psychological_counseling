package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Kejian {
    /*课件id*/
    private Integer kejianId;
    public Integer getKejianId(){
        return kejianId;
    }
    public void setKejianId(Integer kejianId){
        this.kejianId = kejianId;
    }

    /*课件标题*/
    @NotEmpty(message="课件标题不能为空")
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*所属心理课程*/
    private Course courseObj;
    public Course getCourseObj() {
        return courseObj;
    }
    public void setCourseObj(Course courseObj) {
        this.courseObj = courseObj;
    }

    /*课件描述*/
    @NotEmpty(message="课件描述不能为空")
    private String kejianDesc;
    public String getKejianDesc() {
        return kejianDesc;
    }
    public void setKejianDesc(String kejianDesc) {
        this.kejianDesc = kejianDesc;
    }

    /*课件文件*/
    private String kejianFile;
    public String getKejianFile() {
        return kejianFile;
    }
    public void setKejianFile(String kejianFile) {
        this.kejianFile = kejianFile;
    }

    /*发布时间*/
    @NotEmpty(message="发布时间不能为空")
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonKejian=new JSONObject(); 
		jsonKejian.accumulate("kejianId", this.getKejianId());
		jsonKejian.accumulate("title", this.getTitle());
		jsonKejian.accumulate("courseObj", this.getCourseObj().getCourseName());
		jsonKejian.accumulate("courseObjPri", this.getCourseObj().getCourseNo());
		jsonKejian.accumulate("kejianDesc", this.getKejianDesc());
		jsonKejian.accumulate("kejianFile", this.getKejianFile());
		jsonKejian.accumulate("addTime", this.getAddTime().length()>19?this.getAddTime().substring(0,19):this.getAddTime());
		return jsonKejian;
    }}