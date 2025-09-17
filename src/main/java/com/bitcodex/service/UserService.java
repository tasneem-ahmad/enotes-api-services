package com.bitcodex.service;

import com.bitcodex.dto.UserDto;

public interface UserService {
	
	public Boolean register(UserDto userDto) throws Exception;
}
