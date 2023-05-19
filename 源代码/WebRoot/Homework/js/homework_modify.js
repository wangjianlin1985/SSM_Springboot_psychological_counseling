$(function () {
	$.ajax({
		url : "Homework/" + $("#homework_homeworkId_edit").val() + "/update",
		type : "get",
		data : {
			//homeworkId : $("#homework_homeworkId_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (homework, response, status) {
			$.messager.progress("close");
			if (homework) { 
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
				$("#homework_taskFileA").attr("href", homework.taskFile);
				$("#homework_homeworkDate_edit").datebox({
					value: homework.homeworkDate,
					required: true,
					showSeconds: true,
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#homeworkModifyButton").click(function(){ 
		if ($("#homeworkEditForm").form("validate")) {
			$("#homeworkEditForm").form({
			    url:"Homework/" +  $("#homework_homeworkId_edit").val() + "/update",
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
                	var obj = jQuery.parseJSON(data);
                    if(obj.success){
                        $.messager.alert("消息","信息修改成功！");
                        $(".messager-window").css("z-index",10000);
                        //location.href="frontlist";
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    } 
			    }
			});
			//提交表单
			$("#homeworkEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});
