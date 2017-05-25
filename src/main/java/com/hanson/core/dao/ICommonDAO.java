package com.hanson.core.dao;

import java.util.List;
import java.util.Map;

/**
 * 通用的数据访问对象接口
 * @author hanson
 *
 */
public abstract interface ICommonDAO {

	/**
	 * 根据主键id获取相应实体对象
	 * @param id
	 * @return 实体对象
	 */
	public abstract Object getObjById(long id);
	
	/**
	 * 插入实体对象
	 * @param obj
	 * @return 是否成功
	 */
	public abstract boolean save(Object obj);
	
	/**
	 * 根据主键id删除实体
	 * @param id
	 * @return 是否成功
	 */
	public abstract boolean delete(long id);
	
	/**
	 * 更新实体对象
	 * @param obj
	 * @return 是否成功
	 */
	public abstract boolean update(Object obj);
	
	/**
	 * 保存或更新实体对象   存在主键则更新，无主键则保存
	 * @param obj
	 * @return 是否成功
	 */
	public abstract boolean saveOrUpdate(Object obj);
	
	/**
	 * 根据实体的某个属性获取实体对象
	 * @param paramName  属性名
	 * @param paramValue  属性值
	 * @return
	 */
	public abstract Object getObjByProperty(String paramName, Object paramValue);
	
	/**
	 * 根据实体的多个属性获取实体对象
	 * @param paramNames  属性名数组
	 * @param paramValues  属性值数组
	 * @return
	 */
	public abstract Object getObjByProperties(String[] paramNames, Object[] paramValues);
	
	/**
	 * 根据对象属性查询对象列表
	 * @param paramNames  属性名数组
	 * @param paramValues  属性值数组
	 * @param begin 记录开始的位置
	 * @param max  取出记录条数
	 * @return
	 */
	public abstract List queryByProperties(String[] paramNames, Object[] paramValues, int begin, int max);
	
	/**
	 * 查询对象列表
	 * @param queryStr 查询的hql
	 * @param params 参数值对
	 * @param begin 记录开始的位置
	 * @param max  取出记录条数
	 * @return
	 */
	public abstract List query(String queryStr, Map params, int begin, int max);
	
	/**
	 * 查询总的结果数
	 * @param queryStr 查询的hql
	 * @param params 参数值对
	 * @return
	 */
	public abstract int queryTotalRows(String queryStr, Map params);
	
}
