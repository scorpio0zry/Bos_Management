package com.rongyue.service.system;

import java.util.List;

import com.rongyue.domain.system.User;

public interface UserService {

	User findByUsername(String username);

	List<User> findAll();

	void save(User user, String[] roleIds);

}
