package com.babel.common.core.service;

import java.util.List;

import com.babel.common.core.data.IUser;
import com.babel.common.core.data.RetResult;

public interface IUserInfoService {
	public RetResult<IUser> findUserByIdList(List<Long> idList);
}
