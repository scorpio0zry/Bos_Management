package com.rongyue.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.rongyue.domain.base.FixedArea;

public interface FixedAreaService {

	void save(FixedArea model);

	Page<FixedArea> findAll(Pageable pageable, Specification<FixedArea> specification);

	List<FixedArea> findAll();

}
