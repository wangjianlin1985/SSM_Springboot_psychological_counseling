package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Homework;

public interface HomeworkMapper {
	/*添加家庭作业信息*/
	public void addHomework(Homework homework) throws Exception;

	/*按照查询条件分页查询家庭作业记录*/
	public ArrayList<Homework> queryHomework(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有家庭作业记录*/
	public ArrayList<Homework> queryHomeworkList(@Param("where") String where) throws Exception;

	/*按照查询条件的家庭作业记录数*/
	public int queryHomeworkCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条家庭作业记录*/
	public Homework getHomework(int homeworkId) throws Exception;

	/*更新家庭作业记录*/
	public void updateHomework(Homework homework) throws Exception;

	/*删除家庭作业记录*/
	public void deleteHomework(int homeworkId) throws Exception;

}
