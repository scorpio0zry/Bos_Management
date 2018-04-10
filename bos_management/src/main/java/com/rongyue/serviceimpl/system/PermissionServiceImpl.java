package com.rongyue.serviceimpl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.PermissionRepository;
import com.rongyue.domain.system.Permission;
import com.rongyue.domain.system.User;
import com.rongyue.service.system.PermissionService;
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<Permission> findByUser(User user) {
		if("admin".equals(user.getUsername())){
			//admin给予所有权限
			return permissionRepository.findAll();
		}
		return permissionRepository.findByUser(user.getId());
	}
	
	//权限列表查询
	@Override
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}
	
	//添加权限
	@Override
	public void save(Permission model) {
		permissionRepository.save(model);
	}

	@Override
	public Permission findOne(int id) {
		return permissionRepository.findOne(id);
	}

}
