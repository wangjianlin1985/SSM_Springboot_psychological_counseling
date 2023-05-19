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
import com.chengxusheji.service.KejianService;
import com.chengxusheji.po.Kejian;
import com.chengxusheji.service.CourseService;
import com.chengxusheji.po.Course;

//Kejian管理控制层
@Controller
@RequestMapping("/Kejian")
public class KejianController extends BaseController {

    /*业务层对象*/
    @Resource KejianService kejianService;

    @Resource CourseService courseService;
	@InitBinder("courseObj")
	public void initBindercourseObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("courseObj.");
	}
	@InitBinder("kejian")
	public void initBinderKejian(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("kejian.");
	}
	/*跳转到添加Kejian视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Kejian());
		/*查询所有的Course信息*/
		List<Course> courseList = courseService.queryAllCourse();
		request.setAttribute("courseList", courseList);
		return "Kejian_add";
	}

	/*客户端ajax方式提交添加课件信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Kejian kejian, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		kejian.setKejianFile(this.handleFileUpload(request, "kejianFileFile"));
        kejianService.addKejian(kejian);
        message = "课件添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询课件信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String title,@ModelAttribute("courseObj") Course courseObj,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (title == null) title = "";
		if (addTime == null) addTime = "";
		if(rows != 0)kejianService.setRows(rows);
		List<Kejian> kejianList = kejianService.queryKejian(title, courseObj, addTime, page);
	    /*计算总的页数和总的记录数*/
	    kejianService.queryTotalPageAndRecordNumber(title, courseObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = kejianService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = kejianService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Kejian kejian:kejianList) {
			JSONObject jsonKejian = kejian.getJsonObject();
			jsonArray.put(jsonKejian);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询课件信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Kejian> kejianList = kejianService.queryAllKejian();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Kejian kejian:kejianList) {
			JSONObject jsonKejian = new JSONObject();
			jsonKejian.accumulate("kejianId", kejian.getKejianId());
			jsonKejian.accumulate("title", kejian.getTitle());
			jsonArray.put(jsonKejian);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询课件信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String title,@ModelAttribute("courseObj") Course courseObj,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (title == null) title = "";
		if (addTime == null) addTime = "";
		List<Kejian> kejianList = kejianService.queryKejian(title, courseObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    kejianService.queryTotalPageAndRecordNumber(title, courseObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = kejianService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = kejianService.getRecordNumber();
	    request.setAttribute("kejianList",  kejianList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("title", title);
	    request.setAttribute("courseObj", courseObj);
	    request.setAttribute("addTime", addTime);
	    List<Course> courseList = courseService.queryAllCourse();
	    request.setAttribute("courseList", courseList);
		return "Kejian/kejian_frontquery_result"; 
	}

     /*前台查询Kejian信息*/
	@RequestMapping(value="/{kejianId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer kejianId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键kejianId获取Kejian对象*/
        Kejian kejian = kejianService.getKejian(kejianId);

        List<Course> courseList = courseService.queryAllCourse();
        request.setAttribute("courseList", courseList);
        request.setAttribute("kejian",  kejian);
        return "Kejian/kejian_frontshow";
	}

	/*ajax方式显示课件修改jsp视图页*/
	@RequestMapping(value="/{kejianId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer kejianId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键kejianId获取Kejian对象*/
        Kejian kejian = kejianService.getKejian(kejianId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonKejian = kejian.getJsonObject();
		out.println(jsonKejian.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新课件信息*/
	@RequestMapping(value = "/{kejianId}/update", method = RequestMethod.POST)
	public void update(@Validated Kejian kejian, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String kejianFileFileName = this.handleFileUpload(request, "kejianFileFile");
		if(!kejianFileFileName.equals(""))kejian.setKejianFile(kejianFileFileName);
		try {
			kejianService.updateKejian(kejian);
			message = "课件更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "课件更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除课件信息*/
	@RequestMapping(value="/{kejianId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer kejianId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  kejianService.deleteKejian(kejianId);
	            request.setAttribute("message", "课件删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "课件删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条课件记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String kejianIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = kejianService.deleteKejians(kejianIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出课件信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String title,@ModelAttribute("courseObj") Course courseObj,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<Kejian> kejianList = kejianService.queryKejian(title,courseObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Kejian信息记录"; 
        String[] headers = { "课件id","课件标题","所属心理课程","发布时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<kejianList.size();i++) {
        	Kejian kejian = kejianList.get(i); 
        	dataset.add(new String[]{kejian.getKejianId() + "",kejian.getTitle(),kejian.getCourseObj().getCourseName(),kejian.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Kejian.xls");//filename是下载的xls的名，建议最好用英文 
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
