﻿package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Course;

public interface CourseMapper {
	/*添加心理课程信息*/
	public void addCourse(Course course) throws Exception;

	/*按照查询条件分页查询心理课程记录*/
	public ArrayList<Course> queryCourse(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有心理课程记录*/
	public ArrayList<Course> queryCourseList(@Param("where") String where) throws Exception;

	/*按照查询条件的心理课程记录数*/
	public int queryCourseCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条心理课程记录*/
	public Course getCourse(String courseNo) throws Exception;

	/*更新心理课程记录*/
	public void updateCourse(Course course) throws Exception;

	/*删除心理课程记录*/
	public void deleteCourse(String courseNo) throws Exception;

}
