package com.babel.common.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FatherToChildUtils {
    /* 
    * 将父类所有的属性COPY到子类中。 
    * 类定义中child一定要extends father； 
    * 而且child和father一定为严格javabean写法，属性为deleteDate，方法为getDeleteDate 
    */  
    public static void fatherToChild (Object child, Object father) throws Exception{  
        if(!(child.getClass().getSuperclass()==father.getClass())){  
            System.err.println("child不是father的子类"); 
        }  
        Class fatherClass= father.getClass();
        Field ff[]= fatherClass.getDeclaredFields();
        Method[] methods=fatherClass.getMethods();
        String upperHeaderStr=null;
        Method method=null;
        String setMethodName=null;
        for(int i=0;i<ff.length;i++){  
            Field f=ff[i];//取出每一个属性，如deleteDate  
        	upperHeaderStr=upperHeadChar(f.getName());
            Method m = fatherClass.getMethod("get"+upperHeaderStr);//方法getDeleteDate  
            Object obj=m.invoke(father);//取出属性值 
            
            for(int j=0; j<methods.length; j++){
            	method=methods[j];
            	setMethodName="set"+upperHeaderStr;
            	if(method.getName().equals(setMethodName)){
            		method.invoke(child, obj);
            		break;
            	}
            }
        }  
    }  
    /** 
    * 首字母大写，in:deleteDate，out:DeleteDate 
    */  
    private static String upperHeadChar(String in){  
        String head=in.substring(0,1);  
        String out=head.toUpperCase()+in.substring(1,in.length());  
        return out;  
    } 

}