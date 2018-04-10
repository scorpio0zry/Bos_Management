package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rongyue.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill, Integer> {

	WayBill findByWayBillNum(String wayBillNum);

}
