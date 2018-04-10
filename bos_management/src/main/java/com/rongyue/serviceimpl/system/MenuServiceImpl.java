package com.rongyue.serviceimpl.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.MenuRepository;
import com.rongyue.domain.system.Menu;
import com.rongyue.domain.system.User;
import com.rongyue.service.system.MenuService;
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuRepository menuRepository;
	
	//显示 菜单列表
	@Override
	public List<Menu> findAll() {
		return menuRepository.findAll();
	}
	
	//添加菜单
	@Override
	public void save(Menu menu) {
		//如果父菜单项没有选择则置为空
		if(menu.getParentMenu()!= null && menu.getParentMenu().getId()==0){
			menu.setParentMenu(null);
		}
		
		menuRepository.save(menu);
	}

	@Override
	public Menu findOne(int id) {
		return menuRepository.findOne(id);
	}
	
	//不同用户显示不同菜单
	@Override
	public List<Menu> findByUser(User user) {
		if("admin".equals(user.getUsername())){
			return menuRepository.findAll();
		}
		
		return menuRepository.findByUser(user.getId());
	}
	
	
	

}
