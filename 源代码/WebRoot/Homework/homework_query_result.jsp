<%@ page language="java"  contentType="text/html;charset=UTF-8"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/homework.css" /> 

<div id="homework_manage"></div>
<div id="homework_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="homework_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="homework_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="homework_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="homework_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="homework_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="homeworkQueryForm" method="post">
			作业心理课程：<input class="textbox" type="text" id="courseObj_courseNo_query" name="courseObj.courseNo" style="width: auto"/>
			作业任务：<input type="text" class="textbox" id="taskTitle" name="taskTitle" style="width:110px" />
			作业日期：<input type="text" id="homeworkDate" name="homeworkDate" class="easyui-datebox" editable="false" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="homework_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="homeworkEditDiv">
	<form id="homeworkEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">作业id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="homework_homeworkId_edit" name="homework.homeworkId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">作业心理课程:</span>
			<span class="inputControl">
				<input class="textbox"  id="homework_courseObj_courseNo_edit" name="homework.courseObj.courseNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">作业任务:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="homework_taskTitle_edit" name="homework.taskTitle" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">作业要求:</span>
			<span class="inputControl">
				<textarea id="homework_taskContent_edit" name="homework.taskContent" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div>
			<span class="label">作业文件:</span>
			<span class="inputControl">
				<a id="homework_taskFileA" style="color:red;margin-bottom:5px;">查看</a>&nbsp;&nbsp;
    			<input type="hidden" id="homework_taskFile" name="homework.taskFile"/>
				<input id="taskFileFile" name="taskFileFile" value="重新选择文件" type="file" size="50" />
			</span>
		</div>
		<div>
			<span class="label">作业日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="homework_homeworkDate_edit" name="homework.homeworkDate" />

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Homework/js/homework_manage.js"></script> 
