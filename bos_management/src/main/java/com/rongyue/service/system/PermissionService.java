package com.rongyue.service.system;

import java.util.List;

import com.rongyue.domain.system.Permission;
import com.rongyue.domain.system.User;

public interface PermissionService {

	List<Permission> findByUser(User user);

	List<Permission> findAll();

	void save(Permission model);

	Permission findOne(int id);

}
