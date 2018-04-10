package com.rongyue.mq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.rongyue.utils.SmsUtils;
@Component
public class QueueConsumer implements MessageListener{

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		try {
			String result = SmsUtils.sendSmsByHTTP(mapMessage.getString("mobilePhone"), 
					mapMessage.getString("msg"));
			if(result.startsWith("000")){
				//发送成功
				System.out.println("发送短信成功");
			}else{
				//发送失败
				System.out.println("发送短信失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
