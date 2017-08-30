package com.babel.common.web.controller;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.babel.common.core.util.DateUtils;



public class DateEditor extends PropertyEditorSupport {
	private static Logger logger = Logger.getLogger(DateEditor.class);
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			if(!StringUtils.isEmpty(text))
					date = DateUtils.parse(text, DateUtils.SDF_FORMAT_DATETIME);
		} catch (ParseException e) {
//			format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = DateUtils.parse(text, DateUtils.SDF_FORMAT_DATE);
			} catch (ParseException e1) {
//				e1.printStackTrace();
				logger.warn("--invalid date format:"+text+", exp:"+e1.getMessage());
			}
		}
		setValue(date);
	}
}