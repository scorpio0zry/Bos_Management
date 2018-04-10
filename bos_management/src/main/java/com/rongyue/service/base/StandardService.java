package com.rongyue.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rongyue.domain.base.Standard;

public interface StandardService {
	public void save(Standard standard);

	public Page<Standard> pageQuery(Pageable pageable);

	public List<Standard> findAll();

	public void updateDelTag(String[] idArr, Character deltag);
}
