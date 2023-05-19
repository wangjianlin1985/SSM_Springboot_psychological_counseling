﻿package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ClassInfo;

import com.chengxusheji.mapper.ClassInfoMapper;
@Service
public class ClassInfoService {

	@Resource ClassInfoMapper classInfoMapper;
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

    /*添加班级记录*/
    public void addClassInfo(ClassInfo classInfo) throws Exception {
    	classInfoMapper.addClassInfo(classInfo);
    }

    /*按照查询条件分页查询班级记录*/
    public ArrayList<ClassInfo> queryClassInfo(String classNo,String className,String bornDate,String mainTeacher,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!classNo.equals("")) where = where + " and t_classInfo.classNo like '%" + classNo + "%'";
    	if(!className.equals("")) where = where + " and t_classInfo.className like '%" + className + "%'";
    	if(!bornDate.equals("")) where = where + " and t_classInfo.bornDate like '%" + bornDate + "%'";
    	if(!mainTeacher.equals("")) where = where + " and t_classInfo.mainTeacher like '%" + mainTeacher + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return classInfoMapper.queryClassInfo(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ClassInfo> queryClassInfo(String classNo,String className,String bornDate,String mainTeacher) throws Exception  { 
     	String where = "where 1=1";
    	if(!classNo.equals("")) where = where + " and t_classInfo.classNo like '%" + classNo + "%'";
    	if(!className.equals("")) where = where + " and t_classInfo.className like '%" + className + "%'";
    	if(!bornDate.equals("")) where = where + " and t_classInfo.bornDate like '%" + bornDate + "%'";
    	if(!mainTeacher.equals("")) where = where + " and t_classInfo.mainTeacher like '%" + mainTeacher + "%'";
    	return classInfoMapper.queryClassInfoList(where);
    }

    /*查询所有班级记录*/
    public ArrayList<ClassInfo> queryAllClassInfo()  throws Exception {
        return classInfoMapper.queryClassInfoList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String classNo,String className,String bornDate,String mainTeacher) throws Exception {
     	String where = "where 1=1";
    	if(!classNo.equals("")) where = where + " and t_classInfo.classNo like '%" + classNo + "%'";
    	if(!className.equals("")) where = where + " and t_classInfo.className like '%" + className + "%'";
    	if(!bornDate.equals("")) where = where + " and t_classInfo.bornDate like '%" + bornDate + "%'";
    	if(!mainTeacher.equals("")) where = where + " and t_classInfo.mainTeacher like '%" + mainTeacher + "%'";
        recordNumber = classInfoMapper.queryClassInfoCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取班级记录*/
    public ClassInfo getClassInfo(String classNo) throws Exception  {
        ClassInfo classInfo = classInfoMapper.getClassInfo(classNo);
        return classInfo;
    }

    /*更新班级记录*/
    public void updateClassInfo(ClassInfo classInfo) throws Exception {
        classInfoMapper.updateClassInfo(classInfo);
    }

    /*删除一条班级记录*/
    public void deleteClassInfo (String classNo) throws Exception {
        classInfoMapper.deleteClassInfo(classNo);
    }

    /*删除多条班级信息*/
    public int deleteClassInfos (String classNos) throws Exception {
    	String _classNos[] = classNos.split(",");
    	for(String _classNo: _classNos) {
    		classInfoMapper.deleteClassInfo(_classNo);
    	}
    	return _classNos.length;
    }
}
