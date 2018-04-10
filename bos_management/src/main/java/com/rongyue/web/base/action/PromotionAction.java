package com.rongyue.web.base.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.rongyue.domain.Promotion;
import com.rongyue.service.base.PromotionService;
import com.rongyue.utils.UUIDUtils;
//宣传
public class PromotionAction extends BaseAction<Promotion>{
	
	/**
	 * 宣传任务的存储
	 */
	private File titleImgFile;
	private String titleImgFileFileName;
	
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}
	
	@Autowired
	private PromotionService promotionService;

	@Action(value="promotion_save",results={@Result(name="success",location="/pages/take_delivery/promotion.html",type="redirect")})
	public String save(){
		//将上传的图片保存到图片服务器中
		//上传图片的绝对路径 (本项目下的绝对路径)
		String dirPath = "E:/WorkSpace_sts/bos_dev_3/logistics_bos_project/upload/";
		//上传图片的服务器路径
		String serverPath = "http://localhost:8080/upload/";
		//随机创建文件名
		String filename = UUIDUtils.getUUID();
		//获取文件的后缀名
		String ext = FilenameUtils.getExtension(titleImgFileFileName);
		
		filename = filename + "." + ext;
		
		try {
			//保存文件
			FileUtils.copyFile(titleImgFile, new File(dirPath + filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//保存到实体类中
		model.setTitleImg(serverPath + filename);
		promotionService.save(model);
		return SUCCESS;
	}
	
	/**
	 * 宣传任务的页面查询 promotion.html
	 */
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> page = promotionService.findAll(pageable);
		pushPageToStack(page);
		return SUCCESS;
	}
	
	
}
