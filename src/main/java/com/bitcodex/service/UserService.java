package com.bitcodex.service;

import com.bitcodex.controller.LoginRequest;
import com.bitcodex.dto.LoginResponse;
import com.bitcodex.dto.UserDto;

public interface UserService {
	
	public Boolean register(UserDto userDto,String url) throws Exception;

	public LoginResponse login(LoginRequest loginRequest);
}
