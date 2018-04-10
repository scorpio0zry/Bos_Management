package com.rongyue.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.rongyue.domain.take_delivery.Order;

public interface OrderService {
	//订单存储
	@Path("/saveOrder")
	@POST
	@Consumes({"application/xml","application/json"})
	public void saveOrder(Order order);

	public Order findByOrderNum(String orderNum);
}
