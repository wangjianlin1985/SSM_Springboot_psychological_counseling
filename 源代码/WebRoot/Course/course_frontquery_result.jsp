<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Course" %>
<%@ page import="com.chengxusheji.po.Teacher" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Course> courseList = (List<Course>)request.getAttribute("courseList");
    //获取所有的teacherObj信息
    List<Teacher> teacherList = (List<Teacher>)request.getAttribute("teacherList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String courseNo = (String)request.getAttribute("courseNo"); //心理课程编号查询关键字
    String courseName = (String)request.getAttribute("courseName"); //心理课程名称查询关键字
    Teacher teacherObj = (Teacher)request.getAttribute("teacherObj");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>心理课程查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>Course/frontlist">心理课程信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>Course/course_frontAdd.jsp" style="display:none;">添加心理课程</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<courseList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		Course course = courseList.get(i); //获取到心理课程对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>Course/<%=course.getCourseNo() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=course.getCoursePhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		心理课程编号:<%=course.getCourseNo() %>
			     	</div>
			     	<div class="field">
	            		心理课程名称:<%=course.getCourseName() %>
			     	</div>
			     	<div class="field">
	            		上课心理辅导老师:<%=course.getTeacherObj().getTeacherName() %>
			     	</div>
			     	<div class="field">
	            		心理课程学时:<%=course.getCourseHours() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>Course/<%=course.getCourseNo() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="courseEdit('<%=course.getCourseNo() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="courseDelete('<%=course.getCourseNo() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>心理课程查询</h1>
		</div>
		<form name="courseQueryForm" id="courseQueryForm" action="<%=basePath %>Course/frontlist" class="mar_t15" method="post">
			<div class="form-group">
				<label for="courseNo">心理课程编号:</label>
				<input type="text" id="courseNo" name="courseNo" value="<%=courseNo %>" class="form-control" placeholder="请输入心理课程编号">
			</div>
			<div class="form-group">
				<label for="courseName">心理课程名称:</label>
				<input type="text" id="courseName" name="courseName" value="<%=courseName %>" class="form-control" placeholder="请输入心理课程名称">
			</div>
            <div class="form-group">
            	<label for="teacherObj_teacherNo">上课心理辅导老师：</label>
                <select id="teacherObj_teacherNo" name="teacherObj.teacherNo" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(Teacher teacherTemp:teacherList) {
	 					String selected = "";
 					if(teacherObj!=null && teacherObj.getTeacherNo()!=null && teacherObj.getTeacherNo().equals(teacherTemp.getTeacherNo()))
 						selected = "selected";
	 				%>
 				 <option value="<%=teacherTemp.getTeacherNo() %>" <%=selected %>><%=teacherTemp.getTeacherName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="courseEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;心理课程信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="courseEditForm" id="courseEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="course_courseNo_edit" class="col-md-3 text-right">心理课程编号:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="course_courseNo_edit" name="course.courseNo" class="form-control" placeholder="请输入心理课程编号" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="course_courseName_edit" class="col-md-3 text-right">心理课程名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="course_courseName_edit" name="course.courseName" class="form-control" placeholder="请输入心理课程名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="course_coursePhoto_edit" class="col-md-3 text-right">心理课程图片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="course_coursePhotoImg" border="0px"/><br/>
			    <input type="hidden" id="course_coursePhoto" name="course.coursePhoto"/>
			    <input id="coursePhotoFile" name="coursePhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="course_teacherObj_teacherNo_edit" class="col-md-3 text-right">上课心理辅导老师:</label>
		  	 <div class="col-md-9">
			    <select id="course_teacherObj_teacherNo_edit" name="course.teacherObj.teacherNo" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="course_courseHours_edit" class="col-md-3 text-right">心理课程学时:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="course_courseHours_edit" name="course.courseHours" class="form-control" placeholder="请输入心理课程学时">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="course_jxff_edit" class="col-md-3 text-right">教学大纲:</label>
		  	 <div class="col-md-9">
			    <textarea id="course_jxff_edit" name="course.jxff" rows="8" class="form-control" placeholder="请输入教学大纲"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="course_kcjj_edit" class="col-md-3 text-right">心理课程简介:</label>
		  	 <div class="col-md-9">
			    <textarea id="course_kcjj_edit" name="course.kcjj" rows="8" class="form-control" placeholder="请输入心理课程简介"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#courseEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxCourseModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.courseQueryForm.currentPage.value = currentPage;
    document.courseQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.courseQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.courseQueryForm.currentPage.value = pageValue;
    documentcourseQueryForm.submit();
}

/*弹出修改心理课程界面并初始化数据*/
function courseEdit(courseNo) {
	$.ajax({
		url :  basePath + "Course/" + courseNo + "/update",
		type : "get",
		dataType: "json",
		success : function (course, response, status) {
			if (course) {
				$("#course_courseNo_edit").val(course.courseNo);
				$("#course_courseName_edit").val(course.courseName);
				$("#course_coursePhoto").val(course.coursePhoto);
				$("#course_coursePhotoImg").attr("src", basePath +　course.coursePhoto);
				$.ajax({
					url: basePath + "Teacher/listAll",
					type: "get",
					success: function(teachers,response,status) { 
						$("#course_teacherObj_teacherNo_edit").empty();
						var html="";
		        		$(teachers).each(function(i,teacher){
		        			html += "<option value='" + teacher.teacherNo + "'>" + teacher.teacherName + "</option>";
		        		});
		        		$("#course_teacherObj_teacherNo_edit").html(html);
		        		$("#course_teacherObj_teacherNo_edit").val(course.teacherObjPri);
					}
				});
				$("#course_courseHours_edit").val(course.courseHours);
				$("#course_jxff_edit").val(course.jxff);
				$("#course_kcjj_edit").val(course.kcjj);
				$('#courseEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除心理课程信息*/
function courseDelete(courseNo) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Course/deletes",
			data : {
				courseNos : courseNo,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#courseQueryForm").submit();
					//location.href= basePath + "Course/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交心理课程信息表单给服务器端修改*/
function ajaxCourseModify() {
	$.ajax({
		url :  basePath + "Course/" + $("#course_courseNo_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#courseEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#courseQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

})
</script>
</body>
</html>

