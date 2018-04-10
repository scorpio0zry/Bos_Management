package com.rongyue.serviceimpl.transit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.DeliveryInfoRepository;
import com.rongyue.dao.base.TransitInfoRepository;
import com.rongyue.domain.transit.DeliveryInfo;
import com.rongyue.domain.transit.TransitInfo;
import com.rongyue.service.transit.DeliveryInfoService;
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService{
	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	//添加了运单管理信息
	@Override
	public void save(String transitInfoId, DeliveryInfo deliveryInfo) {
		deliveryInfoRepository.save(deliveryInfo);
		
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		if(transitInfo != null){
			transitInfo.setDeliveryInfo(deliveryInfo);
			
			transitInfo.setStatus("开始配送");
		}
		
		
	}
	
}
