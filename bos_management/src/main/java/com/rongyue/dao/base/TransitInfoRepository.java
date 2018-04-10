package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rongyue.domain.transit.TransitInfo;

public interface TransitInfoRepository extends JpaRepository<TransitInfo, Integer>{

}
