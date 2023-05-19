package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Video {
    /*资料id*/
    private Integer videoId;
    public Integer getVideoId(){
        return videoId;
    }
    public void setVideoId(Integer videoId){
        this.videoId = videoId;
    }

    /*资料标题*/
    @NotEmpty(message="资料标题不能为空")
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

    /*资料介绍*/
    @NotEmpty(message="资料介绍不能为空")
    private String videoDesc;
    public String getVideoDesc() {
        return videoDesc;
    }
    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    /*资料文件*/
    private String videoFile;
    public String getVideoFile() {
        return videoFile;
    }
    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    /*录制时间*/
    @NotEmpty(message="录制时间不能为空")
    private String videoTime;
    public String getVideoTime() {
        return videoTime;
    }
    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonVideo=new JSONObject(); 
		jsonVideo.accumulate("videoId", this.getVideoId());
		jsonVideo.accumulate("title", this.getTitle());
		jsonVideo.accumulate("courseObj", this.getCourseObj().getCourseName());
		jsonVideo.accumulate("courseObjPri", this.getCourseObj().getCourseNo());
		jsonVideo.accumulate("videoDesc", this.getVideoDesc());
		jsonVideo.accumulate("videoFile", this.getVideoFile());
		jsonVideo.accumulate("videoTime", this.getVideoTime().length()>19?this.getVideoTime().substring(0,19):this.getVideoTime());
		return jsonVideo;
    }}