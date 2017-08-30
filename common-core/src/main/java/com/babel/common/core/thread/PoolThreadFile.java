package com.babel.common.core.thread;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author jview
 *
 */
public class PoolThreadFile {
	public PoolThreadFile(){
		
	}
	public PoolThreadFile(int corePoolSize, int maximumPoolSize, int keepAliveTime){
		if(corePoolSize>0){
			this.corePoolSize=corePoolSize;
		}
		if(maximumPoolSize>0){
			this.maximumPoolSize=maximumPoolSize;
		}
		if(keepAliveTime>0){
			this.keepAliveTime=keepAliveTime;
		}
	}
	private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	private ThreadPoolExecutor executor;
	
	public void addWrite(String path, String dataJson, String data, boolean isAppand){
		if(this.executor==null){
			this.executor= new ThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, this.keepAliveTime, this.timeUnit, queue);
		}
		Runnable worker=new FileWriteThread(path, dataJson, data, isAppand);
		this.executor.execute(worker);
//		System.out.println("---poolThreadFile-addWrite--");
	}
	
	
	private TimeUnit timeUnit=TimeUnit.HOURS;
	private int corePoolSize=10;
	private int maximumPoolSize=50;
	private long keepAliveTime=1;
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}
	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
	
	
}
