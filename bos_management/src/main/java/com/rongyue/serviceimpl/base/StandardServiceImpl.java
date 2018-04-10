package com.rongyue.serviceimpl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.StandardRepository;
import com.rongyue.domain.base.Standard;
import com.rongyue.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	@Autowired
	private StandardRepository daoRepository;
	
	//存储数据
	@Override
	@CacheEvict(value="standard",allEntries=true)
	public void save(Standard standard) {
		daoRepository.save(standard);
	}

	//读取数据并分页
	@Override
	@Cacheable(value="standard",key="#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Standard> pageQuery(Pageable pageable) {
		return daoRepository.findAll(pageable);
	}
	
	
	@Override
	@Cacheable("standard")
	public List<Standard> findAll() {
		return daoRepository.findDelTag();
	}

	@Override
	public void updateDelTag(String[] idArr, Character deltag) {
		for (String id : idArr) {
			daoRepository.updateDelTag(Integer.parseInt(id),deltag);
		}
	}
	
}
