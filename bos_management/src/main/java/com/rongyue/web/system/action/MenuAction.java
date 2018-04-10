package com.rongyue.web.system.action;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.system.Menu;
import com.rongyue.domain.system.User;
import com.rongyue.service.system.MenuService;
import com.rongyue.web.base.action.BaseAction;

public class MenuAction extends BaseAction<Menu>{
	@Autowired
	private MenuService menuService;
	//显示 菜单列表
	@Action(value="menu_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Menu> list = menuService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	//添加菜单
	@Action(value="menu_save",results={@Result(name="success",location="/pages/system/menu.html",type="redirect")})
	public String save(){
		menuService.save(model);
		return SUCCESS;
	}
	
	//不同用户显示不同的菜单
	@Action(value="menu_show",results={@Result(name="success",type="json")})
	public String show(){
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		List<Menu> list = menuService.findByUser(user);
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
