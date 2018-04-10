package com.rongyue.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rongyue.domain.take_delivery.WayBill;

public interface WayBillService {

	void save(WayBill model);

	Page<WayBill> findAll(WayBill wayBill, Pageable pageable);

	WayBill findByWayBillNum(String wayBillNum);
	
	void syncIndex();

	List<WayBill> findWayBills(WayBill model);

}
