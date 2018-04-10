package com.rongyue.web.base.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.domain.base.Area;
import com.rongyue.service.base.AreaService;
import com.rongyue.utils.PinYinDuoYinUtils;

public class AreaAction extends BaseAction<Area>{
	@Autowired
	private AreaService areaService;
	
	//上传文件
	private File file;
	
	public void setFile(File file) {
		this.file = file;
	}
	
	//一键文件上传 EXCEL POI插件
	@Action(value="area_import")
	public String fileImport(){
		try{
			List<Area> list = new ArrayList<Area>();
			//基于.xsl解析HSSF
			//1、加载excel文件对象
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
			//2、读取一个sheet(第一个)
			HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
			//3、读取sheet每一行
			for (Row row : sheetAt) {
				//第一行不读取
				if(row.getRowNum() == 0){
					continue;
				}
				//如果是空行则跳过
				if(row.getCell(0)== null || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
					continue;
				}
				Area area = new Area();
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());
				//利用PinYinDuoYinUtils工具类对省市区进行城市编码和简码的编写
				String province = area.getProvince().substring(0,area.getProvince().length()-1);
				String city = area.getCity().substring(0,area.getCity().length()-1);
				String district = area.getDistrict().substring(0,area.getDistrict().length()-1);
				String data = province + city + district;
				area.setShortcode(PinYinDuoYinUtils.convertChineseTogetHeadByString(data, true));
				area.setCitycode(PinYinDuoYinUtils.convertChineseToPinyin(data, false));
				list.add(area);
			}
			areaService.save(list);
		}catch(Exception e){
			e.printStackTrace();
		}	
		return NONE;
	}
	
	//条件分页查询
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Specification<Area> specification = new Specification<Area>() {
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//根据省份查询
				if(StringUtils.isNotBlank(model.getProvince())){
					Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}
				//根据城市查询
				if(StringUtils.isNotBlank(model.getCity())){
					Predicate p2 = cb.like(root.get("city").as(String.class),"%"+model.getCity()+"%");
					list.add(p2);
				}
				//根据区域查询
				if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Page<Area> pageData = areaService.findAll(specification,pageable);
		pushPageToStack(pageData);
		return SUCCESS;
	}
	
	//省市区的添加和修改
	@Action(value="area_save",results={@Result(name="success",location="/pages/base/area.html",type="redirect")})
	public String save(){
		areaService.saveSingle(model);
		return SUCCESS;
	}
	
	//省市区的删除
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Action(value="area_del",results={@Result(name="success",location="/pages/base/area.html",type="redirect")})
	public String del(){
		String[] idArr = ids.split(",");
		areaService.del(idArr);
		return SUCCESS;
	}
	
	//分区subarea添加区域中：查找所有区域
	@Action(value="area_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Area> list = areaService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	

}
