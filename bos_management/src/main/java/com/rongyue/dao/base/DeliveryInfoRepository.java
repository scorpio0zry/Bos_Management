package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rongyue.domain.transit.DeliveryInfo;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {

}
