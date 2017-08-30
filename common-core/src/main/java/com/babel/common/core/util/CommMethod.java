package com.babel.common.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.babel.common.core.thread.PoolThreadFile;

/**
 * 
 * @author jview
 *
 */
public class CommMethod {
	private static final Logger log4 = Logger.getLogger(CommMethod.class);
	private static PoolThreadFile poolFile=new PoolThreadFile();
	public static Date parseDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Long getLongValue(Object object){
		if(object!=null && object instanceof Long) {
			return (Long)object;
		}
		return getLongValue(""+object);
	}
	public static Long getLongValue(String str){
		Long value = 0l;
		if(null!=str && !"".equals(str) && !"null".equals(str)){
			try{
				value = Long.parseLong(str);
			}
			catch(Exception e){
//				e.printStackTrace();
			}
		}
		return value;
	}
	
	public static Integer getIntegerValue(Object object){
		if(object!=null && object instanceof Integer) {
			return (Integer)object;
		}
		return getIntegerValue(""+object);
	}
	public static Integer getIntegerValue(String str){
		Integer value = 0;
		if(null!=str && !"".equals(str) && !"null".equals(str)){
			try{
				value = Integer.parseInt(str);
			}
			catch(Exception e){
//				e.printStackTrace();
			}
		}
		return value;
	}
	
	public static Double getDoubleValue(Object object){
		if(object!=null && object instanceof Double) {
			return (Double)object;
		}
		return getDoubleValue(""+object);
	}
	public static Double getDoubleValue(String str){
		Double value = 0d;
		if(null!=str && !"".equals(str) && !"null".equals(str)){
			try{
				value = Double.valueOf(str);
			}
			catch(Exception e){
//				e.printStackTrace();
			}
		}
		return value;
	}

	public static String formatDate(Date date, String expstr, String timezone) {
		if (date == null) {
			return "";
		}
		if (expstr==null || expstr.equals("") || expstr.equals("null")) {
			expstr = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(expstr);
		if (timezone != null) {
			sdf.setTimeZone(TimeZone.getTimeZone(timezone));
		}

		return sdf.format(date);

	}

	public static String formatDate(Date date, String expstr) {
		return formatDate(date, expstr, null);
	}

	public static String getClientIP(HttpServletRequest request) {
		String fromType = "X-Real-IP";
		String ip = request.getHeader(fromType);

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromType = "X-Forwarded-For";
			ip = request.getHeader(fromType);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromType = "Proxy-Client-IP";
			ip = request.getHeader(fromType);
			;
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromType = "WL-Proxy-Client-IP";
			ip = request.getHeader(fromType);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromType = "request.getRemoteAddr";
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String readFile(String path) {

		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			System.out.println("------path=" + file.getAbsolutePath()+"　unexist!");
			return "error";
		}
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "UTF-8"));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				// System.out.println("line " + line + ": " + tempString);
				sb.append(tempString + "\n");
				line++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e1) {

				}
			}
		}
		return sb.toString();
	}

	public static void writeFile(String path, String dataJson, String data,
			boolean isAppand) {
		// String
		// paths=baseRealPath;//Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
		boolean isSupportTheardPool=true;
		if(isSupportTheardPool){
			poolFile.addWrite(path, dataJson, data, isAppand);
			return;
		}
		CommMethod.writeFileTask(path, dataJson, data, isAppand);
	}
	
	public static void writeFileTask(String path, String dataJson, String data,
			boolean isAppand) {
		// String
		// paths=baseRealPath;//Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1);
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			//System.out.print("-----writeFileTask--path=" + file.getAbsolutePath());

			FileWriter fw = new FileWriter(file, isAppand);
			BufferedWriter bw = new BufferedWriter(fw);
			if (dataJson != null) {
				bw.append(dataJson+"\r\n");
			}
			if (data!=null){
				bw.append(data+"\r\n");
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// json字符串转Map
	public static Map transJSONStrToMap(String mapStr, String split,
			String split2) {
		Map map = new HashMap();
		if (mapStr == null || "".equals(mapStr)) {
			return map;
		}
		String[] arrays = mapStr.split(split);
		if (arrays != null && arrays.length > 0) {
			String key=null, value=null;
			for (int i = 0; i < arrays.length; i++) {
				String[] arraysplit = arrays[i].split(split2);
				if (arraysplit != null && arraysplit.length > 1) {
					key=arraysplit[0];
					value=arraysplit[1].trim();
					map.put(key, value);
				}
			}
		}
		return map;
	}

	public static int lottery(List<Double> orignalRates) {
		if (orignalRates == null || orignalRates.isEmpty()) {
			return -1;
		}

		int size = orignalRates.size();

		// 计算总概率，这样可以保证不一定总概率是1
		double sumRate = 0d;
		for (double rate : orignalRates) {
			sumRate += rate;
		}

		// 计算每个物品在总概率的基础下的概率情况
		List<Double> sortOrignalRates = new ArrayList<Double>(size);
		Double tempSumRate = 0d;
		for (double rate : orignalRates) {
			tempSumRate += rate;
			sortOrignalRates.add(tempSumRate / sumRate);
		}
		// 根据区块值来获取抽取到的物品索引
		double nextDouble = Math.random();
		sortOrignalRates.add(nextDouble);
		Collections.sort(sortOrignalRates);

		return sortOrignalRates.indexOf(nextDouble);
	}
	
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);
		return m.matches();

	}
	
	/**
	  * 获取类的class文件位置的URL。这个方法是本类最基础的方法，供其它方法调用。
	  */
	private static URL getClassLocationURL(final Class cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(
				".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		// java.lang.Class contract does not specify
		// if 'pd' can ever be null;
		// it is not the case for Sun's implementations,
		// but guard against null
		// just in case:
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			// 'cs' can be null depending on
			// the classloader behavior:
			if (cs != null)
				result = cs.getLocation();

			if (result != null) {
				// Convert a code source location into
				// a full class file location
				// for some common cases:
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar")
								|| result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:".concat(
									result.toExternalForm()).concat("!/")
									.concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory())
							result = new URL(result, clsAsResource);
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}

		if (result == null) {
			// Try to find 'cls' definition as a resource;
			// this is not
			// document��d to be legal, but Sun's
			// implementations seem to //allow this:
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource)
					: ClassLoader.getSystemResource(clsAsResource);
		}
		return result;
	}
	
	/**
	  * 获取一个类的class文件所在的绝对路径。 这个类可以是JDK自身的类，也可以是用户自定义的类，或者是第三方开发包里的类。
	  * 只要是在本程序中可以被加载的类，都可以定位到它的class文件的绝对路径。
	  * 
	  * @param cls
	  *            一个对象的Class属性
	  * @return 这个类的class文件位置的绝对路径。 如果没有这个类的定义，则返回null。
	  */
	public static String getPathFromClass(Class cls) throws IOException {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			path = file.getCanonicalPath();
		}
		return path;
	}
	
	  public static String getWebPath()
	    {
	        String paths = null;
	        String tempPath = null;
	        try
	        {
	            tempPath = getPathFromClass(CommMethod.class);
	            if(tempPath.indexOf("WEB-INF") > 0)
	                paths = tempPath.substring(0, tempPath.indexOf("WEB-INF"));
	        }
	        catch(Exception e)
	        {
//	            e.printStackTrace();
//	        	log4.error("-------getWebPath=", e);
	        }
	        return paths;
	    }
	
	/**
	 * 按key查找报文内容
	 * @param esbXml
	 * @param key
	 * @return
	 */
	public static String findKeyValue(String esbXml, String key){
		String infor=null;
		if(key==null){
			return infor;
		}
		String start="<"+key+">";
		String end="</"+key+">";
		int i_start=esbXml.indexOf(start);
		int i_end=esbXml.indexOf(end);
		if(i_start>=0){
			infor=esbXml.substring(i_start, i_end+end.length());
			infor=infor.substring(start.length(), infor.length()-end.length());
		}
		else{
			return null;
		}
		return infor;
	}
	
	/**
     * 判断数据是否为空
     * @param inputStr
     * @return
     */
    public static boolean isEmpty(String inputStr) {
    	
        if (null == inputStr || inputStr.trim().equals("")||inputStr.equals("null")) {
            return true;
        }
        return false;
    }
    
    private static int PRINT_LIMIT=50;
    public static void print(Collection list){
    	String info="---CommMethod.print(list):"+list.size();
    	
    	if(list.size()>0){
    		Object object=null;
    		for(Object obj:list){
    			object=obj;
    			break;
    		}
    		info+=" "+object.getClass();
    	}
    	System.out.println(info);
    	int count=0;
    	for(Object obj: list){
    		if(count>PRINT_LIMIT){
    			System.out.println("---stop print:out limit");
    			break;
    		}
    		System.out.println(count+" "+obj);
    		count++;
    	}
    }
    
    public static void print(Object[] arrays){
    	String info="---CommMethod.print(array):"+arrays.length;
    	if(arrays.length>0){
    		info+=" "+arrays[0].getClass();
    	}
    	System.out.println(info);
    	int count=0;
    	for(Object obj: arrays){
    		if(count>PRINT_LIMIT){
    			System.out.println("---stop print:out limit");
    			break;
    		}
    		System.out.println(count+" "+obj);
    		count++;
    	}
    }
    
    public static void print(Set set){
    	String info="---CommMethod.print(set):"+set.size();
    	if(set.size()>0){
    		Object object=null;
    		for(Object obj:set){
    			object=obj;
    			break;
    		}
    		info+=" "+object.getClass();
    	}
    	System.out.println(info);
    	int count=0;
    	for(Object obj: set){
    		if(count>PRINT_LIMIT){
    			System.out.println("---stop print:out limit");
    			break;
    		}
    		System.out.println(count+" "+obj);
    		count++;
    	}
    }
    
    public static void print(Enumeration en){
    	System.out.println("---CommMethod.print(enum)---");
    	while(en.hasMoreElements()){
    		System.out.println(en.nextElement());
    	}
    }
}
