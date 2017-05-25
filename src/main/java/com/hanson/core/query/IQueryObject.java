package com.hanson.core.query;

import java.util.Map;

/**
 * 查询对象 统一接口
 * @author hanson
 *
 */
public interface IQueryObject {
	
	/**
	 * 得到hql查询串
	 * @return
	 */
	public String getQueryHqlStr();
	
	/**
	 * 得到查询结果总记录数的hql串
	 * @return
	 */
	public String getQueryHqlStr_forTotalRows();
	
	/**
	 * 得到查询参数对应的值对
	 * @return
	 */
	public Map getParams();
	
	/**
	 * 得到当前的页码
	 * @return
	 */
	public int getCurrentPage();
	
	/**
	 * 得到每页的记录数
	 * @return
	 */
	public int getPageRows();
}
