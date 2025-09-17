package com.bitcodex.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bitcodex.dto.UserDto;
import com.bitcodex.entity.Role;
import com.bitcodex.entity.User;
import com.bitcodex.repository.RoleRepository;
import com.bitcodex.repository.UserRepository;
import com.bitcodex.service.UserService;
import com.bitcodex.util.Validation;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository UserRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private Validation validation;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean register(UserDto userDto) {

		validation.userValidation(userDto);
		
		User user = mapper.map(userDto, User.class);
		setRole(userDto,user);
		User saveUser = UserRepo.save(user);
		if(!ObjectUtils.isEmpty(saveUser)) {
			return true;
		}
		
		return false;
	}

	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepo.findAllById(reqRoleId);
		user.setRoles(roles);
		
	}

}
