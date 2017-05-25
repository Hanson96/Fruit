package com.hanson.core.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hanson.core.dao.BaseDAO;
import com.hanson.core.service.IDatabaseService;

@Service
public class DatabaseServiceServiceImpl implements IDatabaseService{

	private static final Logger log = LoggerFactory.getLogger(DatabaseServiceServiceImpl.class);
	
	@Resource(name="BaseDAO")
	protected BaseDAO baseDao;
	
	public int excuteBySql(String sql){
		int count = 0;
		try{
			count = this.baseDao.excuteBySql(sql);
		}catch(Exception e){
			count = -1;
			log.error("sql["+sql+"]语句执行发生错误", e);
		}
		return count;
	}
	
}
