package com.hanson.view.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.constant.Globals;
import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.FileHelper;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.WebFormHelper;
import com.hanson.foundation.domain.Accessory;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.GoodsClass;
import com.hanson.foundation.service.IAccessoryService;
import com.hanson.foundation.service.IGoodsClassService;
import com.hanson.foundation.service.IGoodsService;

@Controller
@RequestMapping("/admin")
public class GoodsAdminController {

	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsClassService goodsClassService;
	@Autowired
	private IAccessoryService accessoryService;
	
	@RequestMapping("/goods_list")
	public ModelAndView goods_list(HttpServletRequest request, String currentPage, String pageRows){
		JModelAndView mv = new JModelAndView("goods_list.html", 11, request);
		QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, null, null);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(Goods.class, qo, request, fields, "like");
		IPageObject pageObj = this.goodsService.list(qo);
		mv.addObject("pageObj", pageObj);
		return  mv;
	}
	
	@RequestMapping("/goods_add")
	public ModelAndView goods_add(HttpServletRequest request, String obj_id){
		JModelAndView mv = new JModelAndView("goods_add.html", 11, request);
		if(StringUtils.isNotEmpty(obj_id)){
			Goods obj = this.goodsService.getObjById(Long.valueOf(obj_id));
			mv.addObject("obj", obj);
		}
		mv.addObject("goods_class_list", this.goodsClassService.listAll());
		return  mv;
	}
	
	@RequestMapping("/goods_save")
	public ModelAndView goods_save(HttpServletRequest request, String obj_id, Goods goods, 
			String goods_class_id, @RequestParam MultipartFile main_photo_file, MultipartFile[] photo_list_file){
		JModelAndView mv = new JModelAndView("handle_msg.html", 11, request);
		boolean result = false;
		String error_msg = "";
		Goods goods_true = null;
		GoodsClass gc = StringUtils.isNotEmpty(goods_class_id) ? this.goodsClassService.getObjById(Long.valueOf(goods_class_id)) : null;
		if(StringUtils.isNotEmpty(obj_id)){
			// 编辑商品
			goods_true = this.goodsService.getObjById(Long.valueOf(obj_id));
			goods_true = (Goods) WebFormHelper.toPo(request, goods, goods_true, "obj", 0);
			goods_true.setGoods_class(gc);
			Map uploadConfig = new HashMap();
			if(main_photo_file!=null){
				uploadConfig.put("multipartFile", main_photo_file);
				uploadConfig.put("uploadFolderPath", Globals.DEFAULT_UPLOAD_FOLDER + File.separator + "goods" + File.separator + "photo_main");
				uploadConfig.put("originalAccessory", goods_true.getMain_photo());
				Accessory acc_main = FileHelper.saveFileToAcc(request, uploadConfig);
				this.accessoryService.saveOrUpdate(acc_main);
				goods_true.setMain_photo(acc_main);
				result = this.goodsService.save(goods_true);
			}
			if(photo_list_file!=null){
				uploadConfig.clear();
				uploadConfig.put("uploadFolderPath", Globals.DEFAULT_UPLOAD_FOLDER + File.separator + "goods" + File.separator + "photo_other");
				for(MultipartFile photo_other : photo_list_file){
					uploadConfig.put("multipartFile", photo_other);
					Accessory acc_other = FileHelper.saveFileToAcc(request, uploadConfig);
					acc_other.setGoods(goods_true);
					System.out.println("acc_other:"+acc_other.getName());
					this.accessoryService.save(acc_other);
				}
			}
		}else{
			// 新增商品
			goods_true = WebFormHelper.toPo(request, goods, Goods.class, "obj", 0);
			goods_true.setGoods_class(gc);
			Map uploadConfig = new HashMap();
			if(main_photo_file!=null){
				uploadConfig.put("multipartFile", main_photo_file);
				uploadConfig.put("uploadFolderPath", Globals.DEFAULT_UPLOAD_FOLDER + File.separator + "goods" + File.separator + "photo_main");
				Accessory acc_main = FileHelper.saveFileToAcc(request, uploadConfig);
				this.accessoryService.save(acc_main);
				goods_true.setMain_photo(acc_main);
				result = this.goodsService.save(goods_true);
			}
			if(photo_list_file!=null){
				uploadConfig.clear();
				uploadConfig.put("uploadFolderPath", Globals.DEFAULT_UPLOAD_FOLDER + File.separator + "goods" + File.separator + "photo_other");
				for(MultipartFile photo_other : photo_list_file){
					uploadConfig.put("multipartFile", photo_other);
					Accessory acc_other = FileHelper.saveFileToAcc(request, uploadConfig);
					acc_other.setGoods(goods_true);
					this.accessoryService.save(acc_other);
				}
			}
		}
		
		String msg_title = result ? "操作成功" : error_msg;
		String ctx = CommUtil.getContextPath(request);
		mv.addObject("result", result);
		mv.addObject("msg_title", msg_title);
		mv.addObject("error_msg", error_msg);
		mv.addObject("list_url", ctx + "/admin/goods_list");
		mv.addObject("add_url", ctx + "/admin/goods_add");
		if(goods_true!=null){
			mv.addObject("edit_url", ctx + "/admin/goods_add?obj_id=" + goods_true.getId());
		}
		return  mv;
	}
	
	@ResponseBody
	@RequestMapping("/goods_delete")
	public Map goods_delete(HttpServletRequest request, String obj_id){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.isNotEmpty(obj_id)){
			result = this.goodsService.maintainDelete(Long.valueOf(obj_id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	// 文件上传测试
	@ResponseBody
	@RequestMapping(value="/photo_list_file_upload")  
    public Map photo_list_file_upload(HttpServletRequest request, @RequestParam MultipartFile[] photo_list_file) throws IOException{  
        //如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解  
        //如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解  
        //并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件  
        for(MultipartFile file : photo_list_file){  
            if(file.isEmpty()){  
                System.out.println("文件未上传");  
            }else{  
                System.out.println("文件长度: " + file.getSize());  
                System.out.println("文件类型: " + file.getContentType());  
                System.out.println("文件名称: " + file.getName());  
                System.out.println("文件原名: " + file.getOriginalFilename());  
                System.out.println("========================================");  
                //如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中  
                String realPath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");  
                //这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, file.getOriginalFilename()));  
            }  
        }
        Map data = new HashMap();
        data.put("result", true);
        return data;  
    }
	
	/**
	 * 单个商品图片删除
	 * @param request
	 * @param obj_id
	 * @param photo_type 图片的类型    main表示主图， other表示其它图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/goods_photo_delete")
	public Map goods_photo_delete(HttpServletRequest request, String obj_id, String photo_type){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		if(StringUtils.equals(photo_type, "main") && StringUtils.isNotEmpty(obj_id)){
			// 删除主图
			Long acc_id = Long.valueOf(obj_id);
			Goods goods = this.goodsService.getObjByProperty("main_photo.id", acc_id);
			goods.setMain_photo(null);
			this.goodsService.update(goods);
			result = this.accessoryService.maintainDelete(acc_id);
		}else if(StringUtils.equals(photo_type, "other") && StringUtils.isNotEmpty(obj_id)){
			// 删除其它图片
			result = this.accessoryService.maintainDelete(Long.valueOf(obj_id));
		}else{
			error_msg = "参数有误";
		}
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
}
