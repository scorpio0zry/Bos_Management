package com.rongyue.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rongyue.domain.system.Permission;
import com.rongyue.domain.system.User;

public interface PermissionRepository extends JpaRepository<Permission, Integer>{
	@Query("from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id = ? ")
	List<Permission> findByUser(Integer id);

}
