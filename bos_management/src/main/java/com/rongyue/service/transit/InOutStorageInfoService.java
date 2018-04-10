package com.rongyue.service.transit;

import com.rongyue.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoService {

	void save(String transitInfoId, InOutStorageInfo inOutStorageInfo);

}
