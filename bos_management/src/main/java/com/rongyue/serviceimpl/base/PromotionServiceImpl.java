package com.rongyue.serviceimpl.base;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.PromotionRepository;
import com.rongyue.domain.PageBean;
import com.rongyue.domain.Promotion;
import com.rongyue.service.base.PromotionService;
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
	@Autowired
	private PromotionRepository promotionRepository;
	
	/**
	 * 宣传任务的存储
	 */
	@Override
	public void save(Promotion promotion) {
		promotionRepository.save(promotion);
	}
	
	/**
	 * 宣传任务的页面查询 promotion.html
	 */
	@Override
	public Page<Promotion> findAll(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}
	
	/**
	 * 前台分页查询活动任务
	 */
	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageFind = promotionRepository.findAll(pageable);
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalCount((int) pageFind.getTotalElements());
		pageBean.setPageData(pageFind.getContent());
		return pageBean;
	}
	
	/**
	 * 前台查询宣传活动数据
	 */
	@Override
	public Promotion findById(Integer id) {
		return promotionRepository.findOne(id);
	}
	
	/**
	 * 更改已过期活动的状态
	 */
	@Override
	public void updateStatus(Date date) {
		promotionRepository.updateStatus(date);
	}

}
