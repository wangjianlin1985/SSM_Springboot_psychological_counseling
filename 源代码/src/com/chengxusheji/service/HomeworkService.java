package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Course;
import com.chengxusheji.po.Homework;

import com.chengxusheji.mapper.HomeworkMapper;
@Service
public class HomeworkService {

	@Resource HomeworkMapper homeworkMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加家庭作业记录*/
    public void addHomework(Homework homework) throws Exception {
    	homeworkMapper.addHomework(homework);
    }

    /*按照查询条件分页查询家庭作业记录*/
    public ArrayList<Homework> queryHomework(Course courseObj,String taskTitle,String homeworkDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != courseObj &&  courseObj.getCourseNo() != null  && !courseObj.getCourseNo().equals(""))  where += " and t_homework.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!taskTitle.equals("")) where = where + " and t_homework.taskTitle like '%" + taskTitle + "%'";
    	if(!homeworkDate.equals("")) where = where + " and t_homework.homeworkDate like '%" + homeworkDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return homeworkMapper.queryHomework(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Homework> queryHomework(Course courseObj,String taskTitle,String homeworkDate) throws Exception  { 
     	String where = "where 1=1";
    	if(null != courseObj &&  courseObj.getCourseNo() != null && !courseObj.getCourseNo().equals(""))  where += " and t_homework.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!taskTitle.equals("")) where = where + " and t_homework.taskTitle like '%" + taskTitle + "%'";
    	if(!homeworkDate.equals("")) where = where + " and t_homework.homeworkDate like '%" + homeworkDate + "%'";
    	return homeworkMapper.queryHomeworkList(where);
    }

    /*查询所有家庭作业记录*/
    public ArrayList<Homework> queryAllHomework()  throws Exception {
        return homeworkMapper.queryHomeworkList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Course courseObj,String taskTitle,String homeworkDate) throws Exception {
     	String where = "where 1=1";
    	if(null != courseObj &&  courseObj.getCourseNo() != null && !courseObj.getCourseNo().equals(""))  where += " and t_homework.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!taskTitle.equals("")) where = where + " and t_homework.taskTitle like '%" + taskTitle + "%'";
    	if(!homeworkDate.equals("")) where = where + " and t_homework.homeworkDate like '%" + homeworkDate + "%'";
        recordNumber = homeworkMapper.queryHomeworkCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取家庭作业记录*/
    public Homework getHomework(int homeworkId) throws Exception  {
        Homework homework = homeworkMapper.getHomework(homeworkId);
        return homework;
    }

    /*更新家庭作业记录*/
    public void updateHomework(Homework homework) throws Exception {
        homeworkMapper.updateHomework(homework);
    }

    /*删除一条家庭作业记录*/
    public void deleteHomework (int homeworkId) throws Exception {
        homeworkMapper.deleteHomework(homeworkId);
    }

    /*删除多条家庭作业信息*/
    public int deleteHomeworks (String homeworkIds) throws Exception {
    	String _homeworkIds[] = homeworkIds.split(",");
    	for(String _homeworkId: _homeworkIds) {
    		homeworkMapper.deleteHomework(Integer.parseInt(_homeworkId));
    	}
    	return _homeworkIds.length;
    }
}
