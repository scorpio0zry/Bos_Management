package com.rongyue.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongyue.service.base.PromotionService;

public class PromotionJob implements Job {
	@Autowired
	private PromotionService promotionService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("结束时间已过期活动");
		//更改已过期时间的状态
		promotionService.updateStatus(new Date());
		
	}
	
}
