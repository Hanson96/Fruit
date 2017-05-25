package com.hanson.core.query;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页列表
 * @author hanson
 *
 */
public class PageObject implements IPageObject{

	// 当前页码   默认0
	private int currentPage;
	// 每页的记录数 默认0
	private int pageRows;
	// 总页数  默认0
	private int totalPages;
	// 总记录数  默认0
	private int totalRows;
	
	private List resultList = new ArrayList();
	
	public int getNextPage() {//下一页，不超过最大页数
		int p = this.currentPage + 1;
		if (p > this.totalPages)
		    p = this.totalPages;
		return p;
	}

	public int getPreviousPage() {//上一页，不小于第一页
		int p = this.currentPage - 1;
		if (p < 1)
		    p = 1;
		return p;
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

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public List getResultList() {
		return resultList;
	}

	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
}
