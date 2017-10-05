package com.babel.common.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.babel.common.core.util.FatherToChildUtils;
import com.babel.common.core.util.ObjectToMapUtil;



/**
 * java性能测试
 * @author Justin
 *
 */
public class PerfTest {

	@Test
	public void testPerf(){
		int RUN_COUNT=100000000;
		System.out.println("----runCount="+RUN_COUNT);
		long time=System.currentTimeMillis();
		
		Integer count=0;
		for(int i=0; i<RUN_COUNT; i++){
			this.getClass().getName();
			count++;
		}
		System.out.println("--time="+(System.currentTimeMillis()-time)+" count="+count);
		
		
		time=System.currentTimeMillis();
		count=0;
		for(int i=0; i<RUN_COUNT; i++){
			try {
				count++;
			} catch (Exception e) {
				System.out.println("----"+e.getMessage());
			}
		}
		System.out.println("--timeExp="+(System.currentTimeMillis()-time)+" count="+count);
		
		
		time=System.currentTimeMillis();
		count=0;
		for(int i=0; i<RUN_COUNT; i++){
			count++;
		}
		System.out.println("--time="+(System.currentTimeMillis()-time)+" count="+count);
		
		
		time=System.currentTimeMillis();
		count=0;
		for(int i=0; i<RUN_COUNT; i++){
			try {
				count++;
			} catch (Exception e) {
				System.out.println("----"+e.getMessage());
			}
		}
		System.out.println("--timeExp="+(System.currentTimeMillis()-time)+" count="+count);
	}
	
	@Test
	public void testPerf2(){
		
		int RUN_COUNT=100000000;
		System.out.println("----runCount="+RUN_COUNT);
		Integer count=0;
		long time=System.currentTimeMillis();
		long v=0;
		for(int i=0; i<RUN_COUNT; i++){
			count++;
			v=(System.currentTimeMillis()-time);
		}
		System.out.println("--time="+(System.currentTimeMillis()-time)+" count="+count);
		
		
		time=System.currentTimeMillis();
		count=0;
		for(int i=0; i<RUN_COUNT; i++){
			try {
				count++;
				v=(System.currentTimeMillis()-time);
			} catch (Exception e) {
				System.out.println("----"+e.getMessage());
			}
		}
		System.out.println("--timeExp="+(System.currentTimeMillis()-time)+" count="+count);
		
		
		time=System.currentTimeMillis();
		count=0;
		for(int i=0; i<RUN_COUNT; i++){
			count++;
			v=(System.currentTimeMillis()-time);
		}
		System.out.println("--time="+(System.currentTimeMillis()-time)+" count="+count);
		
		
		time=System.currentTimeMillis();
		count=0;
		for(int i=0; i<RUN_COUNT; i++){
			try {
				count++;
				v=(System.currentTimeMillis()-time);
			} catch (Exception e) {
				System.out.println("----"+e.getMessage());
			}
		}
		System.out.println("--timeExp="+(System.currentTimeMillis()-time)+" count="+count);
	}
	
	@Test
	public void testPerf3() throws Exception{
		List<RoleVO> list = getRoleList();
//		long time=System.currentTimeMillis();
//		for(int j=0; j<1000; j++){
//			for(int i=0; i<list.size(); i++){
//				JsonKit.dataToMap2(list.get(i));
//			}
//		}
//		System.out.println("----time="+(System.currentTimeMillis()-time));
		
		long time=System.currentTimeMillis();
		for(int j=0; j<1000; j++){
			for(int i=0; i<list.size(); i++){
				ObjectToMapUtil.objectToMap(list.get(i));
			}
		}
		System.out.println("----time="+(System.currentTimeMillis()-time));
		
		
	}

	private List<RoleVO> getRoleList() {
		List<RoleVO>list=new ArrayList<>(); 
		RoleVO roleVO=null;
		for(int i=0; i<10000; i++){
			roleVO=new RoleVO();
			roleVO.setId(2l+i);
			roleVO.setName("test2"+i);
			roleVO.setDescs(""+i);
			list.add(roleVO);
		}
		return list;
	}
	
	private static Map<String, List<RoleVO>> cacheMap=new ConcurrentHashMap<>();
//	@Test
	public void forTest() throws Exception{
		int total=0;
		for(int k=0; k<10000; k++){
//			cacheMap.clear();
			for(int i=0; i<10; i++){
				cacheMap.put(""+i, this.getRoleList());
			}
			total+=cacheMap.size()*cacheMap.get("0").size();
			System.out.println("-----total="+total);
			Thread.sleep(500l);
		}
		
	}
	
	@Test
	public void testClone() throws Exception{
		int total=0;
		long time=System.currentTimeMillis();
		for(int k=0; k<100; k++){
			List<RoleVO> list=this.getRoleList();
			int size=list.size();
			total+=size;
			List<RoleVO> tmpList=new ArrayList<>();
			RoleDTO tmp=null;
			for(int i=0; i<size; i++){
//				tmp=(RoleDTO)list.get(i).clone();
				tmp=new RoleDTO();
				FatherToChildUtils.fatherToChild(tmp, list.get(i));
//				FatherToChildUtils.parentToChild(tmp, list.get(i));
//				BeanUtils.copyProperties(tmp, list.get(i));
//				System.out.println(tmp.getName());
				tmpList.add(tmp);
			}
			
			System.out.println("-----count="+k+" size="+list.size()+" total="+total+" time="+(System.currentTimeMillis()-time));	
		}
	}
	
	@Test
	public void testGenerateChildClass() throws Exception{
		String classInfo=FatherToChildUtils.genreateChildClass("RoleDTO", RoleVO.class);
		System.out.println("----classInfo="+classInfo);
	}
}
