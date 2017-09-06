package com.babel.common.core.page;

public class PageDTO {
	private Integer page=1;
	private Integer rows=20;
	private Integer pageSize=20;
	/**
	 * column
	 */
	private String sort;
	/**
	 * asc/desc
	 */
	private String order;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	/**
	 * column
	 * @return
	 */
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	/**
	 * asc/desc
	 * @return
	 */
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
}
