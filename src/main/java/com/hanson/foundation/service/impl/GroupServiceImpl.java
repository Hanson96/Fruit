package com.hanson.foundation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.FileHelper;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.Group;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.foundation.service.IGroupService;

@Service
public class GroupServiceImpl extends CommonServiceImpl<Group> implements IGroupService{

	@Autowired
	private IGoodsService goodsService;
	
	@Override
	public boolean cascadeDelete(Long id) {
		Group obj = this.getObjById(id);
		return cascadeDelete(obj);
	}
	
	private boolean cascadeDelete(Group obj){
		// 取消商品的关联关系才可删除
		for(Goods goods : obj.getGoods_list()){
			goods.setGroup(null);
			goods.setActivity_status(Goods.ActivityStatus.NORMAL.value());
			this.goodsService.update(goods);
		}
		return this.delete(obj.getId());
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		Group obj = this.getObjById(id);
		FileHelper.deleteAcc(CommUtil.getServletContextBySpring(), obj.getAcc()); // 删除文件
		boolean result = cascadeDelete(obj);
		return result;
	}
	
}
