var homework_manage_tool = null; 
$(function () { 
	initHomeworkManageTool(); //建立Homework管理对象
	homework_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#homework_manage").datagrid({
		url : 'Homework/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "homeworkId",
		sortOrder : "desc",
		toolbar : "#homework_manage_tool",
		columns : [[
			{
				field : "homeworkId",
				title : "作业id",
				width : 70,
			},
			{
				field : "courseObj",
				title : "作业心理课程",
				width : 140,
			},
			{
				field : "taskTitle",
				title : "作业任务",
				width : 140,
			},
			{
				field : "taskFile",
				title : "作业文件",
				width : "350px",
				formatter: function(val,row) {
 					if(val == "") return "暂无文件";
					return "<a href='" + val + "' target='_blank' style='color:red;'>" + val + "</a>";
				}
 			},
			{
				field : "homeworkDate",
				title : "作业日期",
				width : 140,
			},
		]],
	});

	$("#homeworkEditDiv").dialog({
		title : "修改管理",
		top: "50px",
		width : 700,
		height : 515,
		modal : true,
		closed : true,
		iconCls : "icon-edit-new",
		buttons : [{
			text : "提交",
			iconCls : "icon-edit-new",
			handler : function () {
				if ($("#homeworkEditForm").form("validate")) {
					//验证表单 
					if(!$("#homeworkEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#homeworkEditForm").form({
						    url:"Homework/" + $("#homework_homeworkId_edit").val() + "/update",
						    onSubmit: function(){
								if($("#homeworkEditForm").form("validate"))  {
				                	$.messager.progress({
										text : "正在提交数据中...",
									});
				                	return true;
				                } else { 
				                    return false; 
				                }
						    },
						    success:function(data){
						    	$.messager.progress("close");
						    	console.log(data);
			                	var obj = jQuery.parseJSON(data);
			                    if(obj.success){
			                        $.messager.alert("消息","信息修改成功！");
			                        $("#homeworkEditDiv").dialog("close");
			                        homework_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#homeworkEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#homeworkEditDiv").dialog("close");
				$("#homeworkEditForm").form("reset"); 
			},
		}],
	});
});

function initHomeworkManageTool() {
	homework_manage_tool = {
		init: function() {
			$.ajax({
				url : "Course/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#courseObj_courseNo_query").combobox({ 
					    valueField:"courseNo",
					    textField:"courseName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{courseNo:"",courseName:"不限制"});
					$("#courseObj_courseNo_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#homework_manage").datagrid("reload");
		},
		redo : function () {
			$("#homework_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#homework_manage").datagrid("options").queryParams;
			queryParams["courseObj.courseNo"] = $("#courseObj_courseNo_query").combobox("getValue");
			queryParams["taskTitle"] = $("#taskTitle").val();
			queryParams["homeworkDate"] = $("#homeworkDate").datebox("getValue"); 
			$("#homework_manage").datagrid("options").queryParams=queryParams; 
			$("#homework_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#homeworkQueryForm").form({
			    url:"Homework/OutToExcel",
			});
			//提交表单
			$("#homeworkQueryForm").submit();
		},
		remove : function () {
			var rows = $("#homework_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var homeworkIds = [];
						for (var i = 0; i < rows.length; i ++) {
							homeworkIds.push(rows[i].homeworkId);
						}
						$.ajax({
							type : "POST",
							url : "Homework/deletes",
							data : {
								homeworkIds : homeworkIds.join(","),
							},
							beforeSend : function () {
								$("#homework_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#homework_manage").datagrid("loaded");
									$("#homework_manage").datagrid("load");
									$("#homework_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#homework_manage").datagrid("loaded");
									$("#homework_manage").datagrid("load");
									$("#homework_manage").datagrid("unselectAll");
									$.messager.alert("消息",data.message);
								}
							},
						});
					}
				});
			} else {
				$.messager.alert("提示", "请选择要删除的记录！", "info");
			}
		},
		edit : function () {
			var rows = $("#homework_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Homework/" + rows[0].homeworkId +  "/update",
					type : "get",
					data : {
						//homeworkId : rows[0].homeworkId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (homework, response, status) {
						$.messager.progress("close");
						if (homework) { 
							$("#homeworkEditDiv").dialog("open");
							$("#homework_homeworkId_edit").val(homework.homeworkId);
							$("#homework_homeworkId_edit").validatebox({
								required : true,
								missingMessage : "请输入作业id",
								editable: false
							});
							$("#homework_courseObj_courseNo_edit").combobox({
								url:"Course/listAll",
							    valueField:"courseNo",
							    textField:"courseName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#homework_courseObj_courseNo_edit").combobox("select", homework.courseObjPri);
									//var data = $("#homework_courseObj_courseNo_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#homework_courseObj_courseNo_edit").combobox("select", data[0].courseNo);
						            //}
								}
							});
							$("#homework_taskTitle_edit").val(homework.taskTitle);
							$("#homework_taskTitle_edit").validatebox({
								required : true,
								missingMessage : "请输入作业任务",
							});
							$("#homework_taskContent_edit").val(homework.taskContent);
							$("#homework_taskContent_edit").validatebox({
								required : true,
								missingMessage : "请输入作业要求",
							});
							$("#homework_taskFile").val(homework.taskFile);
							if(homework.taskFile == "") $("#homework_taskFileA").html("暂无文件");
							else $("#homework_taskFileA").html(homework.taskFile);
							$("#homework_taskFileA").attr("href", homework.taskFile);
							$("#homework_homeworkDate_edit").datebox({
								value: homework.homeworkDate,
							    required: true,
							    showSeconds: true,
							});
						} else {
							$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}
