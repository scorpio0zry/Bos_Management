package com.rongyue.web.system.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.system.Permission;
import com.rongyue.service.system.PermissionService;
import com.rongyue.web.base.action.BaseAction;

public class PermissionAction extends BaseAction<Permission>{
	@Autowired
	private PermissionService permissionService;
	//权限查询
	@Action(value="permission_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Permission> list = permissionService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	//权限添加
	@Action(value="permission_save",results={@Result(name="success",location="/pages/system/permission.html",type="redirect")})
	public String save(){
		permissionService.save(model);
		return SUCCESS;
	}
}
