package com.rongyue.serviceimpl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.RoleRepository;
import com.rongyue.domain.system.Menu;
import com.rongyue.domain.system.Permission;
import com.rongyue.domain.system.Role;
import com.rongyue.domain.system.User;
import com.rongyue.service.system.MenuService;
import com.rongyue.service.system.PermissionService;
import com.rongyue.service.system.RoleSerivce;
@Service
@Transactional
public class RoleServiceImpl implements RoleSerivce {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private MenuService menuService;

	@Override
	public List<Role> findByUser(User user) {
		if("admin".equals(user.getUsername())){
			//admin给予所有权限
			return roleRepository.findAll();
		}
		return roleRepository.findByUser(user.getId());
	}
	
	//显示角色列表
	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}
	
	//保存角色
	@Override
	public void saveRole(Role role, String[] permissionIds, String menuIds) {
		//保存角色信息
		
		//关联权限
		for (String permissionId : permissionIds) {
			Permission permission = permissionService.findOne(Integer.parseInt(permissionId));
			role.getPermissions().add(permission);
		}
		//关联菜单
		for (String menuId : menuIds.split(",")) {
			Menu menu = menuService.findOne(Integer.parseInt(menuId));
			role.getMenus().add(menu);
		}
		//刷新
		//roleRepository.flush();
		roleRepository.save(role);
	}

	@Override
	public Role findOne(int id) {
		return roleRepository.findOne(id);
	}

}
