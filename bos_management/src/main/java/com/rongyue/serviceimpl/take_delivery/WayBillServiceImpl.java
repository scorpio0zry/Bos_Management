package com.rongyue.serviceimpl.take_delivery;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.dao.base.OrderRepository;
import com.rongyue.dao.base.WayBillRepository;
import com.rongyue.index.WayBillIndexRepository;
import com.rongyue.service.take_delivery.WayBillService;
import com.rongyue.domain.take_delivery.Order;
import com.rongyue.domain.take_delivery.WayBill;
import com.rongyue.utils.UUIDUtils;
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(WayBill wayBill) {
		if(wayBill.getWayBillNum() == null){
			//订单录入，运单号不存在
			wayBill.setWayBillNum(UUIDUtils.getUUID());
			wayBill.setSignStatus(1);
		}else{
			//运单录入
			if(wayBill.getSignStatus() == null || wayBill.getSignStatus() == 1){
				wayBill.setSignStatus(1);
				if(wayBill.getOrder() != null){
					Order order = orderRepository.findByOrderNum(wayBill.getOrder().getOrderNum());
					wayBill.setOrder(order);
				}	
			}else{
				throw new RuntimeException("运单不处于发货中");
			}
				
		}
		wayBillRepository.save(wayBill);
		wayBillIndexRepository.save(wayBill);
	}
	
	// 分页显示运单数据信息
	@Override
	public Page<WayBill> findAll(WayBill wayBill,Pageable pageable) {
		//如果没有查询任何值，直接查询数据库
		if(StringUtils.isBlank(wayBill.getWayBillNum())&&
				StringUtils.isBlank(wayBill.getSendAddress())&&
				StringUtils.isBlank(wayBill.getRecAddress()) &&
				StringUtils.isBlank(wayBill.getSendProNum()) &&
				(wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)){
			return wayBillRepository.findAll(pageable);
		}else{
			//查询条件
			//must 条件成立  must not 条件不成立   should 条件可以成立
			
			//布尔查询 多条件查询
			BoolQueryBuilder query = new BoolQueryBuilder();
			// 向组合查询对象添加条件
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号查询
				QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum",
						wayBill.getWayBillNum());
				query.must(tempQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				// 发货地 模糊查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"sendAddress", "*" + wayBill.getSendAddress() + "*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				// 收货地 模糊查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"recAddress", "*" + wayBill.getRecAddress() + "*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
				// 签收状态查询
				QueryBuilder termQuery = new TermQueryBuilder("signStatus",
						wayBill.getSignStatus());
				query.must(termQuery);
			}

			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable); // 分页效果
			// 有条件查询 、查询索引库
			return wayBillIndexRepository.search(searchQuery);
		}
		
	}
	
	//运单号异步从数据库加载信息
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
	
	//quartz定时同步数据库与索引库信息
	public void syncIndex(){
		List<WayBill> list = wayBillRepository.findAll();
		wayBillIndexRepository.save(list);
	}

	@Override
	public List<WayBill> findWayBills(WayBill wayBill) {
		// 判断WayBill 中条件是否存在
		if (StringUtils.isBlank(wayBill.getWayBillNum())
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress())
				&& StringUtils.isBlank(wayBill.getSendProNum())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
			// 无条件查询 、查询数据库
			return wayBillRepository.findAll();
		} else {
			// 查询条件
			// must 条件必须成立 and
			// must not 条件必须不成立 not
			// should 条件可以成立 or
			BoolQueryBuilder query = new BoolQueryBuilder(); // 布尔查询 ，多条件组合查询
			// 向组合查询对象添加条件
			if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
				// 运单号查询
				QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum",
						wayBill.getWayBillNum());
				query.must(tempQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
				// 发货地 模糊查询
				// 情况一： 输入"北" 是查询词条一部分， 使用模糊匹配词条查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"sendAddress", "*" + wayBill.getSendAddress() + "*");

				
			

				// 两种情况取or关系
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(wildcardQuery);

				query.must(boolQueryBuilder);
			}
			if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
				// 收货地 模糊查询
				QueryBuilder wildcardQuery = new WildcardQueryBuilder(
						"recAddress", "*" + wayBill.getRecAddress() + "*");
				query.must(wildcardQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
				// 速运类型 等值查询
				QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
						wayBill.getSendProNum());
				query.must(termQuery);
			}
			if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
				// 签收状态查询
				QueryBuilder termQuery = new TermQueryBuilder("signStatus",
						wayBill.getSignStatus());
				query.must(termQuery);
			}

			SearchQuery searchQuery = new NativeSearchQuery(query);
			// ElasticSearch 允许搜索分页查询，最大数据条数10000
			Pageable pageable = new PageRequest(0, 10000);
			searchQuery.setPageable(pageable); // 分页效果

			// 有条件查询 、查询索引库
			return wayBillIndexRepository.search(searchQuery).getContent();
		}
	}

}
