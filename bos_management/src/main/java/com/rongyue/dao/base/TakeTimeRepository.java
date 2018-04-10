package com.rongyue.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rongyue.domain.base.TakeTime;

public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer> {

}
