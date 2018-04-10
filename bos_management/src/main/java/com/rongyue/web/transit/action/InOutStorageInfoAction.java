package com.rongyue.web.transit.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongyue.domain.transit.InOutStorageInfo;
import com.rongyue.service.transit.InOutStorageInfoService;
import com.rongyue.web.base.action.BaseAction;

public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo>{
	@Autowired
	private InOutStorageInfoService  inOutStorageInfoService;

	//出入库信息保存
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}

	@Action(value="inoutstore_save",results={@Result(name="success",location="pages/transit/transitinfo.html",type="redirect")})
	public String save(){
		if(transitInfoId != null){
			inOutStorageInfoService.save(transitInfoId,model);
		}
		
		return SUCCESS;
	}
}
