package com.hanson.core.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.SimpleMap;

/**
 * 查询对象   将对对象的查询转化为相应的sql语句
 * @author hanson
 *
 */
public class QueryObject implements IQueryObject{
	// 查询的实体类
	private Class<?> clazz;
	// 当前页数  默认第1页
	private int currentPage = 1;
	// 每页显示的记录数  默认十条
	private int pageRows = 10;
	// 排序字段   默认(addTime)
	private String orderBy = "addTime";
	// 排序类型  asc,desc  默认(desc)
	private String orderType = "desc";
	// 查询字段
	private String select_str;
	// 条件字段
	private String where_str = " where 1=1 ";
	// 排序字段
	private String order_str;
	// 变量参数
	private Map params = new HashMap();
	// 结果开始的位置   默认0 表示从头开始
	private int begin = 0;
	// 结果的数量  默认0 表示没有限制
	private int max = 0;
	
	public QueryObject(){
		
	}
	
	public QueryObject(Class<?> clazz){
		setClazz(clazz);
	}
	
	public QueryObject(ModelAndView mv, String currentPage, String pageRows, String orderBy, String orderType){
		if(CommUtil.isNotNull(currentPage)){
			setCurrentPage(CommUtil.null2Int(currentPage));
		}
		if(CommUtil.isNotNull(pageRows)){
			int number = CommUtil.null2Int(pageRows);
			if(number>0){
				setPageRows(CommUtil.null2Int(pageRows));
			}
		}
		if(CommUtil.isNotNull(orderBy)){
			setOrderBy(orderBy);
		}
		if(CommUtil.isNotNull(orderType)){
			setOrderType(orderType);
		}
		mv.addObject("currentPage", this.currentPage);
		mv.addObject("pageRows", this.pageRows);
		mv.addObject("orderBy", this.orderBy);
		mv.addObject("orderType", this.orderType);
	}
	
	/**
	 * 添加多个条件字符串   如  obj.name=:name and obj.value=:value
	 * @param paramString 参数字符串
	 * @param paramMap 参数值对
	 */
	public void addQuery(String paramString, Map paramMap){
		this.where_str += " "+paramString;
		if(paramMap!=null){
			Iterator iterator = paramMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry entry = (Map.Entry)iterator.next();
				this.params.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 添加单个条件字符串   如  addQuery("obj.name",new Simple("name","shifang"),"=")
	 * @param field  字段
	 * @param map 单一值对
	 * @param expression 条件表达式 (>,<,=,like)
	 */
	public void addQuery(String field, SimpleMap map, String expression){
		String str = field+ " " +handleExpression(expression)+ " :"+map.getKey();
		this.where_str += " and "+str;
		this.params.put(map.getKey(), map.getValue());
	}
	
	/**
	 * 添加单个条件字符串   如  addQuery("obj.name",new Simple("name","shifang"),"=","or")
	 * @param field  字段
	 * @param map 单一值对
	 * @param expression 条件表达式 (>,<,=,like)
	 * @param logic  逻辑连接符 (and,or)
	 */
	public void addQuery(String field, SimpleMap map, String expression, String logic){
		String str = field+ " " +handleExpression(expression)+ " :"+map.getKey();
		this.where_str += " "+ logic +" "+str;
		this.params.put(map.getKey(), map.getValue());
	}
	
	/**
     * @Description: 转换表达式，如果为空就转换为=号
     * @param expression
     * @return 
     */
    private String handleExpression(String expression) {
		if (expression == null) {
		    return "=";
		}
		return expression;
    }
	
    /**
     * 得到 hql查询串
     * @return
     */
	public String getQueryHqlStr(){
		return getSelect_str() + " " + getWhere_str() + " " + getOrder_str();
	}
	
	public String getQueryHqlStr_forTotalRows(){
		String select_str = getSelect_str();
		int from_index = select_str.indexOf("from");
		String select_count_str = " select count(*) " + select_str.substring(from_index);
		System.out.println("select_count_str:"+select_count_str);
		return select_count_str + getWhere_str();
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageRows() {
		return pageRows;
	}

	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public String getSelect_str() {
		if(CommUtil.isNull(select_str) && CommUtil.isNotNull(clazz)){
			String str = " select obj from " + clazz.getName() + " obj ";
			return str;
		}
		return select_str;
	}

	public void setSelect_str(String select_str) {
		this.select_str = select_str;
	}

	public String getWhere_str() {
		return where_str;
	}

	public void setWhere_str(String where_str) {
		this.where_str = where_str;
	}

	public String getOrder_str() {
		if(CommUtil.isNull(order_str)){
			String str = " order by obj." + getOrderBy() + " " + getOrderType() +" ";
			return str;
		}
		return order_str;
	}

	public void setOrder_str(String order_str) {
		this.order_str = order_str;
	}

	public int getBegin() {
		if(begin > 0){ // 如果是手动set的则直接返回
			return begin;
		}
		int currentPage = getCurrentPage();
		int pageRows = getPageRows();
		int begin = (currentPage-1) * pageRows;
		if(begin < 0){
			begin = 0;
		}
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getMax() {
		if(max > 0){ // 手动set的则直接返回
			return max;
		}
		int pageRows = getPageRows();
		if(pageRows > 0){
			return pageRows;
		}
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	

}
