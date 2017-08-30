package com.babel.common.core.util;
//package com.babel.common.core.util;
//
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap; 
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONNull;
//import net.sf.json.JSONObject;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.util.StringUtils;
//
//public class JsonUtils
//{
//  protected static Log log = LogFactory.getLog(JsonUtils.class);
//
//  private static String jsonSpliter = "_____JSON_____";
//
//  public static Map<String, Object> jsonToMap(String json)
//  {
//    return jsonToMap(JSONObject.fromObject(json));
//  }
//
//
//  private static Map<String, Object> jsonToMap(JSONObject jsonObject)
//  {
//    Map map = new HashMap();
//    Iterator iterator = jsonObject.entrySet().iterator();
//    while (iterator.hasNext()) {
//      Map.Entry entry = (Map.Entry)iterator.next();
//      String property = (String)entry.getKey();
//      Object value = entry.getValue();
//      if ((value instanceof JSONObject))
//        map.put(property, jsonToMap((JSONObject)value));
//      else if ((value instanceof JSONArray))
//        map.put(property, jsonToList((JSONArray)value));
//      else if (JSONNull.getInstance() != value)
//        map.put(property, value);
//    }
//    return map;
//  }
//
//  private static List<Object> jsonToList(JSONArray jsonArray) {
//    int arraySize = jsonArray.size();
//    List list = new ArrayList(arraySize);
//    for (int i = 0; i < arraySize; i++) {
//      Object obj = jsonArray.get(i);
//      if ((obj instanceof JSONObject))
//        list.add(jsonToMap((JSONObject)obj));
//      else if ((obj instanceof JSONArray))
//        list.add(jsonToList((JSONArray)obj));
//      else
//        list.add(obj);
//    }
//    return list;
//  }
//
//  public static <T> List<T> jsonToList(String json, Class<T> clazz)
//    throws InstantiationException, IllegalAccessException, InvocationTargetException
//  {
//    List objects = new ArrayList();
//    if (json != null) {
//      JSONArray jsonArray = JSONArray.fromObject(json);
//      for (int i = 0; i < jsonArray.size(); i++) {
//        Object bean = clazz.newInstance();
//        JSONObject jsonObject = jsonArray.getJSONObject(i);
//        for (Iterator iterator = jsonObject.entrySet().iterator(); iterator.hasNext(); ) {
//          Map.Entry entry = (Map.Entry)iterator.next();
//          String property = (String)entry.getKey();
//          Object value = entry.getValue();
//          if ((property != null) && (property.length() > 0) && (value != null) && (value.toString().length() > 0) && (JSONNull.getInstance() != value)) {
//            setProperty(bean, property, value);
//          }
//        }
//        objects.add(bean);
//      }
//    }
//    return objects;
//  }
//
//  private static void setProperty(Object bean, String property, Object value) {
//    try {
//      if ((Date.class == findSetterClass(bean.getClass(), property)) && ((value instanceof String))) {
//        log.debug("pase date: " + value);
//        Date date = DateUtils.convert((String)value);
//        if (date == null)
//          return;
//        value = date;
//      }
//      org.apache.commons.beanutils.BeanUtils.setProperty(bean, property, value);
//    } catch (Exception e) {
//      log.error(e.getMessage(), e);
//    }
//  }
//
//  private static Class<?> findSetterClass(Class<?> clazz, String property) throws NoSuchMethodException {
//    String upper = property.substring(0, 1).toUpperCase() + property.substring(1);
//    String setterName = "set" + upper;
//    Method[] methods = clazz.getMethods();
//    for (Method method : methods) {
//      if ((setterName.equals(method.getName())) && (method.getParameterTypes().length == 1)) {
//        return method.getParameterTypes()[0];
//      }
//    }
//    throw new NoSuchMethodException("No such method \"" + setterName + "\" in class \"" + clazz.getCanonicalName() + "\"");
//  }
//
//  public static void jsonToPropList(Object obj, String slaveJson)
//    throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
//  {
//    String[] jsons = slaveJson.split(jsonSpliter);
//    for (String json : jsons) {
//      int index = json.indexOf("|");
//      String slaveClass = json.substring(0, index);
//      String jsonObject = json.substring(index + 1);
//      index = slaveClass.lastIndexOf(".") + 1;
//      String propName = StringUtils.uncapitalize(slaveClass.substring(index)) + "s";
//      Object value = jsonToList(jsonObject, Class.forName(slaveClass));
//      BeanUtils.setProperty(obj, propName, value);
//    }
//  }
//}
//
//
//
//
