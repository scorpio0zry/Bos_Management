package com.rongyue.web.transit.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongyue.domain.transit.SignInfo;
import com.rongyue.service.transit.SignInfoService;
import com.rongyue.web.base.action.BaseAction;

public class SignInfoAction extends BaseAction<SignInfo>{
	@Autowired
	private SignInfoService signInfoService;
	//签收添加
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}

	@Action(value="sign_save",results={@Result(name="success",location="pages/transit/transitinfo.html",type="redirect")})
	public String save(){
		if(transitInfoId != null){
			signInfoService.save(transitInfoId,model);
		}
		return SUCCESS;
	}
}
