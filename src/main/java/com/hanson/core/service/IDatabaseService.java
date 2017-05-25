package com.hanson.core.service;

import com.hanson.core.dao.BaseDAO;

public interface IDatabaseService {

	/**
	 * 执行原生的sql语句
	 * @param sql
	 * @return 成功条数
	 */
	public abstract int excuteBySql(String sql);
}
