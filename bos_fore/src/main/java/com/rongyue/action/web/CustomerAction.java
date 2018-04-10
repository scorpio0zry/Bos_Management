package com.rongyue.action.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.loader.plan.build.spi.ReturnGraphTreePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.rongyue.constants.Constants;
import com.rongyue.crm.domain.Customer;
import com.rongyue.uils.MailUtils;
import com.rongyue.uils.UUIDUtils;

public class CustomerAction extends BaseAction<Customer> {
	
	//异步校验手机号是否存在
	@Action(value="signup_checkPhone")
	public String checkPhone(){
		Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/findByMobilePhone/" + 
				model.getMobilePhone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		try{
			if(customer != null){
				//注册手机号已经存在
				ServletActionContext.getResponse().getWriter().print(false);
			}else{
				//注册手机号不存在
				ServletActionContext.getResponse().getWriter().print(true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return NONE;
	}
	
	//异步校验邮箱是否存在
	@Action(value="signup_checkEmail")
	public String checkEmail(){
		Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/findByEmail/"+ model.getEmail())
			.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if(customer != null){
			try {
				//该邮箱已经被注册
				ServletActionContext.getResponse().getWriter().print(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				//该邮箱未被注册
				ServletActionContext.getResponse().getWriter().print(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}

	// 发送验证码
	//注入jmsTemplate生产者
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource(name="queueDestination")
	private Destination destination;
	
	@Action(value = "signup_sendSms")
	public String sendSms() {
		// 生成4位验证码
		String randomCode = RandomStringUtils.randomNumeric(4);
		// 将验证码存入到session
		ServletActionContext.getRequest()
			.getSession().setAttribute(model.getMobilePhone(), randomCode);
		System.out.println("生成验证码：" + randomCode);
		final String msg = "尊敬的用户，您好！您本次申请的验证码：" + randomCode;
		
		jmsTemplate.send(destination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("mobilePhone", model.getMobilePhone());
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});
		return NONE;
	}
	
	//客户注册
	private String checkCode;
	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Action(value="signup_register",results={@Result(name="success",location="signup-success.html",type="redirect"),
			@Result(name="none",location="signup.html",type="redirect")})
	public String register(){
		String checkCodeSession = (String) ServletActionContext.getRequest()
				.getSession().getAttribute(model.getMobilePhone());
		if(checkCodeSession == null || !checkCodeSession.equals(checkCode)){
			System.out.println("验证码输入错误");
			return NONE;
		}
		/*crm_management进行数据库写入*/
		WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/register")
			.type(MediaType.APPLICATION_JSON).post(model);
		//向客户邮箱发送邮件
		String activeCode = UUIDUtils.getUUID();
		//将激活码放到redis中
		redisTemplate.opsForValue().set(model.getMobilePhone(), activeCode,24,TimeUnit.HOURS);
		String content = "尊敬的客户，您好！请您在二十四小时内对邮箱进行绑定，请点击下面地址进行绑定:<br/><a href='"+
				MailUtils.activeUrl+"?mobilePhone="+model.getMobilePhone()+"&activeCode="+activeCode
				+"'>邮箱激活地址</a>";
		MailUtils.sendMail("客户激活", content, model.getEmail());
		return SUCCESS;
	}
	
	//激活成功后跳转到指定页面
	//邮箱激活码激活
	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	
	@Action(value="signup_activeMail",results={@Result(name="success",location="http://localhost:9003/bos_fore/msg.html",type="redirect")})
	public String activeMail(){
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		String activeCodeRedis = redisTemplate.opsForValue().get(model.getMobilePhone());
		if(activeCodeRedis == null || !activeCode.equals(activeCodeRedis)){
			//激活码无效,向页面输出错误信息
			try {
				ServletActionContext.getResponse().getWriter().println("<center><h1>您的激活码已无效，请重新绑定邮箱<h1></center>");
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}else{
			//激活码有效
			Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/findByMobilePhone/" + 
					model.getMobilePhone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if(customer.getType() == null){
				//邮箱未被激活，进行激活
				WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/updateType/" +
						model.getMobilePhone()).post(null);
				try {
					ServletActionContext.getResponse().getWriter().println("<center><h1>您的邮箱已经被成功绑定<h1></center>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				//邮箱已经被激活
				try {
					ServletActionContext.getResponse().getWriter().println("<center><h1>您的邮箱已经被绑定，无需再次绑定<h1></center>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//删除redis中的激活码
		redisTemplate.delete(model.getMobilePhone());
		return SUCCESS;
	}
	
	//用户登录login
	@Action(value="customer_login",results={@Result(name="success",location="index.html#/myhome",type="redirect"),
			@Result(name="login",location="login.html",type="redirect")})
	public String login(){
		Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/login?mobilePhone="
				+ model.getMobilePhone() + "&password=" + model.getPassword()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		//登录失败，用户没有查找到
		if(customer == null){
			return LOGIN;
		}
		
		//登录成功
		ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
		return SUCCESS;
	}
}
