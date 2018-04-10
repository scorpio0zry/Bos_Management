package com.rongyue.serviceimpl.take_delivery;

import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongyue.constants.Constants;
import com.rongyue.dao.base.AreaRepository;
import com.rongyue.dao.base.FixedAreaRepository;
import com.rongyue.dao.base.OrderRepository;
import com.rongyue.dao.base.WorkBillRepository;
import com.rongyue.domain.base.Area;
import com.rongyue.domain.base.Courier;
import com.rongyue.domain.base.FixedArea;
import com.rongyue.domain.base.SubArea;
import com.rongyue.domain.take_delivery.Order;
import com.rongyue.domain.take_delivery.WorkBill;
import com.rongyue.service.take_delivery.OrderService;
import com.rongyue.utils.UUIDUtils;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	@Autowired
	private WorkBillRepository workBillRepository;

	/**
	 * 订单存储
	 */
	@Override
	public void saveOrder(Order order) {
		order.setOrderNum(UUIDUtils.getUUID()); // 订单号
		order.setOrderTime(new Date()); // 订单时间
		order.setStatus("1"); // 订单状态

		// 获取数据库存储的省市区的id值重新存入order订单中
		Area sendArea = order.getSendArea();
		Area presistSendArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(),
				sendArea.getCity(), sendArea.getDistrict());
		Area recArea = order.getRecArea();
		Area presistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
				recArea.getDistrict());

		order.setSendArea(presistSendArea);
		order.setRecArea(presistRecArea);

		// 自动分单 从CRM数据库中查询寄件人用户地址是否绑定了定区FixedAreaId,通过定区查询到相关的快递员，进行业务绑定
		String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL
				+ "/services/customerService/findFixedAreaIdByAddress/" + order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		if (fixedAreaId != null) {
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			if (iterator.hasNext()) {
				Courier courier = iterator.next();
				if (courier != null) {
					// 将快递员与订单绑定
					saveOrder(order, courier);
					// 生成工单 发送短信
					generateWorkBill(order);

					return;
				}
			}
		}

		// 自动分单 通过查询分区关键字或者辅助关键字，匹配地址
		for (SubArea subArea : presistSendArea.getSubareas()) {
			if (order.getSendAddress().contains(subArea.getKeyWords())
					|| order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				// 查找到分区，匹配相关定区的快递员
				Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
				if (iterator.hasNext()) {
					Courier courier = iterator.next();
					if (courier != null) {
						// 将快递员与订单绑定
						saveOrder(order, courier);
						// 生成工单 发送短信
						generateWorkBill(order);

						return;
					}
				}

			}
		}

		

		// 如果都没有查询到，进入人工分单
		order.setOrderType("2");
		System.out.println("人工分担中...");
		orderRepository.save(order);

	}

	// 注入jmsTemplate生产者 ActiveMQ
	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource(name = "queueDestination")
	private Destination destination;

	// 生成工单 发送短信
	private void generateWorkBill(final Order order) {
		// 生成工单
		WorkBill workBill = new WorkBill();
		workBill.setType("新"); // 工单类型
		workBill.setPickstate("新单"); // 取件状态
		workBill.setBuildtime(new Date()); // 工单生成时间
		workBill.setRemark(order.getRemark()); // 定义备注
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber); // 短信序号4位
		workBill.setCourier(order.getCourier());
		workBill.setOrder(order);
		workBillRepository.save(workBill);

		// 发送短信
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				String msg = "短信序号：" + smsNumber + ",取件地址：" + order.getSendAddress() + ",联系人:" + order.getSendName()
						+ ",手机:" + order.getSendMobile() + "，快递员捎话：" + order.getSendMobileMsg();
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("mobilePhone", order.getCourier().getTelephone());
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});

		// 修改工单状态
		workBill.setPickstate("已通知");

	}

	// 将快递员与订单绑定
	private void saveOrder(Order order, Courier courier) {
		// 将快递员关联订单上
		order.setCourier(courier);
		// 设置自动分单
		order.setOrderType("1");
		// 保存订单
		orderRepository.save(order);
	}
	
	//waybill 异步通过订单编号查询订单信息
	@Override
	public Order findByOrderNum(String orderNum) {
		return orderRepository.findByOrderNum(orderNum);
	}

}
