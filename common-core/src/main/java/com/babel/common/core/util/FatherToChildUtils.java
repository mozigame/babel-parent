package com.babel.common.core.util;

import java.lang.reflect.Field;
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
    
	public static void parentToChild(Object child, Object parent) throws Exception{
		Method[] methods = parent.getClass().getMethods();// 得到父类所有方法
		Method method=null;
		String name=null;
		for (int i=0; i<methods.length; i++) {// 遍历父类方法
			method=methods[i];
			name=method.getName();
			if (name.startsWith("get") && !name.equals("getClass")) {// 得到父类的get方法
				Object value = method.invoke(parent);// 通过get方法得到父类的值
				// 尝试得到子类的set方法 method.getReturnType()：即得到返回类
				Method zi_method = child.getClass().getMethod(method.getName().replaceFirst("get", "set"),
						method.getReturnType());
				zi_method.invoke(child, value);// 将父类的属性注入到子类里面去
			}
		}
	}
	
	/**
	 * 生成子类
	 * @param childClassName
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public static String genreateChildClass(String childClassName, Class parent) throws Exception{
		Method[] methods = parent.getMethods();// 得到父类所有方法
		Method method=null;
		String name=null;
		String classInfo="\n public class "+childClassName+" extends "+parent.getSimpleName()+"{\n";
		classInfo+="	public "+childClassName+"("+parent.getSimpleName()+" data){\n";
		for (int i=0; i<methods.length; i++) {// 遍历父类方法
			method=methods[i];
			name=method.getName();
			if (name.startsWith("get") && !name.equals("getClass")) {// 得到父类的get方法
				classInfo+="		this."+name.replaceFirst("get", "set")+"(data."+name+"());\n";
			}
		}
		classInfo+="	}\n";
		classInfo+="}\n";
		return classInfo;
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