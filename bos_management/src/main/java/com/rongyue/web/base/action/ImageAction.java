package com.rongyue.web.base.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionContext;
import com.rongyue.utils.UUIDUtils;


public class ImageAction extends BaseAction<Object>{
	//kindeditor进行图片上传
	private File imgFile;
	private String imgFileFileName;
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	@Action(value="image_upload",results={@Result(name="success",type="json")})
	public String upload(){
		//上传图片的绝对路径 (本项目下的绝对路径)
		String dirPath = "E:/WorkSpace_sts/bos_dev_3/logistics_bos_project/upload/";
		//上传图片的服务器路径
		String serverPath = "http://localhost:8080/upload/";
		//随机创建文件名
		String filename = UUIDUtils.getUUID();
		//获取文件的后缀名
		String ext = FilenameUtils.getExtension(imgFileFileName);
		
		filename = filename + "." + ext;
		
		try {
			//保存文件
			FileUtils.copyFile(imgFile, new File(dirPath + filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//返回上传成功的json信息
		//{"error" : 0,"url" : "http://www.example.com/path/to/file.ext"}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("error", 0);
		map.put("url", serverPath + filename);
		
		ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}
	
	//kindeditor图片空间
	@Action(value="image_manage",results={@Result(name="success",type="json")})
	public String manage(){
		String rootPath = "E:/WorkSpace_sts/bos_dev_3/logistics_bos_project/upload/";
		String rootUrl = "http://localhost:8080/upload/";
		File directory = new File(rootPath);
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		if (directory.listFiles() != null) {
			for (File file : directory.listFiles()) {
				Map<String, Object> hash = new HashMap<>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else {
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", true);
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}

		Map<String, Object> result = new HashMap<>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);

		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;

	}
}
