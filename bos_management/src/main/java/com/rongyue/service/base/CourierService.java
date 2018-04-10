package com.rongyue.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.rongyue.domain.base.Courier;

public interface CourierService {

	Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

	void save(Courier courier);

	void updateDelTag(String[] idArr, Character flag);

	List<Courier> findAll();

	void updateCourier(String courierId, String fixedAreaId, String takeTimeId);
}
