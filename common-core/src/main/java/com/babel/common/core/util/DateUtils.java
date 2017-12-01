 package com.babel.common.core.util;
 
 import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
 public class DateUtils
 {
   private static Log log = LogFactory.getLog(DateUtils.class);
 
   private static final String[] FORMATS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "HH:mm", "HH:mm:ss", "yyyy-MM" };
 
   public static Date convert(String str)
   {
     if ((str != null) && (str.length() > 0)) {
       if ((str.length() > 10) && (str.charAt(10) == 'T'))
         str = str.replace('T', ' ');
       for (String format : FORMATS) {
         if (str.length() == format.length()) {
           try {
             log.debug("convert " + str + " to date format " + format);
 
             Date date = new SimpleDateFormat(format).parse(str);
             log.debug("****" + date + "****");
             return date;
           } catch (ParseException e) {
             log.warn(e.getMessage());
           }
         }
       }
     }
     return null;
   }
 
   public static String UDateToString(Date udate, String format)
   {
     String sdate = null;
     try {
       sdate = new SimpleDateFormat(format).format(udate);
     } catch (Exception e) {
       log.error("Can not convert [java.util.date] to [java.lang.String]. [FORMAT]:" + format, e);
     }
 
     return sdate;
   }
   
   public static int getReduce(Date date1, Date date2, int type){
	   if(date1==null||date2==null){
		   return 0;
	   }
	   int value=0;
	   long ss=date1.getTime()-date2.getTime();
	   ss=ss/1000;
	   if(Calendar.HOUR==type){
		   value=(int)ss/3600;
	   }
	   else if(Calendar.MINUTE==type){
		   value=(int)ss/60;
	   }
	   else if(Calendar.SECOND==type){
		   value=(int)ss/1;
	   }
	   else if(Calendar.DATE==type){
		   value=(int)ss/3600/24;
	   }
	   return value;
   }
   
   /** 锁对象 */
   private static final Object lockObj = new Object();

   /** 存放不同的日期模板格式的sdf的Map */
   private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

   /**
    * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
    * 
    * @param pattern
    * @return
    */
   private static SimpleDateFormat getSdf(String pattern) {
	   if(pattern==null){
		   pattern=SDF_FORMAT_DATETIME;
	   }
	   final String pattern_str=pattern;
       ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern_str);

       // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
       if (tl == null) {
           synchronized (lockObj) {
               tl = sdfMap.get(pattern_str);
               if (tl == null) {
                   // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
//                   System.out.println("put new sdf of pattern " + pattern + " to map");

                   // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                   tl = new ThreadLocal<SimpleDateFormat>() {

                       @Override
                       protected SimpleDateFormat initialValue() {
                           log.debug("thread: " + Thread.currentThread() + " init pattern: " + pattern_str);
                           return new SimpleDateFormat(pattern_str);
                       }
                   };
                   sdfMap.put(pattern_str, tl);
               }
           }
       }

       return tl.get();
   }
   
   public static final String SDF_FORMAT_DATE="yyyy-MM-dd";
   public static final String SDF_FORMAT_DATETIME="yyyy-MM-dd HH:mm:ss"; 

   /**
    * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
    * 
    * @param date
    * @param pattern
    * @return
    */
   public static String format(Date date, String pattern) {
       return getSdf(pattern).format(date);
   }

   public static Date parse(String dateStr, String pattern) throws ParseException {
       return getSdf(pattern).parse(dateStr);
   }
   
   /**
    * 获取日期数值
    * @param date
    * @return
    */
   public static Long getPdate(Date date){
	   return Long.parseLong(DateUtils.format(date, "yyyyMMdd"));
   }

	public static Long addPDays(Long pdate, Integer day) {
		try {
			if(day==0){
				return pdate;
			}
			Date date = parse("" + pdate, "yyyyMMdd");
			date=org.apache.commons.lang.time.DateUtils.addDays(date, day);
			return getPdate(date);
		} catch (ParseException e) {
//			e.printStackTrace();
			log.error("----pdateAdd--pdate="+pdate+" day="+day, e);
		}
		return 0l;

	}
 }


 
 
