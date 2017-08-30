package com.babel.common.core.service.impl;

import java.util.List;

import com.babel.common.core.entity.BaseEntity;
import com.babel.common.core.entity.BaseEntitySimple;
import com.babel.common.core.entity.BaseEntityTimestamp;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.common.MapperMy;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by liuzh on 2014/12/11.
 */
public abstract class BaseService<T> implements IBaseService<T> {

    public abstract MapperMy<T> getMapper();

    @Override
    public T selectByKey(Object key) {
        return getMapper().selectByPrimaryKey(key);
    }

    public int save(T entity) {
    	if(entity instanceof BaseEntitySimple){
    		BaseEntitySimple dataEntity=(BaseEntitySimple)entity;
    		if(dataEntity.getCid()==null || dataEntity.getCid().longValue()==0){
    			dataEntity.setCid(this.getMapper().selectSeqId(entity));
    			return getMapper().insert(entity);
    		}
    		else{
    			return this.updateAll(entity);
    		}
    	}
    	else{
    		//这个保存后entity中没有cid的值
    		return getMapper().insert(entity);
    	}
    }
    
    public int saveBatch(List<T> list) {
    	int count=0;
    	int status=0;
//    	for(T t:list){
//    		status=getMapper().insert(t);
//    	}
    	status=getMapper().insertList(list);
        return status;
    }
    

    public int delete(Object key) {
        return getMapper().deleteByPrimaryKey(key);
    }

    public int updateAll(T entity) {
        return getMapper().updateByPrimaryKey(entity);
    }

    public int updateNotNull(T entity) {
        return getMapper().updateByPrimaryKeySelective(entity);
    }
    
    public int updateAllBatch(List<T> list){
//    	int count=0;
//    	int status=0;
//    	for(T entity:list){
//    		status=this.updateAll(entity);
//    	}
    	return this.getMapper().updateListById(list);
//    	return status;
    }

    public int updateNotNullBatch(List<T> list){
//    	int status=0;
//    	for(T entity:list){
//    		status=this.updateNotNull(entity);
//    	}
    	return this.getMapper().updateListByIdSelective(list);
//    	return count;
    }

    public List<T> selectByExample(Object example) {
        return getMapper().selectByExample(example);
    }
    
    /**
     * add by caco
     * @param example
     * @return
     */
    protected PageVO<T> selectPageByExample(Example example, PageVO<T> page) {
    	PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<T> list = selectByExample(example);
		PageInfo<T> pageInfo = new PageInfo<T>(list);
		PageVO<T> pageRet = new PageVO<T>(pageInfo);
		return pageRet;
    }
    
    public Integer deleteVirtual(T entity) throws Exception{
    	if(entity instanceof BaseEntitySimple||entity instanceof BaseEntityTimestamp){
    		BaseEntitySimple dataEntity=(BaseEntitySimple)entity;
    		if(dataEntity.getCid()==null||dataEntity.getCid().intValue()==0){
    			throw new Exception("PrimaryKey cid is empty");
    		}
    		dataEntity.setIfDel(1);
    		dataEntity.initUpdate();
    		return this.getMapper().updateByPrimaryKeySelective(entity);
    	}
    	else{
    		throw new Exception("Invalid entity");
    	}
    }

    //TODO 其他...
}
