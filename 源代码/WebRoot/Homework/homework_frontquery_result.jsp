<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Homework" %>
<%@ page import="com.chengxusheji.po.Course" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Homework> homeworkList = (List<Homework>)request.getAttribute("homeworkList");
    //获取所有的courseObj信息
    List<Course> courseList = (List<Course>)request.getAttribute("courseList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Course courseObj = (Course)request.getAttribute("courseObj");
    String taskTitle = (String)request.getAttribute("taskTitle"); //作业任务查询关键字
    String homeworkDate = (String)request.getAttribute("homeworkDate"); //作业日期查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>家庭作业查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#homeworkListPanel" aria-controls="homeworkListPanel" role="tab" data-toggle="tab">家庭作业列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Homework/homework_frontAdd.jsp" style="display:none;">添加家庭作业</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="homeworkListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>作业心理课程</td><td>作业任务</td><td>作业文件</td><td>作业日期</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<homeworkList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Homework homework = homeworkList.get(i); //获取到家庭作业对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=homework.getCourseObj().getCourseName() %></td>
 											<td><%=homework.getTaskTitle() %></td>
 											<td><%=homework.getTaskFile().equals("")?"暂无文件":"<a href='" + basePath + homework.getTaskFile() + "' target='_blank'>" + homework.getTaskFile() + "</a>"%>
 											<td><%=homework.getHomeworkDate() %></td>
 											<td>
 												<a href="<%=basePath  %>Homework/<%=homework.getHomeworkId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="homeworkEdit('<%=homework.getHomeworkId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="homeworkDelete('<%=homework.getHomeworkId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

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
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>家庭作业查询</h1>
		</div>
		<form name="homeworkQueryForm" id="homeworkQueryForm" action="<%=basePath %>Homework/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="courseObj_courseNo">作业心理课程：</label>
                <select id="courseObj_courseNo" name="courseObj.courseNo" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(Course courseTemp:courseList) {
	 					String selected = "";
 					if(courseObj!=null && courseObj.getCourseNo()!=null && courseObj.getCourseNo().equals(courseTemp.getCourseNo()))
 						selected = "selected";
	 				%>
 				 <option value="<%=courseTemp.getCourseNo() %>" <%=selected %>><%=courseTemp.getCourseName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="taskTitle">作业任务:</label>
				<input type="text" id="taskTitle" name="taskTitle" value="<%=taskTitle %>" class="form-control" placeholder="请输入作业任务">
			</div>






			<div class="form-group">
				<label for="homeworkDate">作业日期:</label>
				<input type="text" id="homeworkDate" name="homeworkDate" class="form-control"  placeholder="请选择作业日期" value="<%=homeworkDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="homeworkEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;家庭作业信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="homeworkEditForm" id="homeworkEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="homework_homeworkId_edit" class="col-md-3 text-right">作业id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="homework_homeworkId_edit" name="homework.homeworkId" class="form-control" placeholder="请输入作业id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="homework_courseObj_courseNo_edit" class="col-md-3 text-right">作业心理课程:</label>
		  	 <div class="col-md-9">
			    <select id="homework_courseObj_courseNo_edit" name="homework.courseObj.courseNo" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="homework_taskTitle_edit" class="col-md-3 text-right">作业任务:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="homework_taskTitle_edit" name="homework.taskTitle" class="form-control" placeholder="请输入作业任务">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="homework_taskContent_edit" class="col-md-3 text-right">作业要求:</label>
		  	 <div class="col-md-9">
			    <textarea id="homework_taskContent_edit" name="homework.taskContent" rows="8" class="form-control" placeholder="请输入作业要求"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="homework_taskFile_edit" class="col-md-3 text-right">作业文件:</label>
		  	 <div class="col-md-9">
			    <a id="homework_taskFileA" target="_blank"></a><br/>
			    <input type="hidden" id="homework_taskFile" name="homework.taskFile"/>
			    <input id="taskFileFile" name="taskFileFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="homework_homeworkDate_edit" class="col-md-3 text-right">作业日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date homework_homeworkDate_edit col-md-12" data-link-field="homework_homeworkDate_edit"  data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="homework_homeworkDate_edit" name="homework.homeworkDate" size="16" type="text" value="" placeholder="请选择作业日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		</form> 
	    <style>#homeworkEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxHomeworkModify();">提交</button>
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
    document.homeworkQueryForm.currentPage.value = currentPage;
    document.homeworkQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.homeworkQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.homeworkQueryForm.currentPage.value = pageValue;
    documenthomeworkQueryForm.submit();
}

/*弹出修改家庭作业界面并初始化数据*/
function homeworkEdit(homeworkId) {
	$.ajax({
		url :  basePath + "Homework/" + homeworkId + "/update",
		type : "get",
		dataType: "json",
		success : function (homework, response, status) {
			if (homework) {
				$("#homework_homeworkId_edit").val(homework.homeworkId);
				$.ajax({
					url: basePath + "Course/listAll",
					type: "get",
					success: function(courses,response,status) { 
						$("#homework_courseObj_courseNo_edit").empty();
						var html="";
		        		$(courses).each(function(i,course){
		        			html += "<option value='" + course.courseNo + "'>" + course.courseName + "</option>";
		        		});
		        		$("#homework_courseObj_courseNo_edit").html(html);
		        		$("#homework_courseObj_courseNo_edit").val(homework.courseObjPri);
					}
				});
				$("#homework_taskTitle_edit").val(homework.taskTitle);
				$("#homework_taskContent_edit").val(homework.taskContent);
				$("#homework_taskFile").val(homework.taskFile);
				$("#homework_taskFileA").text(homework.taskFile);
				$("#homework_taskFileA").attr("href", basePath +　homework.taskFile);
				$("#homework_homeworkDate_edit").val(homework.homeworkDate);
				$('#homeworkEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除家庭作业信息*/
function homeworkDelete(homeworkId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Homework/deletes",
			data : {
				homeworkIds : homeworkId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#homeworkQueryForm").submit();
					//location.href= basePath + "Homework/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交家庭作业信息表单给服务器端修改*/
function ajaxHomeworkModify() {
	$.ajax({
		url :  basePath + "Homework/" + $("#homework_homeworkId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#homeworkEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#homeworkQueryForm").submit();
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

    /*作业日期组件*/
    $('.homework_homeworkDate_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd',
    	minView: 2,
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

