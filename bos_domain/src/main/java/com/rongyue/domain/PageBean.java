package com.rongyue.domain;
//前台显示分页类
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="PageBean")
//WebService CXF 返回带有泛型的集合的类型
@XmlSeeAlso({Promotion.class})
public class PageBean<T> {
	private Integer totalCount; //总记录数
	private List<T> pageData; //当前页数据
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	
	
}
