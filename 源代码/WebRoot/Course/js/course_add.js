$(function () {
	$("#course_courseNo").validatebox({
		required : true, 
		missingMessage : '请输入心理课程编号',
	});

	$("#course_courseName").validatebox({
		required : true, 
		missingMessage : '请输入心理课程名称',
	});

	$("#course_teacherObj_teacherNo").combobox({
	    url:'Teacher/listAll',
	    valueField: "teacherNo",
	    textField: "teacherName",
	    panelHeight: "auto",
        editable: false, //不允许手动输入
        required : true,
        onLoadSuccess: function () { //数据加载完毕事件
            var data = $("#course_teacherObj_teacherNo").combobox("getData"); 
            if (data.length > 0) {
                $("#course_teacherObj_teacherNo").combobox("select", data[0].teacherNo);
            }
        }
	});
	$("#course_courseHours").validatebox({
		required : true,
		validType : "integer",
		missingMessage : '请输入心理课程学时',
		invalidMessage : '心理课程学时输入不对',
	});

	$("#course_jxff").validatebox({
		required : true, 
		missingMessage : '请输入教学大纲',
	});

	$("#course_kcjj").validatebox({
		required : true, 
		missingMessage : '请输入心理课程简介',
	});

	//单击添加按钮
	$("#courseAddButton").click(function () {
		//验证表单 
		if(!$("#courseAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		} else {
			$("#courseAddForm").form({
			    url:"Course/add",
			    onSubmit: function(){
					if($("#courseAddForm").form("validate"))  { 
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
                    //此处data={"Success":true}是字符串
                	var obj = jQuery.parseJSON(data); 
                    if(obj.success){ 
                        $.messager.alert("消息","保存成功！");
                        $(".messager-window").css("z-index",10000);
                        $("#courseAddForm").form("clear");
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    }
			    }
			});
			//提交表单
			$("#courseAddForm").submit();
		}
	});

	//单击清空按钮
	$("#courseClearButton").click(function () { 
		$("#courseAddForm").form("clear"); 
	});
});
