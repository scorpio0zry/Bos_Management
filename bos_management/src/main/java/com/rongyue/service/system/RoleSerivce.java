package com.rongyue.service.system;

import java.util.List;

import com.rongyue.domain.system.Role;
import com.rongyue.domain.system.User;

public interface RoleSerivce {

	List<Role> findByUser(User user);

	List<Role> findAll();

	void saveRole(Role role, String[] permissionIds, String menuIds);

	Role findOne(int id);

}
