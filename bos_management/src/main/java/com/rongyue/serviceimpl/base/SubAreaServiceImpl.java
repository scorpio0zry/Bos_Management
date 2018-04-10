package com.rongyue.serviceimpl.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.SubAreaRepository;
import com.rongyue.domain.base.SubArea;
import com.rongyue.service.base.SubAreaService;
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {
	@Autowired
	private SubAreaRepository areaRepository;
	
	//添加分区信息
	@Override
	public void save(SubArea subArea) {
		areaRepository.save(subArea);
	}
	
	//条件查询区域信息
	@Override
	public Page<SubArea> pageQuery(Pageable pageable, Specification<SubArea> specification) {
		return areaRepository.findAll(specification, pageable);
	}
	

}
