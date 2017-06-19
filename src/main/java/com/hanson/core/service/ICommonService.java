package com.hanson.core.service;

import java.util.List;
import java.util.Map;

import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;

/**
 * 通用的服务接口   调用ICommonDAO实现底层操作
 * @author hanson
 *
 * @param <T>
 */
public abstract interface ICommonService<T> {

	/**
	 * 根据主键id获取相应实体对象
	 * @param id
	 * @return 实体对象
	 */
	public abstract T getObjById(long id);
	
	/**
	 * 插入实体对象
	 * @param obj
	 * @return 是否成功
	 */
	public abstract boolean save(T obj);
	
	/**
	 * 根据主键id删除实体
	 * @param id
	 * @return 是否成功
	 */
	public abstract boolean delete(long id);
	
	/**
	 * 级联删除  删除关联此id的其它对象（即此id被其它对象作为外键，需要把这其它对象先删除）
	 * @param id
	 * @return
	 */
	public abstract boolean cascadeDelete(Long id);
	
	/**
	 * 维护删除   维护此id对象主动关联的其它对象（即此对象把其它对象的id作为外键）
	 * 代码逻辑：获取此对象的维护对象们，调用此对象的cascadeDelete级联删除，再对维护对象们进行维护（删除或更新信息）
	 * @param id
	 * @return
	 */
	public abstract boolean maintainDelete(Long id);
	
	/**
	 * 更新实体对象
	 * @param obj
	 * @return 是否成功
	 */
	public abstract boolean update(T obj);
	
	/**
	 * 保存或更新实体对象   存在主键则更新，无主键则保存
	 * @param obj
	 * @return 是否成功
	 */
	public abstract boolean saveOrUpdate(T obj);
	
	/**
	 * 根据实体的某个属性获取实体对象
	 * @param paramName  属性名
	 * @param paramValue  属性值
	 * @return
	 */
	public abstract T getObjByProperty(String paramName, Object paramValue);
	
	/**
	 * 根据实体的多个属性获取实体对象
	 * @param paramNames  属性名数组
	 * @param paramValues  属性值数组
	 * @return
	 */
	public abstract T getObjByProperties(String[] paramNames, Object[] paramValues);
	
	/**
	 * 根据QueryObject来查询某个唯一对象
	 * @param paramNames  属性名数组
	 * @param paramValues  属性值数组
	 * @return
	 */
	public abstract T getObjByQueryObj(QueryObject qo);
	
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
	 * 通过QueryObject 进行查询
	 * @param qo
	 * @return
	 */
	public abstract List<T> query(QueryObject qo);
	
	/**
	 * 查询所有的结果
	 * @return
	 */
	public abstract List<T> listAll();
	
	/**
	 * 根据查询对象查询结果
	 * @param qo 查询对象
	 * @return 返回页面对象
	 */
	public IPageObject list(QueryObject qo);
	
	/**
	 * 查询总的结果数
	 * @param queryStr 查询的hql
	 * @param params 参数值对
	 * @return
	 */
	public abstract int queryTotalRows(String queryStr, Map params);
	
	/**
	 * 通过QueryObject查询结果总数
	 * @param qo
	 * @return
	 */
	public abstract int queryTotalRows(QueryObject qo);
	
	/**
	 * 根据对象属性查询结果记录数
	 * @param paramNames  属性名数组
	 * @param paramValues  属性值数组
	 * @return
	 */
	public int queryTotalRows(String[] paramNames, Object[] paramValues);
	
	/**
	 * 检查是否存在某一字段同名的对象
	 * @param fieldName 字段的名称
	 * @param fieldValue 字段的值
	 * @param id 原对象的id 如果为新的对象则用空
	 * @return true表示存在同名数据对象    false表示不存在
	 */
	public abstract boolean existSameName(String fieldName, Object fieldValue, Long id);
}
