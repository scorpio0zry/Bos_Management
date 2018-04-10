package com.rongyue.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.rongyue.domain.base.SubArea;

public interface SubAreaService {

	void save(SubArea subArea);

	Page<SubArea> pageQuery(Pageable pageable, Specification<SubArea> specification);

}
