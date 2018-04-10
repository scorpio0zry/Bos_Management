package com.rongyue.service.system;

import java.util.List;

import com.rongyue.domain.system.Menu;
import com.rongyue.domain.system.User;

public interface MenuService {

	List<Menu> findAll();

	void save(Menu menu);

	Menu findOne(int id);

	List<Menu> findByUser(User user);

}
