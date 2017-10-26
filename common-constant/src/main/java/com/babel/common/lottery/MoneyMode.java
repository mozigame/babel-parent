package com.babel.common.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * 钱款单位类型
 * @author Justin
 *
 */
public enum MoneyMode {
    YUAN('y', "元"),
    JIAO('j', "角"),
    FEN('f', "分");


    private static Map<String, MoneyMode> map = new HashMap<>();

    static {
        for (MoneyMode oj : MoneyMode.values()) {
            map.put(""+oj.code, oj);
        }
    }

    private Character code;
    private String mes;

    MoneyMode(char code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public Character code() {
        return code;
    }

    public String mes() {
        return mes;
    }

    public static MoneyMode parse(String code) {
        return map.get(code);
    }
    
	/**
	 * 元角分转换
	 * @param moneyMode
	 * @return
	 */
	public static int getMoneyValue(String moneyMode){
		int v=0;
		if((""+MoneyMode.YUAN.code()).equals(moneyMode)){
			v=100;
		}
		else if((""+MoneyMode.JIAO.code()).equals(moneyMode)){
			v=10;
		}
		else if((""+MoneyMode.FEN.code()).equals(moneyMode)){
			v=1;
		}
		return v;
	}

}
