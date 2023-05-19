﻿<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Kejian" %>
<%@ page import="com.chengxusheji.po.Course" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Kejian> kejianList = (List<Kejian>)request.getAttribute("kejianList");
    //获取所有的courseObj信息
    List<Course> courseList = (List<Course>)request.getAttribute("courseList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String title = (String)request.getAttribute("title"); //课件标题查询关键字
    Course courseObj = (Course)request.getAttribute("courseObj");
    String addTime = (String)request.getAttribute("addTime"); //发布时间查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>课件查询</title>
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
			    	<li role="presentation" class="active"><a href="#kejianListPanel" aria-controls="kejianListPanel" role="tab" data-toggle="tab">课件列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Kejian/kejian_frontAdd.jsp" style="display:none;">添加课件</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="kejianListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>课件标题</td><td>所属心理课程</td><td>课件文件</td><td>发布时间</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<kejianList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Kejian kejian = kejianList.get(i); //获取到课件对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=kejian.getTitle() %></td>
 											<td><%=kejian.getCourseObj().getCourseName() %></td>
 											<td><%=kejian.getKejianFile().equals("")?"暂无文件":"<a href='" + basePath + kejian.getKejianFile() + "' target='_blank'>" + kejian.getKejianFile() + "</a>"%>
 											<td><%=kejian.getAddTime() %></td>
 											<td>
 												<a href="<%=basePath  %>Kejian/<%=kejian.getKejianId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="kejianEdit('<%=kejian.getKejianId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="kejianDelete('<%=kejian.getKejianId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
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
    		<h1>课件查询</h1>
		</div>
		<form name="kejianQueryForm" id="kejianQueryForm" action="<%=basePath %>Kejian/frontlist" class="mar_t15" method="post">
			<div class="form-group">
				<label for="title">课件标题:</label>
				<input type="text" id="title" name="title" value="<%=title %>" class="form-control" placeholder="请输入课件标题">
			</div>






            <div class="form-group">
            	<label for="courseObj_courseNo">所属心理课程：</label>
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
				<label for="addTime">发布时间:</label>
				<input type="text" id="addTime" name="addTime" class="form-control"  placeholder="请选择发布时间" value="<%=addTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="kejianEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;课件信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="kejianEditForm" id="kejianEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="kejian_kejianId_edit" class="col-md-3 text-right">课件id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="kejian_kejianId_edit" name="kejian.kejianId" class="form-control" placeholder="请输入课件id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="kejian_title_edit" class="col-md-3 text-right">课件标题:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="kejian_title_edit" name="kejian.title" class="form-control" placeholder="请输入课件标题">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="kejian_courseObj_courseNo_edit" class="col-md-3 text-right">所属心理课程:</label>
		  	 <div class="col-md-9">
			    <select id="kejian_courseObj_courseNo_edit" name="kejian.courseObj.courseNo" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="kejian_kejianDesc_edit" class="col-md-3 text-right">课件描述:</label>
		  	 <div class="col-md-9">
			    <textarea id="kejian_kejianDesc_edit" name="kejian.kejianDesc" rows="8" class="form-control" placeholder="请输入课件描述"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="kejian_kejianFile_edit" class="col-md-3 text-right">课件文件:</label>
		  	 <div class="col-md-9">
			    <a id="kejian_kejianFileA" target="_blank"></a><br/>
			    <input type="hidden" id="kejian_kejianFile" name="kejian.kejianFile"/>
			    <input id="kejianFileFile" name="kejianFileFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="kejian_addTime_edit" class="col-md-3 text-right">发布时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date kejian_addTime_edit col-md-12" data-link-field="kejian_addTime_edit">
                    <input class="form-control" id="kejian_addTime_edit" name="kejian.addTime" size="16" type="text" value="" placeholder="请选择发布时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		</form> 
	    <style>#kejianEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxKejianModify();">提交</button>
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
    document.kejianQueryForm.currentPage.value = currentPage;
    document.kejianQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.kejianQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.kejianQueryForm.currentPage.value = pageValue;
    documentkejianQueryForm.submit();
}

/*弹出修改课件界面并初始化数据*/
function kejianEdit(kejianId) {
	$.ajax({
		url :  basePath + "Kejian/" + kejianId + "/update",
		type : "get",
		dataType: "json",
		success : function (kejian, response, status) {
			if (kejian) {
				$("#kejian_kejianId_edit").val(kejian.kejianId);
				$("#kejian_title_edit").val(kejian.title);
				$.ajax({
					url: basePath + "Course/listAll",
					type: "get",
					success: function(courses,response,status) { 
						$("#kejian_courseObj_courseNo_edit").empty();
						var html="";
		        		$(courses).each(function(i,course){
		        			html += "<option value='" + course.courseNo + "'>" + course.courseName + "</option>";
		        		});
		        		$("#kejian_courseObj_courseNo_edit").html(html);
		        		$("#kejian_courseObj_courseNo_edit").val(kejian.courseObjPri);
					}
				});
				$("#kejian_kejianDesc_edit").val(kejian.kejianDesc);
				$("#kejian_kejianFile").val(kejian.kejianFile);
				$("#kejian_kejianFileA").text(kejian.kejianFile);
				$("#kejian_kejianFileA").attr("href", basePath +　kejian.kejianFile);
				$("#kejian_addTime_edit").val(kejian.addTime);
				$('#kejianEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除课件信息*/
function kejianDelete(kejianId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Kejian/deletes",
			data : {
				kejianIds : kejianId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#kejianQueryForm").submit();
					//location.href= basePath + "Kejian/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交课件信息表单给服务器端修改*/
function ajaxKejianModify() {
	$.ajax({
		url :  basePath + "Kejian/" + $("#kejian_kejianId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#kejianEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#kejianQueryForm").submit();
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

    /*发布时间组件*/
    $('.kejian_addTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
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

