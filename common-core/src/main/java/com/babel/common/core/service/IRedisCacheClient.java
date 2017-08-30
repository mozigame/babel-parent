package com.babel.common.core.service;

public interface IRedisCacheClient {
	public void listLpush(String key, String value);
	public String listLpop(String key);
}
