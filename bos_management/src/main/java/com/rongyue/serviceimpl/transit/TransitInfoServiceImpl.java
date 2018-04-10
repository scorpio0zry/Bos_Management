package com.rongyue.serviceimpl.transit;

import org.hibernate.boot.jaxb.hbm.spi.PluralAttributeInfoPrimitiveArrayAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.TransitInfoRepository;
import com.rongyue.dao.base.WayBillRepository;
import com.rongyue.domain.take_delivery.WayBill;
import com.rongyue.domain.transit.TransitInfo;
import com.rongyue.index.WayBillIndexRepository;
import com.rongyue.service.transit.TransitInfoService;
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {
	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	//开启中转
	@Override
	public void createTransitInfo(String wayBillIds) {
		for (String wayBillId : wayBillIds.split(",")) {
			WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
			if(wayBill != null){
				if(wayBill.getSignStatus() == 1){
					TransitInfo transitInfo = new TransitInfo();
					transitInfo.setWayBill(wayBill);
					transitInfo.setStatus("出入库中转");
					transitInfoRepository.save(transitInfo);
					
					//更改运单状态
					wayBill.setSignStatus(2);
					
					//更新索引库
					wayBillIndexRepository.save(wayBill);
				}
			}
		} 
	}
	
	//显示运输管理页面
	@Override
	public Page<TransitInfo> findPage(Pageable pageable) {
		return transitInfoRepository.findAll(pageable);
	}

}
