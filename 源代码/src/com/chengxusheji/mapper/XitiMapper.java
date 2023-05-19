package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Xiti;

public interface XitiMapper {
	/*添加习题信息*/
	public void addXiti(Xiti xiti) throws Exception;

	/*按照查询条件分页查询习题记录*/
	public ArrayList<Xiti> queryXiti(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有习题记录*/
	public ArrayList<Xiti> queryXitiList(@Param("where") String where) throws Exception;

	/*按照查询条件的习题记录数*/
	public int queryXitiCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条习题记录*/
	public Xiti getXiti(int xitiId) throws Exception;

	/*更新习题记录*/
	public void updateXiti(Xiti xiti) throws Exception;

	/*删除习题记录*/
	public void deleteXiti(int xitiId) throws Exception;

}
