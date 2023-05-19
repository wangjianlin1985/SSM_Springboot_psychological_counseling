$(function () {
	$.ajax({
		url : "Student/" + $("#student_user_name_edit").val() + "/update",
		type : "get",
		data : {
			//user_name : $("#student_user_name_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (student, response, status) {
			$.messager.progress("close");
			if (student) { 
				$("#student_user_name_edit").val(student.user_name);
				$("#student_user_name_edit").validatebox({
					required : true,
					missingMessage : "请输入学号",
					editable: false
				});
				$("#student_password_edit").val(student.password);
				$("#student_password_edit").validatebox({
					required : true,
					missingMessage : "请输入登录密码",
				});
				$("#student_classObj_classNo_edit").combobox({
					url:"ClassInfo/listAll",
					valueField:"classNo",
					textField:"className",
					panelHeight: "auto",
					editable: false, //不允许手动输入 
					onLoadSuccess: function () { //数据加载完毕事件
						$("#student_classObj_classNo_edit").combobox("select", student.classObjPri);
						//var data = $("#student_classObj_classNo_edit").combobox("getData"); 
						//if (data.length > 0) {
							//$("#student_classObj_classNo_edit").combobox("select", data[0].classNo);
						//}
					}
				});
				$("#student_name_edit").val(student.name);
				$("#student_name_edit").validatebox({
					required : true,
					missingMessage : "请输入姓名",
				});
				$("#student_gender_edit").val(student.gender);
				$("#student_gender_edit").validatebox({
					required : true,
					missingMessage : "请输入性别",
				});
				$("#student_birthDate_edit").datebox({
					value: student.birthDate,
					required: true,
					showSeconds: true,
				});
				$("#student_userPhoto").val(student.userPhoto);
				$("#student_userPhotoImg").attr("src", student.userPhoto);
				$("#student_telephone_edit").val(student.telephone);
				$("#student_telephone_edit").validatebox({
					required : true,
					missingMessage : "请输入联系电话",
				});
				$("#student_email_edit").val(student.email);
				$("#student_email_edit").validatebox({
					required : true,
					missingMessage : "请输入邮箱",
				});
				$("#student_address_edit").val(student.address);
				$("#student_regTime_edit").datetimebox({
					value: student.regTime,
					required: true,
					showSeconds: true,
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#studentModifyButton").click(function(){ 
		if ($("#studentEditForm").form("validate")) {
			$("#studentEditForm").form({
			    url:"Student/" +  $("#student_user_name_edit").val() + "/update",
			    onSubmit: function(){
					if($("#studentEditForm").form("validate"))  {
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
			$("#studentEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});
