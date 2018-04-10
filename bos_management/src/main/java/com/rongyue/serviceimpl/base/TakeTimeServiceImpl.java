package com.rongyue.serviceimpl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.TakeTimeRepository;
import com.rongyue.domain.base.TakeTime;
import com.rongyue.service.base.TakeTimeService;
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {
	
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}
