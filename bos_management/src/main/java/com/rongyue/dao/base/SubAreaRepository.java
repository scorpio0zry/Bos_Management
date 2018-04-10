package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rongyue.domain.base.SubArea;

public interface SubAreaRepository extends JpaRepository<SubArea, String>
	,JpaSpecificationExecutor<SubArea>{

}
