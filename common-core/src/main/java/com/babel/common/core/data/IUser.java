package com.babel.common.core.data;

public interface IUser {
	public Long getCid();

	public abstract String getUserName();

	public abstract String getPasswd();

	public abstract Integer getStatus();

	public abstract String getName();

	public abstract String getEmail();
}
