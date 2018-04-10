package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rongyue.domain.system.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);

}
