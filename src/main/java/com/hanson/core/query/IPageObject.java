package com.hanson.core.query;

import java.util.List;

/**
 * 页面对象  存放页面的相关信息
 * @author hanson
 *
 */
public interface IPageObject {

	/**
	 * 获取当前页码
	 * @return
	 */
	public abstract int getCurrentPage();
	
	/**
	 * 获取每页的记录数
	 * @return
	 */
	public abstract int getPageRows();
	
	/**
	 * 获取总页数
	 * @return
	 */
	public abstract int getTotalPages();
	
	/**
	 * 获取总记录数
	 * @return
	 */
	public abstract int getTotalRows();
	
	/**
	 * 获取查询结果列表
	 * @return
	 */
	public abstract List getResultList();
}
