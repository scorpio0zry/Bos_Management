package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rongyue.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Order findByOrderNum(String orderNum);

}
