package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rongyue.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoRepository extends JpaRepository<InOutStorageInfo, Integer> {

}
