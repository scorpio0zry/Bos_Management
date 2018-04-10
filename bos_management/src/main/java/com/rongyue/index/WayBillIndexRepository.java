package com.rongyue.index;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.rongyue.domain.take_delivery.WayBill;

public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer>{

}
