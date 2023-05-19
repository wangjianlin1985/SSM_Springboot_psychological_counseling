package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Homework {
    /*作业id*/
    private Integer homeworkId;
    public Integer getHomeworkId(){
        return homeworkId;
    }
    public void setHomeworkId(Integer homeworkId){
        this.homeworkId = homeworkId;
    }

    /*作业心理课程*/
    private Course courseObj;
    public Course getCourseObj() {
        return courseObj;
    }
    public void setCourseObj(Course courseObj) {
        this.courseObj = courseObj;
    }

    /*作业任务*/
    @NotEmpty(message="作业任务不能为空")
    private String taskTitle;
    public String getTaskTitle() {
        return taskTitle;
    }
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    /*作业要求*/
    @NotEmpty(message="作业要求不能为空")
    private String taskContent;
    public String getTaskContent() {
        return taskContent;
    }
    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    /*作业文件*/
    private String taskFile;
    public String getTaskFile() {
        return taskFile;
    }
    public void setTaskFile(String taskFile) {
        this.taskFile = taskFile;
    }

    /*作业日期*/
    @NotEmpty(message="作业日期不能为空")
    private String homeworkDate;
    public String getHomeworkDate() {
        return homeworkDate;
    }
    public void setHomeworkDate(String homeworkDate) {
        this.homeworkDate = homeworkDate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonHomework=new JSONObject(); 
		jsonHomework.accumulate("homeworkId", this.getHomeworkId());
		jsonHomework.accumulate("courseObj", this.getCourseObj().getCourseName());
		jsonHomework.accumulate("courseObjPri", this.getCourseObj().getCourseNo());
		jsonHomework.accumulate("taskTitle", this.getTaskTitle());
		jsonHomework.accumulate("taskContent", this.getTaskContent());
		jsonHomework.accumulate("taskFile", this.getTaskFile());
		jsonHomework.accumulate("homeworkDate", this.getHomeworkDate().length()>19?this.getHomeworkDate().substring(0,19):this.getHomeworkDate());
		return jsonHomework;
    }}