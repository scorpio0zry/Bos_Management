package com.rongyue.serviceimpl.transit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.InOutStorageInfoRepository;
import com.rongyue.dao.base.TransitInfoRepository;
import com.rongyue.domain.transit.InOutStorageInfo;
import com.rongyue.domain.transit.TransitInfo;
import com.rongyue.service.transit.InOutStorageInfoService;
@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {
	@Autowired
	private InOutStorageInfoRepository inOutStorageInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	//出入库信息保存
	@Override
	public void save(String transitInfoId, InOutStorageInfo inOutStorageInfo) {
		inOutStorageInfoRepository.save(inOutStorageInfo);
		
		TransitInfo transitIfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		if(transitIfo != null){
			transitIfo.getInOutStorageInfos().add(inOutStorageInfo);
			
			if("到达网点".equals(inOutStorageInfo.getOperation())){
				transitIfo.setStatus("到达网点");
				transitIfo.setOutletAddress(inOutStorageInfo.getAddress());
			}
		}
		
		
		
		
	}

}
