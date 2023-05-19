﻿package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.CourseService;
import com.chengxusheji.po.Course;
import com.chengxusheji.service.TeacherService;
import com.chengxusheji.po.Teacher;

//Course管理控制层
@Controller
@RequestMapping("/Course")
public class CourseController extends BaseController {

    /*业务层对象*/
    @Resource CourseService courseService;

    @Resource TeacherService teacherService;
	@InitBinder("teacherObj")
	public void initBinderteacherObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("teacherObj.");
	}
	@InitBinder("course")
	public void initBinderCourse(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("course.");
	}
	/*跳转到添加Course视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Course());
		/*查询所有的Teacher信息*/
		List<Teacher> teacherList = teacherService.queryAllTeacher();
		request.setAttribute("teacherList", teacherList);
		return "Course_add";
	}

	/*客户端ajax方式提交添加心理课程信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Course course, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(courseService.getCourse(course.getCourseNo()) != null) {
			message = "心理课程编号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			course.setCoursePhoto(this.handlePhotoUpload(request, "coursePhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        courseService.addCourse(course);
        message = "心理课程添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询心理课程信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String courseNo,String courseName,@ModelAttribute("teacherObj") Teacher teacherObj,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (courseNo == null) courseNo = "";
		if (courseName == null) courseName = "";
		if(rows != 0)courseService.setRows(rows);
		List<Course> courseList = courseService.queryCourse(courseNo, courseName, teacherObj, page);
	    /*计算总的页数和总的记录数*/
	    courseService.queryTotalPageAndRecordNumber(courseNo, courseName, teacherObj);
	    /*获取到总的页码数目*/
	    int totalPage = courseService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = courseService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Course course:courseList) {
			JSONObject jsonCourse = course.getJsonObject();
			jsonArray.put(jsonCourse);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询心理课程信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Course> courseList = courseService.queryAllCourse();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Course course:courseList) {
			JSONObject jsonCourse = new JSONObject();
			jsonCourse.accumulate("courseNo", course.getCourseNo());
			jsonCourse.accumulate("courseName", course.getCourseName());
			jsonArray.put(jsonCourse);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询心理课程信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String courseNo,String courseName,@ModelAttribute("teacherObj") Teacher teacherObj,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (courseNo == null) courseNo = "";
		if (courseName == null) courseName = "";
		List<Course> courseList = courseService.queryCourse(courseNo, courseName, teacherObj, currentPage);
	    /*计算总的页数和总的记录数*/
	    courseService.queryTotalPageAndRecordNumber(courseNo, courseName, teacherObj);
	    /*获取到总的页码数目*/
	    int totalPage = courseService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = courseService.getRecordNumber();
	    request.setAttribute("courseList",  courseList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("courseNo", courseNo);
	    request.setAttribute("courseName", courseName);
	    request.setAttribute("teacherObj", teacherObj);
	    List<Teacher> teacherList = teacherService.queryAllTeacher();
	    request.setAttribute("teacherList", teacherList);
		return "Course/course_frontquery_result"; 
	}

     /*前台查询Course信息*/
	@RequestMapping(value="/{courseNo}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String courseNo,Model model,HttpServletRequest request) throws Exception {
		/*根据主键courseNo获取Course对象*/
        Course course = courseService.getCourse(courseNo);

        List<Teacher> teacherList = teacherService.queryAllTeacher();
        request.setAttribute("teacherList", teacherList);
        request.setAttribute("course",  course);
        return "Course/course_frontshow";
	}

	/*ajax方式显示心理课程修改jsp视图页*/
	@RequestMapping(value="/{courseNo}/update",method=RequestMethod.GET)
	public void update(@PathVariable String courseNo,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键courseNo获取Course对象*/
        Course course = courseService.getCourse(courseNo);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonCourse = course.getJsonObject();
		out.println(jsonCourse.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新心理课程信息*/
	@RequestMapping(value = "/{courseNo}/update", method = RequestMethod.POST)
	public void update(@Validated Course course, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String coursePhotoFileName = this.handlePhotoUpload(request, "coursePhotoFile");
		if(!coursePhotoFileName.equals("upload/NoImage.jpg"))course.setCoursePhoto(coursePhotoFileName); 


		try {
			courseService.updateCourse(course);
			message = "心理课程更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "心理课程更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除心理课程信息*/
	@RequestMapping(value="/{courseNo}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String courseNo,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  courseService.deleteCourse(courseNo);
	            request.setAttribute("message", "心理课程删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "心理课程删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条心理课程记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String courseNos,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = courseService.deleteCourses(courseNos);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出心理课程信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String courseNo,String courseName,@ModelAttribute("teacherObj") Teacher teacherObj, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(courseNo == null) courseNo = "";
        if(courseName == null) courseName = "";
        List<Course> courseList = courseService.queryCourse(courseNo,courseName,teacherObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Course信息记录"; 
        String[] headers = { "心理课程编号","心理课程名称","心理课程图片","上课心理辅导老师","心理课程学时"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<courseList.size();i++) {
        	Course course = courseList.get(i); 
        	dataset.add(new String[]{course.getCourseNo(),course.getCourseName(),course.getCoursePhoto(),course.getTeacherObj().getTeacherName(),course.getCourseHours() + ""});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Course.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
