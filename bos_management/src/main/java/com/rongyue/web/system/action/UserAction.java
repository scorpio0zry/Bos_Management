package com.rongyue.web.system.action;


import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.system.User;
import com.rongyue.service.system.UserService;
import com.rongyue.web.base.action.BaseAction;

public class UserAction extends BaseAction<User>{
	@Autowired
	private UserService userService;
	//登陆 权限校验
	@Action(value="user_login",results={@Result(name="success",location="index.html",type="redirect"),
			@Result(name="login",location="login.html",type="redirect")})
	public String login(){
		//基于shiro实现登陆
		Subject subject = SecurityUtils.getSubject();
		//用户名密码信息
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(),model.getPassword());
		try{
			subject.login(token);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			return LOGIN;
		}
		
	}
	
	//用户 注销
	@Action(value="user_logout",results={@Result(name="success",location="login.html",type="redirect")})
	public String logout(){
		Subject subject = SecurityUtils.getSubject();
		
		subject.logout();
		return SUCCESS;
	}
	
	//用户查询
	@Action(value="user_list",results={@Result(name="success",type="json")})
	public String list(){
		List<User> list = userService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	//用户存储
	private String[] roleIds;
	
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	@Action(value="user_save",results={@Result(name="success",type="redirect",location="pages/system/userlist.html"),
			@Result(name="none",type="redirect",location="pages/system/userinfo.html")})
	public String save(){
		if(roleIds == null){
			return NONE;
		}
		userService.save(model,roleIds);
		return SUCCESS;
	}
}
