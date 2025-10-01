package com.bitcodex.service;

import com.bitcodex.controller.LoginRequest;
import com.bitcodex.dto.LoginResponse;
import com.bitcodex.dto.UserRequest;

public interface UserService {
	
	public Boolean register(UserRequest userDto,String url) throws Exception;

	public LoginResponse login(LoginRequest loginRequest);
}
