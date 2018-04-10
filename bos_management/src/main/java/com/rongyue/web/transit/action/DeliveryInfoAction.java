package com.rongyue.web.transit.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongyue.domain.transit.DeliveryInfo;
import com.rongyue.service.transit.DeliveryInfoService;
import com.rongyue.web.base.action.BaseAction;

public class DeliveryInfoAction extends BaseAction<DeliveryInfo>{
	@Autowired
	private DeliveryInfoService deliveryInfoService;
	//配送信息添加
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}

	@Action(value="delivery_save",results={@Result(name="success",location="pages/transit/transitinfo.html",type="redirect")})
	public String save(){
		if(transitInfoId != null){
			deliveryInfoService.save(transitInfoId,model);
		}
		return SUCCESS;
	}
}
