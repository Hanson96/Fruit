package com.hanson.core.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.hanson.core.dao.BaseDAO;
import com.hanson.core.dao.CommonDAO;
import com.hanson.core.dao.ICommonDAO;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.PageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.service.ICommonService;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.SimpleMap;

public class CommonServiceImpl<T> implements ICommonService<T>{
	
	@Resource(name="BaseDAO")
	protected BaseDAO baseDao;
	
	protected ICommonDAO dao;
	
	protected Class<T> clazz;
	
	@PostConstruct
	public void init(){
		Type type = this.getClass().getGenericSuperclass(); //获取当前类的超类也就是 CommonServiceImpl
		if(type instanceof ParameterizedType){ // 判断这个超类是不是参数化类型的
	    	Type paramtype1 = ((ParameterizedType)type).getActualTypeArguments()[0]; // 获取第一个参数
		    this.clazz = (Class<T>)paramtype1;
		    dao = new CommonDAO(this.clazz, this.baseDao);
	    }
	}
	
	@Override
	public T getObjById(long id) {
		return (T)dao.getObjById(id);
	}

	@Override
	public boolean save(T obj) {
		return dao.save(obj);
	}

	@Override
	public boolean delete(long id) {
		return dao.delete(id);
	}

	@Override
	public boolean update(T obj) {
		return dao.update(obj);
	}
	
	@Override
	public boolean saveOrUpdate(T obj) {
		return dao.saveOrUpdate(obj);
	}
	
	@Override
	public T getObjByProperty(String paramName, Object paramValue) {
		return (T) dao.getObjByProperty(paramName, paramValue);
	}
	
	@Override
	public T getObjByProperties(String[] paramNames, Object[] paramValues) {
		return (T) dao.getObjByProperties(paramNames, paramValues);
	}

	@Override
	public List<T> queryByProperties(String[] paramNames, Object[] paramValues, int begin, int max){
		return (List<T>)dao.queryByProperties(paramNames, paramValues, begin, max);
	}
	
	@Override
	public List<T> query(String queryStr, Map params, int begin, int max){
		return (List<T>)dao.query(queryStr, params, begin, max);
	}
	
	@Override
	public List<T> query(QueryObject qo){
		qo.setClazz(clazz);
		return (List<T>)dao.query(qo.getQueryHqlStr(), qo.getParams(), qo.getBegin(), qo.getMax());
	}
	
	@Override
	public List<T> listAll() {
		QueryObject qo = new QueryObject(clazz);
		return (List<T>)dao.query(qo.getQueryHqlStr(), qo.getParams(), -1, -1);
	}
	
	public IPageObject list(QueryObject qo) {
		qo.setClazz(clazz);
		PageObject po = new PageObject();
		int totalRows = dao.queryTotalRows(qo.getQueryHqlStr_forTotalRows(), qo.getParams()); // 总记录数
		if(totalRows > 0){
			po.setTotalRows(totalRows);
			int totalPages = (totalRows - 1) / qo.getPageRows() + 1; // 总页数
			int currentPage = qo.getCurrentPage() > totalPages ? totalPages : qo.getCurrentPage();
			if(currentPage < 1){
				currentPage = 1;
			}
			qo.setCurrentPage(currentPage);
			List resultList = dao.query(qo.getQueryHqlStr(), qo.getParams(), qo.getBegin(), qo.getMax());
			po.setTotalRows(totalRows);
			po.setResultList(resultList);
			po.setCurrentPage(currentPage);
			po.setTotalPages(totalPages);
		}
		return po;
	}

	@Override
	public int queryTotalRows(String queryStr, Map params) {
		return dao.queryTotalRows(queryStr, params);
	}

	@Override
	public int queryTotalRows(QueryObject qo) {
		qo.setClazz(clazz);
		int totalRows = dao.queryTotalRows(qo.getQueryHqlStr_forTotalRows(), qo.getParams());
		return totalRows;
	}

	@Override
	public int queryTotalRows(String[] paramNames, Object[] paramValues) {
		QueryObject qo = new QueryObject(clazz);
		if(CommUtil.isNotNull(paramNames) && CommUtil.isNotNull(paramValues) 
				&& paramNames.length>0 && paramValues.length>0 && paramNames.length == paramValues.length){
			for(int i=0; i<paramNames.length; i++){
				String paramName_handle = paramNames[i].replace(".", "_"); //替换掉点号
				qo.addQuery("obj."+paramNames[i], new SimpleMap(paramName_handle, paramValues[i]), "=");
			}
		}
		int totalRows = dao.queryTotalRows(qo.getQueryHqlStr_forTotalRows(), qo.getParams());
		return totalRows;
	}
}
