package com.babel.common.core.thread;


import org.apache.log4j.Logger;

import com.babel.common.core.util.CommMethod;




/**
 * 
 * @author jview
 *
 */
public class FileWriteThread implements Runnable{
	public FileWriteThread(){
		
	}
	private static final Logger log4 = Logger.getLogger(FileWriteThread.class);
	private String path;
	private String dataJson;
	private String data;
	private boolean isAppand;
	public FileWriteThread(String path, String dataJson, String data, boolean isAppand){
		this.path=path;
		this.dataJson=dataJson;
		this.data=data;
		this.isAppand=isAppand;
	}
	
	public void run(){
		try {
//			System.out.println("-----thread--");
			CommMethod.writeFileTask(path, dataJson, data, isAppand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
