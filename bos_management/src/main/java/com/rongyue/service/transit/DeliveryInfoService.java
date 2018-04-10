package com.rongyue.service.transit;

import com.rongyue.domain.transit.DeliveryInfo;

public interface DeliveryInfoService {

	void save(String transitInfoId, DeliveryInfo deliveryInfo);

}
