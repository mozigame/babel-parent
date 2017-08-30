package com.babel.common.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class FileUtils {
	private static final Logger log4 = Logger.getLogger(FileUtils.class);
	private static final int MAX_TOTAL_ROW_READ=10000;
	
	public static List<String> readLineFile(File file, int maxCount, Map<String, String> filterMap, String charset) throws Exception {
//		long time=System.currentTimeMillis();
		
		List<String> lineList = new ArrayList<String>();
		if(charset==null ||"".equals(charset)){
			charset = "UTF-8";
		}
		
		int count = 0;
		int totalCount=0;
		
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(file, "r");
			long len = rf.length();
			long start = rf.getFilePointer();
			long nextend = start + len - 1;
			String line;
			rf.seek(nextend);
			int c = -1;
			String filter=filterMap.get("filter");
			String stop_key=filterMap.get("stop_key");
			String hide_key=filterMap.get("hide_key");
			if(stop_key!=null && (stop_key.equals("") ||stop_key.equals("null"))){
				stop_key=null;
			}
			if(hide_key!=null && (hide_key.equals("") ||hide_key.equals("null"))){
				hide_key=null;
			}
			String[] hides=null;
			if(hide_key!=null){
				hides=hide_key.split(",");
			}
			
			
			boolean isHide=false;
			while (nextend > start) {
				c = rf.read();
				if (c == '\n' || c == '\r') {
					totalCount++;
					if(totalCount>MAX_TOTAL_ROW_READ){
						break;
					}
					if (count > maxCount) {
						break;
					}
					line = rf.readLine();
					if (line != null) {
						line = new String(line.getBytes("ISO-8859-1"), charset);
						//stop
						if(stop_key!=null && line.indexOf(stop_key)>=0){
							break;
						}
						//hide
						if(hides!=null){
							isHide=false;
							for(String hide:hides){
								if(hide.equals("hide_empty") && "".equals(line)){
									isHide=true;
									break;
								}
								else if(line.indexOf(hide)>=0){
									isHide=true;
									break;
								}
							}
							if(isHide){
								continue;
							}
						}
						//filter
						if(filter==null|| line.indexOf(filter)>=0){
							count++;
							lineList.add(line);
						}
					}
					nextend--;
				}
				nextend--;
				rf.seek(nextend);
				if (nextend == 0) {// output first line on seek file head
					line = rf.readLine();
					line = new String(line.getBytes("ISO-8859-1"), charset);
					lineList.add(line);
				}
			}
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			lineList.add(e.getMessage());
			log4.error("", e);
		} catch (IOException e) {
//			e.printStackTrace();
			lineList.add(e.getMessage());
		} finally {
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
//				e.printStackTrace();
				lineList.add(e.getMessage());
			}
		}
//		System.out.println("totalCount="+totalCount+" foundCount="+count+" time="+(System.currentTimeMillis()-time));
		return lineList;
	}
}
