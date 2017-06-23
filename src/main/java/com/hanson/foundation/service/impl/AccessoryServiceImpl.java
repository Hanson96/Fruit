package com.hanson.foundation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.FileHelper;
import com.hanson.foundation.domain.Accessory;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.service.IAccessoryService;
import com.hanson.foundation.service.IGoodsService;

@Service
public class AccessoryServiceImpl extends CommonServiceImpl<Accessory> implements IAccessoryService{

	@Autowired
	private IGoodsService goodsService;
	
	@Override
	public boolean cascadeDelete(Long id) {
		Accessory obj = this.getObjById(id);
		Goods goods = this.goodsService.getObjByProperty("main_photo.id", obj.getId());
		if(goods!=null){
			goods.setMain_photo(null);
			this.goodsService.update(goods);
		}
		FileHelper.deleteAcc(CommUtil.getServletContextBySpring(), obj); // 删除图片
		return this.delete(obj.getId());
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		boolean result = this.cascadeDelete(id);
		return result;
	}
}
