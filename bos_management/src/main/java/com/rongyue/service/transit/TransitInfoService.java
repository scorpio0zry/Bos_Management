package com.rongyue.service.transit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rongyue.domain.transit.TransitInfo;

public interface TransitInfoService{

	void createTransitInfo(String wayBillIds);

	Page<TransitInfo> findPage(Pageable pageable);
	
}
