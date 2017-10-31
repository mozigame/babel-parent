package com.babel.common.core.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.babel.common.core.util.CommUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.pagehelper.PageInfo;


@JsonInclude(Include.NON_NULL)
public class PageVO<T> implements IPageVO<T>, java.io.Serializable{
	private static final Logger logger = Logger.getLogger(PageVO.class);
	public static final int MAX_PAGE_SIZE=50000;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalSize;
	private int pageSize;
	private int totalPage;
	private int currentPage;
	private Collection<T> datas;
	private String sort;//column
	private String order;//asc/desc
	public static List<String>commParamList=CommUtil.newList("sort","order","page","rows","startDate","endDate");
	private Map<String, String> paramMap=null;
	public PageVO(){
		paramMap=new HashMap<>();
	}
	public PageVO(int currentPage, int pageSize){
		this.currentPage=currentPage;
		this.pageSize=pageSize;
		paramMap=new HashMap<>();
		pageSizeCheck();
		
	}
	
	public PageVO(PageDTO page){
		this.currentPage=page.getPage();
		this.pageSize=page.getRows();
		if(page.getRows()==null){
			this.pageSize=page.getPageSize();
		}
		paramMap=new HashMap<>();
		pageSizeCheck();
		
	}
	
	public PageVO(ServletRequest request){
		Enumeration<String> names= request.getParameterNames();
		String name=null;
		paramMap=new HashMap<>();
		while(names.hasMoreElements()){
			name=names.nextElement();
			if(commParamList.contains(name)){
				continue;
			}
			paramMap.put(name, request.getParameter(name));
		}
		sort=request.getParameter("sort");
		order=request.getParameter("order");
		if("create_disp".equals(sort)){
			sort="createUser";
		}
		else if("modify_disp".equals(sort)){
			sort="modifyUser";
		}
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		if(StringUtils.isEmpty(page)){
			page="1";
		}
		if(StringUtils.isEmpty(rows)){
			rows="20";
		}
		this.currentPage=Integer.parseInt(page);
		this.pageSize=Integer.parseInt(rows);
		pageSizeCheck();
		
	}
	
	private final char UNDERLINE = '_';

	public String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
    
	/**
	 * 拼order排序条件，目前只支持单字段反序
	 * @return
	 */
    public String getOrderClause() {
    	String sort=getSort();
        if(StringUtils.isEmpty(sort)){
        	return null;
        }
        sort = camelToUnderline(sort);
        String order=getOrder();
        if(StringUtils.isEmpty(order)){
        	return null;
        }
        return sort+" "+order;
    }
	
	private void pageSizeCheck(){
		if(this.datas==null)
			this.datas=new ArrayList<>();
		if(pageSize>MAX_PAGE_SIZE){
			logger.warn("----Reset pageSize by out of limit:"+MAX_PAGE_SIZE);
			pageSize=MAX_PAGE_SIZE;
		}
	}

	public PageVO(Collection<T> datas, int totalSize, int pageSize, int currentPage) {
		this.datas=datas;
		this.totalSize = totalSize;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		pageSizeCheck();
		this.totalPage=this.calcTotalPage();
	}
	
	public PageVO(Collection<T> datas, PageVO pageVO) {
		this.datas=datas;
		this.totalSize = pageVO.getTotal();
		this.pageSize = pageVO.getPageSize();
		this.currentPage = pageVO.getCurrentPage();
		pageSizeCheck();
		this.totalPage=this.calcTotalPage();
	}
	
	public PageVO(PageInfo<T> pageInfo){
		if(pageInfo==null){
			return;
		}
		this.datas=pageInfo.getList();
		Long total=pageInfo.getTotal();
		this.totalSize=total.intValue();
		this.pageSize=pageInfo.getPageSize();
		this.currentPage=pageInfo.getPageNum();
		this.totalPage=pageInfo.getPages();
	}

	public int getTotal() {
		return this.totalSize;
	}

	public int getPageSize() {
		return this.pageSize;
	}
	
	public String getParameter(String paramName) {
		return paramMap.get(paramName);
	}

	private int calcTotalPage() {
		int t = getTotal();
		int p = getPageSize();
		if ((t == 0) || (p == 0))
			return 0;
		int r = t % p;
		int pages = (t - r) / p;
		if (r > 0)
			pages += 1;
		return pages;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

	public int getPageBegin() {
		return this.pageSize * (this.currentPage-1);
	}

	public int getPageEnd() {
		return getPageBegin() + getRows().size();
	}
	
	public int getPageLimit(){
		return this.getPageBegin()+this.getPageSize();
	}

	public Collection<T> getRows() {
		return this.datas;
	}

	@Deprecated
	public void setRows(Collection<T> rows) {
		this.datas = rows;
	}

//	public boolean add(T o) {
//		return this.datas.add(o);
//	}
//
	public boolean addAll(Collection<? extends T> c) {
		return this.datas.addAll(c);
	}

	public void clear() {
		this.datas.clear();
	}

	public boolean contains(Object o) {
		return this.datas.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return this.datas.containsAll(c);
	}



	public Iterator<T> iterator() {
		return this.datas.iterator();
	}

	public boolean remove(Object o) {
		return this.datas.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return this.datas.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return this.datas.retainAll(c);
	}

	public int size() {
		return this.datas.size();
	}

	public Object[] toArray() {
		return this.datas.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return this.datas.toArray(a);
	}

//	public Collection<T> getRows(){
//		return this.datas;
//	}

	public String getSort() {
		return sort;
	}
	/**
	 * field
	 * @param sort
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}
	/**
	 * asc/desc
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
