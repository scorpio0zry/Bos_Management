package com.rongyue.serviceimpl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.UserRepository;
import com.rongyue.domain.system.Role;
import com.rongyue.domain.system.User;
import com.rongyue.service.system.RoleSerivce;
import com.rongyue.service.system.UserService;
@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleSerivce roleSerivce;

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	//显示用户列表
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	//用户保存
	@Override
	public void save(User user, String[] roleIds) {
		for (String roleId : roleIds) {
			Role role = roleSerivce.findOne(Integer.parseInt(roleId));
			user.getRoles().add(role);
		}
		userRepository.save(user);
		
	}

}
