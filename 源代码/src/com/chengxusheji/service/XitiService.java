package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Course;
import com.chengxusheji.po.Xiti;

import com.chengxusheji.mapper.XitiMapper;
@Service
public class XitiService {

	@Resource XitiMapper xitiMapper;
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

    /*添加习题记录*/
    public void addXiti(Xiti xiti) throws Exception {
    	xitiMapper.addXiti(xiti);
    }

    /*按照查询条件分页查询习题记录*/
    public ArrayList<Xiti> queryXiti(Course courseObj,String title,String xitiTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != courseObj &&  courseObj.getCourseNo() != null  && !courseObj.getCourseNo().equals(""))  where += " and t_xiti.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!title.equals("")) where = where + " and t_xiti.title like '%" + title + "%'";
    	if(!xitiTime.equals("")) where = where + " and t_xiti.xitiTime like '%" + xitiTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return xitiMapper.queryXiti(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Xiti> queryXiti(Course courseObj,String title,String xitiTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != courseObj &&  courseObj.getCourseNo() != null && !courseObj.getCourseNo().equals(""))  where += " and t_xiti.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!title.equals("")) where = where + " and t_xiti.title like '%" + title + "%'";
    	if(!xitiTime.equals("")) where = where + " and t_xiti.xitiTime like '%" + xitiTime + "%'";
    	return xitiMapper.queryXitiList(where);
    }

    /*查询所有习题记录*/
    public ArrayList<Xiti> queryAllXiti()  throws Exception {
        return xitiMapper.queryXitiList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Course courseObj,String title,String xitiTime) throws Exception {
     	String where = "where 1=1";
    	if(null != courseObj &&  courseObj.getCourseNo() != null && !courseObj.getCourseNo().equals(""))  where += " and t_xiti.courseObj='" + courseObj.getCourseNo() + "'";
    	if(!title.equals("")) where = where + " and t_xiti.title like '%" + title + "%'";
    	if(!xitiTime.equals("")) where = where + " and t_xiti.xitiTime like '%" + xitiTime + "%'";
        recordNumber = xitiMapper.queryXitiCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取习题记录*/
    public Xiti getXiti(int xitiId) throws Exception  {
        Xiti xiti = xitiMapper.getXiti(xitiId);
        return xiti;
    }

    /*更新习题记录*/
    public void updateXiti(Xiti xiti) throws Exception {
        xitiMapper.updateXiti(xiti);
    }

    /*删除一条习题记录*/
    public void deleteXiti (int xitiId) throws Exception {
        xitiMapper.deleteXiti(xitiId);
    }

    /*删除多条习题信息*/
    public int deleteXitis (String xitiIds) throws Exception {
    	String _xitiIds[] = xitiIds.split(",");
    	for(String _xitiId: _xitiIds) {
    		xitiMapper.deleteXiti(Integer.parseInt(_xitiId));
    	}
    	return _xitiIds.length;
    }
}
