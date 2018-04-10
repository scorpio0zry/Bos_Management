package com.rongyue.web.system.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.system.Role;
import com.rongyue.service.system.RoleSerivce;
import com.rongyue.web.base.action.BaseAction;

public class RoleAction extends BaseAction<Role>{
	@Autowired
	private RoleSerivce roleSerivce;
	
	//角色列表显示
	@Action(value="role_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Role> list = roleSerivce.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	//角色存储
	private String[] permissionIds;
	private String menuIds;

	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Action(value="role_save",results={@Result(name="success",location="pages/system/role.html",type="redirect"),
			@Result(name="none",location="pages/system/role_add.html",type="redirect")})
	public String save(){
		if(permissionIds == null || menuIds == null){
			return NONE;
		}
		roleSerivce.saveRole(model,permissionIds,menuIds);
		return SUCCESS;
	}
}
