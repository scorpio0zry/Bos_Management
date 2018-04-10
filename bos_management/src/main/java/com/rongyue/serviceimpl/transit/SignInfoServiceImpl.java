package com.rongyue.serviceimpl.transit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.SignInfoRepository;
import com.rongyue.dao.base.TransitInfoRepository;
import com.rongyue.domain.transit.SignInfo;
import com.rongyue.domain.transit.TransitInfo;
import com.rongyue.index.WayBillIndexRepository;
import com.rongyue.service.transit.SignInfoService;
@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {
	@Autowired
	private SignInfoRepository signInfoRepository;
	@Autowired
	private TransitInfoRepository transitInfoRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	//签收信息添加
	@Override
	public void save(String transitInfoId, SignInfo signInfo) {
		signInfoRepository.save(signInfo);
		
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		if(transitInfo != null){
			transitInfo.setSignInfo(signInfo);
			//更改签收状态
			if("正常".equals(signInfo.getSignType())){
				//正常签收
				transitInfo.setStatus("正常签收");
				transitInfo.getWayBill().setSignStatus(3);
				//更新索引库
				wayBillIndexRepository.save(transitInfo.getWayBill());
			}else{
				transitInfo.setStatus("异常");
				transitInfo.getWayBill().setSignStatus(4);
				//更新索引库
				wayBillIndexRepository.save(transitInfo.getWayBill());
			}
		}
		
	}

}
