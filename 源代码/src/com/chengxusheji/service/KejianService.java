package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Course;
import com.chengxusheji.po.Kejian;

import com.chengxusheji.mapper.KejianMapper;
@Service
public class KejianService {

	@Resource KejianMapper kejianMapper;
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

    /*添加课件记录*/
    public void addKejian(Kejian kejian) throws Exception {
    	kejianMapper.addKejian(kejian);
    }

    /*按照查询条件分页查询课件记录*/
    public ArrayList<Kejian> queryKejian(String title,Course courseObj,String addTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!title.equals("")) where = where + " and t_kejian.title like '%" + title + "%'";
    	if(null != courseObj &&  courseObj.getCourseNo() != null  && !courseObj.getCourseNo().equals(""))  where += " and t_kejian.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_kejian.addTime like '%" + addTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return kejianMapper.queryKejian(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Kejian> queryKejian(String title,Course courseObj,String addTime) throws Exception  { 
     	String where = "where 1=1";
    	if(!title.equals("")) where = where + " and t_kejian.title like '%" + title + "%'";
    	if(null != courseObj &&  courseObj.getCourseNo() != null && !courseObj.getCourseNo().equals(""))  where += " and t_kejian.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_kejian.addTime like '%" + addTime + "%'";
    	return kejianMapper.queryKejianList(where);
    }

    /*查询所有课件记录*/
    public ArrayList<Kejian> queryAllKejian()  throws Exception {
        return kejianMapper.queryKejianList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String title,Course courseObj,String addTime) throws Exception {
     	String where = "where 1=1";
    	if(!title.equals("")) where = where + " and t_kejian.title like '%" + title + "%'";
    	if(null != courseObj &&  courseObj.getCourseNo() != null && !courseObj.getCourseNo().equals(""))  where += " and t_kejian.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_kejian.addTime like '%" + addTime + "%'";
        recordNumber = kejianMapper.queryKejianCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取课件记录*/
    public Kejian getKejian(int kejianId) throws Exception  {
        Kejian kejian = kejianMapper.getKejian(kejianId);
        return kejian;
    }

    /*更新课件记录*/
    public void updateKejian(Kejian kejian) throws Exception {
        kejianMapper.updateKejian(kejian);
    }

    /*删除一条课件记录*/
    public void deleteKejian (int kejianId) throws Exception {
        kejianMapper.deleteKejian(kejianId);
    }

    /*删除多条课件信息*/
    public int deleteKejians (String kejianIds) throws Exception {
    	String _kejianIds[] = kejianIds.split(",");
    	for(String _kejianId: _kejianIds) {
    		kejianMapper.deleteKejian(Integer.parseInt(_kejianId));
    	}
    	return _kejianIds.length;
    }
}
