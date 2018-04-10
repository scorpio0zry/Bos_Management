package com.rongyue.serviceimpl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.AreaRepository;
import com.rongyue.domain.base.Area;
import com.rongyue.service.base.AreaService;
@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaRepository areaRepsoitory;
	
	//Excel表格存储区域信息
	@Override
	public void save(List<Area> list) {
		areaRepsoitory.save(list);
	}
	
	//根据查询条件，分页读取数据库中区域信息，如果没有查询条件则读取全部信息
	@Override
	public Page<Area> findAll(Specification<Area> specification, Pageable pageable) {
		return areaRepsoitory.findAll(specification, pageable);
	}
	
	//存取单条数据信息
	@Override
	public void saveSingle(Area area) {
		areaRepsoitory.save(area);
	}
	
	//删除区域
	@Override
	public void del(String[] idArr) {
		for (String id : idArr) {
			areaRepsoitory.del(id);
		}
	}
	
	//分区subarea添加区域中：查找所有区域
	@Override
	public List<Area> findAll() {
		return areaRepsoitory.findAll();
	}

}
