package com.rongyue.serviceimpl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.FixedAreaRepository;
import com.rongyue.domain.base.FixedArea;
import com.rongyue.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	//存储定区数据
	@Override
	public void save(FixedArea fixedarea) {
		fixedAreaRepository.save(fixedarea);
	}
	
	//条件查询所有定区数据
	@Override
	public Page<FixedArea> findAll(Pageable pageable, Specification<FixedArea> specification) {
		return fixedAreaRepository.findAll(specification, pageable);
	}
	
	//分区subarea添加定区中：查找所有定区
	@Override
	public List<FixedArea> findAll() {
		return fixedAreaRepository.findAll();
	}

}
