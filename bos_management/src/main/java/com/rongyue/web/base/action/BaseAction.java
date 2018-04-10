package com.rongyue.web.base.action;


import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	//子类访问
	protected T model;

	@Override
	public T getModel() {
		return model;
	}
	
	//构造函数，完成model的实例化
	public BaseAction(){
		//获取继承父类型的泛型参数
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		//获取T的Class对象
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		//构造model对象
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("构造模型失败");
		} 
	}
	
	//构造分页参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	protected void pushPageToStack(Page<T> page){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		ActionContext.getContext().getValueStack().push(map);
	}
}
