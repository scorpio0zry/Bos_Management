package com.rongyue.web.take_delivery.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.take_delivery.Order;
import com.rongyue.service.take_delivery.OrderService;
import com.rongyue.web.base.action.BaseAction;
//订单处理
public class OrderAction extends BaseAction<Order>{
	@Autowired
	private OrderService orderService;
	//waybill 异步通过订单编号查询订单信息
	@Action(value="order_findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum(){
		Order order = orderService.findByOrderNum(model.getOrderNum());
		Map<String, Object> map = new HashMap<String,Object>();
		if(order != null){
			map.put("success", true);
			map.put("orderData", order);
		}else{
			map.put("success", false);
		}
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
}
