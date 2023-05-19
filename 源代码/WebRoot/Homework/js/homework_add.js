$(function () {
	$("#homework_courseObj_courseNo").combobox({
	    url:'Course/listAll',
	    valueField: "courseNo",
	    textField: "courseName",
	    panelHeight: "auto",
        editable: false, //不允许手动输入
        required : true,
        onLoadSuccess: function () { //数据加载完毕事件
            var data = $("#homework_courseObj_courseNo").combobox("getData"); 
            if (data.length > 0) {
                $("#homework_courseObj_courseNo").combobox("select", data[0].courseNo);
            }
        }
	});
	$("#homework_taskTitle").validatebox({
		required : true, 
		missingMessage : '请输入作业任务',
	});

	$("#homework_taskContent").validatebox({
		required : true, 
		missingMessage : '请输入作业要求',
	});

	$("#homework_homeworkDate").datebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	//单击添加按钮
	$("#homeworkAddButton").click(function () {
		//验证表单 
		if(!$("#homeworkAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		} else {
			$("#homeworkAddForm").form({
			    url:"Homework/add",
			    onSubmit: function(){
					if($("#homeworkAddForm").form("validate"))  { 
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
                        $("#homeworkAddForm").form("clear");
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    }
			    }
			});
			//提交表单
			$("#homeworkAddForm").submit();
		}
	});

	//单击清空按钮
	$("#homeworkClearButton").click(function () { 
		$("#homeworkAddForm").form("clear"); 
	});
});
