package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Xiti {
    /*习题id*/
    private Integer xitiId;
    public Integer getXitiId(){
        return xitiId;
    }
    public void setXitiId(Integer xitiId){
        this.xitiId = xitiId;
    }

    /*所属心理课程*/
    private Course courseObj;
    public Course getCourseObj() {
        return courseObj;
    }
    public void setCourseObj(Course courseObj) {
        this.courseObj = courseObj;
    }

    /*习题标题*/
    @NotEmpty(message="习题标题不能为空")
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*习题内容*/
    @NotEmpty(message="习题内容不能为空")
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*添加时间*/
    @NotEmpty(message="添加时间不能为空")
    private String xitiTime;
    public String getXitiTime() {
        return xitiTime;
    }
    public void setXitiTime(String xitiTime) {
        this.xitiTime = xitiTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonXiti=new JSONObject(); 
		jsonXiti.accumulate("xitiId", this.getXitiId());
		jsonXiti.accumulate("courseObj", this.getCourseObj().getCourseName());
		jsonXiti.accumulate("courseObjPri", this.getCourseObj().getCourseNo());
		jsonXiti.accumulate("title", this.getTitle());
		jsonXiti.accumulate("content", this.getContent());
		jsonXiti.accumulate("xitiTime", this.getXitiTime().length()>19?this.getXitiTime().substring(0,19):this.getXitiTime());
		return jsonXiti;
    }}