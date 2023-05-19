$(function () {
	$.ajax({
		url : "Course/" + $("#course_courseNo_edit").val() + "/update",
		type : "get",
		data : {
			//courseNo : $("#course_courseNo_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (course, response, status) {
			$.messager.progress("close");
			if (course) { 
				$("#course_courseNo_edit").val(course.courseNo);
				$("#course_courseNo_edit").validatebox({
					required : true,
					missingMessage : "请输入心理课程编号",
					editable: false
				});
				$("#course_courseName_edit").val(course.courseName);
				$("#course_courseName_edit").validatebox({
					required : true,
					missingMessage : "请输入心理课程名称",
				});
				$("#course_coursePhoto").val(course.coursePhoto);
				$("#course_coursePhotoImg").attr("src", course.coursePhoto);
				$("#course_teacherObj_teacherNo_edit").combobox({
					url:"Teacher/listAll",
					valueField:"teacherNo",
					textField:"teacherName",
					panelHeight: "auto",
					editable: false, //不允许手动输入 
					onLoadSuccess: function () { //数据加载完毕事件
						$("#course_teacherObj_teacherNo_edit").combobox("select", course.teacherObjPri);
						//var data = $("#course_teacherObj_teacherNo_edit").combobox("getData"); 
						//if (data.length > 0) {
							//$("#course_teacherObj_teacherNo_edit").combobox("select", data[0].teacherNo);
						//}
					}
				});
				$("#course_courseHours_edit").val(course.courseHours);
				$("#course_courseHours_edit").validatebox({
					required : true,
					validType : "integer",
					missingMessage : "请输入心理课程学时",
					invalidMessage : "心理课程学时输入不对",
				});
				$("#course_jxff_edit").val(course.jxff);
				$("#course_jxff_edit").validatebox({
					required : true,
					missingMessage : "请输入教学大纲",
				});
				$("#course_kcjj_edit").val(course.kcjj);
				$("#course_kcjj_edit").validatebox({
					required : true,
					missingMessage : "请输入心理课程简介",
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#courseModifyButton").click(function(){ 
		if ($("#courseEditForm").form("validate")) {
			$("#courseEditForm").form({
			    url:"Course/" +  $("#course_courseNo_edit").val() + "/update",
			    onSubmit: function(){
					if($("#courseEditForm").form("validate"))  {
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
			$("#courseEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});
