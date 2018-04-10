package com.rongyue.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongyue.service.take_delivery.WayBillService;
//定时更新索引信息
public class WayBillIndexJob implements Job {
	@Autowired
	private WayBillService wayBillService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("更新订单索引信息");
		wayBillService.syncIndex();
	}

}
