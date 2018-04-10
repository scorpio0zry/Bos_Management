var test1 = false;
var test2 = false;
var test3 = false;
//异步校验手机好是否被注册
function checkPhone() {
	var mobilePhone = $("#inputaccount").val();
	$("#testP").html("");
	var regex = /^1(3|5|7|8)\d{9}$/;
	if(regex.test(mobilePhone)) {
		$.post("signup_checkPhone.action", {
			"mobilePhone": mobilePhone
		}, function(data) {
			if(data == 'true') {
				//注册手机号不存在,可以注册
				$("#testP").append("<span class='label label-success'>手机号可以进行注册</span>");
				test1 = true;
			} else{
				//注册手机号已存在
				$("#testP").append("<span class='label label-danger'>该手机号已经被注册</span>");
				test1 = false;
			}
		})
	}else{
		$("#testP").append("<span class='label label-warning'>这不是一个手机号码哦\(^o^)/</span>");
		test1 = false;
	}
	
}

//检验两次密码是否一致
function checkPassword(){
	$("#testP").html("");
	var password = $("#password").val();
	var confirmPassword = $("#confirmPassword").val();
	if(password != "" && confirmPassword == password){
		$("#testP").append("<span class='label label-success'>密码输入一致</span>");
		test2 = true;
	}else{
		$("#testP").append("<span class='label label-danger'>密码输入不一致</span>");
		test2 = false;
	}
}

//判断邮箱格式是否正确
function checkEmail(){
	$("#testP").html("");
	var email = $("#inputemail").val();
	var regex = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	if(regex.test(email)){
		$.post("signup_checkEmail.action",{"email":email},function(data){
			if(data == 'true'){
				//邮箱未被注册
				$("#testP").append("<span class='label label-success'>邮箱可以使用</span>");
				test3 = true;
			}else{
				//邮箱已被注册
				$("#testP").append("<span class='label label-danger'>邮箱已经被注册</span>");
				test3 = false;
			}
		})
	}else{
		//输入的邮箱格式不正确
		$("#testP").append("<span class='label label-danger'>邮箱格式不正确</span>");
		test3 = false;
	}
	
}

function register(){
	$("#testP").html("");
	if(test1&&test2&&test3){
		if($("#agree").prop("checked")) {
			$("#registerForm").submit();
		} else {
			alert("同意协议之后方可提交！")
		}
	}else{
		alert("请将信息填写完整后再提交！")
	}
	
}
