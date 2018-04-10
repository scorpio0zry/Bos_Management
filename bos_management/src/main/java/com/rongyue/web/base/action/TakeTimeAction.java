package com.rongyue.web.base.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.base.TakeTime;
import com.rongyue.service.base.TakeTimeService;

public class TakeTimeAction extends BaseAction<TakeTime>{
	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action(value="taketime_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<TakeTime> list = takeTimeService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}

}
