package com.rongyue.service.base;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.rongyue.domain.base.Area;

public interface AreaService {

	void saveSingle(Area model);
	
	void save(List<Area> list);

	Page<Area> findAll(Specification<Area> specification, Pageable pageable);

	void del(String[] idArr);

	List<Area> findAll();

}
