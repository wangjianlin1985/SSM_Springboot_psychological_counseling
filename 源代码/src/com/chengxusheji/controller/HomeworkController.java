package com.chengxusheji.controller;

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
import com.chengxusheji.service.HomeworkService;
import com.chengxusheji.po.Homework;
import com.chengxusheji.service.CourseService;
import com.chengxusheji.po.Course;

//Homework管理控制层
@Controller
@RequestMapping("/Homework")
public class HomeworkController extends BaseController {

    /*业务层对象*/
    @Resource HomeworkService homeworkService;

    @Resource CourseService courseService;
	@InitBinder("courseObj")
	public void initBindercourseObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("courseObj.");
	}
	@InitBinder("homework")
	public void initBinderHomework(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("homework.");
	}
	/*跳转到添加Homework视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Homework());
		/*查询所有的Course信息*/
		List<Course> courseList = courseService.queryAllCourse();
		request.setAttribute("courseList", courseList);
		return "Homework_add";
	}

	/*客户端ajax方式提交添加家庭作业信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Homework homework, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		homework.setTaskFile(this.handleFileUpload(request, "taskFileFile"));
        homeworkService.addHomework(homework);
        message = "家庭作业添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询家庭作业信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("courseObj") Course courseObj,String taskTitle,String homeworkDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (taskTitle == null) taskTitle = "";
		if (homeworkDate == null) homeworkDate = "";
		if(rows != 0)homeworkService.setRows(rows);
		List<Homework> homeworkList = homeworkService.queryHomework(courseObj, taskTitle, homeworkDate, page);
	    /*计算总的页数和总的记录数*/
	    homeworkService.queryTotalPageAndRecordNumber(courseObj, taskTitle, homeworkDate);
	    /*获取到总的页码数目*/
	    int totalPage = homeworkService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = homeworkService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Homework homework:homeworkList) {
			JSONObject jsonHomework = homework.getJsonObject();
			jsonArray.put(jsonHomework);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询家庭作业信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Homework> homeworkList = homeworkService.queryAllHomework();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Homework homework:homeworkList) {
			JSONObject jsonHomework = new JSONObject();
			jsonHomework.accumulate("homeworkId", homework.getHomeworkId());
			jsonHomework.accumulate("taskTitle", homework.getTaskTitle());
			jsonArray.put(jsonHomework);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询家庭作业信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("courseObj") Course courseObj,String taskTitle,String homeworkDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (taskTitle == null) taskTitle = "";
		if (homeworkDate == null) homeworkDate = "";
		List<Homework> homeworkList = homeworkService.queryHomework(courseObj, taskTitle, homeworkDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    homeworkService.queryTotalPageAndRecordNumber(courseObj, taskTitle, homeworkDate);
	    /*获取到总的页码数目*/
	    int totalPage = homeworkService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = homeworkService.getRecordNumber();
	    request.setAttribute("homeworkList",  homeworkList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("courseObj", courseObj);
	    request.setAttribute("taskTitle", taskTitle);
	    request.setAttribute("homeworkDate", homeworkDate);
	    List<Course> courseList = courseService.queryAllCourse();
	    request.setAttribute("courseList", courseList);
		return "Homework/homework_frontquery_result"; 
	}

     /*前台查询Homework信息*/
	@RequestMapping(value="/{homeworkId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer homeworkId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键homeworkId获取Homework对象*/
        Homework homework = homeworkService.getHomework(homeworkId);

        List<Course> courseList = courseService.queryAllCourse();
        request.setAttribute("courseList", courseList);
        request.setAttribute("homework",  homework);
        return "Homework/homework_frontshow";
	}

	/*ajax方式显示家庭作业修改jsp视图页*/
	@RequestMapping(value="/{homeworkId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer homeworkId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键homeworkId获取Homework对象*/
        Homework homework = homeworkService.getHomework(homeworkId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonHomework = homework.getJsonObject();
		out.println(jsonHomework.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新家庭作业信息*/
	@RequestMapping(value = "/{homeworkId}/update", method = RequestMethod.POST)
	public void update(@Validated Homework homework, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String taskFileFileName = this.handleFileUpload(request, "taskFileFile");
		if(!taskFileFileName.equals(""))homework.setTaskFile(taskFileFileName);
		try {
			homeworkService.updateHomework(homework);
			message = "家庭作业更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "家庭作业更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除家庭作业信息*/
	@RequestMapping(value="/{homeworkId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer homeworkId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  homeworkService.deleteHomework(homeworkId);
	            request.setAttribute("message", "家庭作业删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "家庭作业删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条家庭作业记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String homeworkIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = homeworkService.deleteHomeworks(homeworkIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出家庭作业信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("courseObj") Course courseObj,String taskTitle,String homeworkDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(taskTitle == null) taskTitle = "";
        if(homeworkDate == null) homeworkDate = "";
        List<Homework> homeworkList = homeworkService.queryHomework(courseObj,taskTitle,homeworkDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Homework信息记录"; 
        String[] headers = { "作业id","作业心理课程","作业任务","作业日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<homeworkList.size();i++) {
        	Homework homework = homeworkList.get(i); 
        	dataset.add(new String[]{homework.getHomeworkId() + "",homework.getCourseObj().getCourseName(),homework.getTaskTitle(),homework.getHomeworkDate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Homework.xls");//filename是下载的xls的名，建议最好用英文 
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
