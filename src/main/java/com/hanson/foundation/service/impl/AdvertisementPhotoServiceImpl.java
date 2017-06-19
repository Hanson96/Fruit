package com.hanson.foundation.service.impl;

import org.springframework.stereotype.Service;

import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.FileHelper;
import com.hanson.foundation.domain.AdvertisementPhoto;
import com.hanson.foundation.service.IAdvertisementPhotoService;

@Service
public class AdvertisementPhotoServiceImpl extends CommonServiceImpl<AdvertisementPhoto> implements IAdvertisementPhotoService{

	@Override
	public boolean cascadeDelete(Long id) {
		AdvertisementPhoto obj = this.getObjById(id);
		return cascadeDelete(obj);
	}
	
	private boolean cascadeDelete(AdvertisementPhoto obj){
		return this.delete(obj.getId());
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		AdvertisementPhoto obj = this.getObjById(id);
		FileHelper.deleteAcc(CommUtil.getServletContextBySpring(), obj.getAcc()); // 删除文件
		boolean result = cascadeDelete(obj);
		return result;
	}
}
