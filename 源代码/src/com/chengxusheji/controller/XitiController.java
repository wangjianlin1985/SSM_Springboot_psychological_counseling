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
import com.chengxusheji.service.XitiService;
import com.chengxusheji.po.Xiti;
import com.chengxusheji.service.CourseService;
import com.chengxusheji.po.Course;

//Xiti管理控制层
@Controller
@RequestMapping("/Xiti")
public class XitiController extends BaseController {

    /*业务层对象*/
    @Resource XitiService xitiService;

    @Resource CourseService courseService;
	@InitBinder("courseObj")
	public void initBindercourseObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("courseObj.");
	}
	@InitBinder("xiti")
	public void initBinderXiti(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("xiti.");
	}
	/*跳转到添加Xiti视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Xiti());
		/*查询所有的Course信息*/
		List<Course> courseList = courseService.queryAllCourse();
		request.setAttribute("courseList", courseList);
		return "Xiti_add";
	}

	/*客户端ajax方式提交添加习题信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Xiti xiti, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        xitiService.addXiti(xiti);
        message = "习题添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询习题信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("courseObj") Course courseObj,String title,String xitiTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (title == null) title = "";
		if (xitiTime == null) xitiTime = "";
		if(rows != 0)xitiService.setRows(rows);
		List<Xiti> xitiList = xitiService.queryXiti(courseObj, title, xitiTime, page);
	    /*计算总的页数和总的记录数*/
	    xitiService.queryTotalPageAndRecordNumber(courseObj, title, xitiTime);
	    /*获取到总的页码数目*/
	    int totalPage = xitiService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = xitiService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Xiti xiti:xitiList) {
			JSONObject jsonXiti = xiti.getJsonObject();
			jsonArray.put(jsonXiti);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询习题信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Xiti> xitiList = xitiService.queryAllXiti();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Xiti xiti:xitiList) {
			JSONObject jsonXiti = new JSONObject();
			jsonXiti.accumulate("xitiId", xiti.getXitiId());
			jsonXiti.accumulate("title", xiti.getTitle());
			jsonArray.put(jsonXiti);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询习题信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("courseObj") Course courseObj,String title,String xitiTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (title == null) title = "";
		if (xitiTime == null) xitiTime = "";
		List<Xiti> xitiList = xitiService.queryXiti(courseObj, title, xitiTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    xitiService.queryTotalPageAndRecordNumber(courseObj, title, xitiTime);
	    /*获取到总的页码数目*/
	    int totalPage = xitiService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = xitiService.getRecordNumber();
	    request.setAttribute("xitiList",  xitiList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("courseObj", courseObj);
	    request.setAttribute("title", title);
	    request.setAttribute("xitiTime", xitiTime);
	    List<Course> courseList = courseService.queryAllCourse();
	    request.setAttribute("courseList", courseList);
		return "Xiti/xiti_frontquery_result"; 
	}

     /*前台查询Xiti信息*/
	@RequestMapping(value="/{xitiId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer xitiId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键xitiId获取Xiti对象*/
        Xiti xiti = xitiService.getXiti(xitiId);

        List<Course> courseList = courseService.queryAllCourse();
        request.setAttribute("courseList", courseList);
        request.setAttribute("xiti",  xiti);
        return "Xiti/xiti_frontshow";
	}

	/*ajax方式显示习题修改jsp视图页*/
	@RequestMapping(value="/{xitiId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer xitiId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键xitiId获取Xiti对象*/
        Xiti xiti = xitiService.getXiti(xitiId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonXiti = xiti.getJsonObject();
		out.println(jsonXiti.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新习题信息*/
	@RequestMapping(value = "/{xitiId}/update", method = RequestMethod.POST)
	public void update(@Validated Xiti xiti, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			xitiService.updateXiti(xiti);
			message = "习题更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "习题更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除习题信息*/
	@RequestMapping(value="/{xitiId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer xitiId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  xitiService.deleteXiti(xitiId);
	            request.setAttribute("message", "习题删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "习题删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条习题记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String xitiIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = xitiService.deleteXitis(xitiIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出习题信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("courseObj") Course courseObj,String title,String xitiTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(title == null) title = "";
        if(xitiTime == null) xitiTime = "";
        List<Xiti> xitiList = xitiService.queryXiti(courseObj,title,xitiTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Xiti信息记录"; 
        String[] headers = { "习题id","所属心理课程","习题标题","添加时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<xitiList.size();i++) {
        	Xiti xiti = xitiList.get(i); 
        	dataset.add(new String[]{xiti.getXitiId() + "",xiti.getCourseObj().getCourseName(),xiti.getTitle(),xiti.getXitiTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Xiti.xls");//filename是下载的xls的名，建议最好用英文 
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
