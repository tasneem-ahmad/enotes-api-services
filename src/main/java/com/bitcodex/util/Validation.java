package com.bitcodex.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.bitcodex.dto.CategoryDto;
import com.bitcodex.dto.TodoDto;
import com.bitcodex.dto.TodoDto.StatusDto;
import com.bitcodex.dto.UserRequest;
import com.bitcodex.enums.TodoStatus;
import com.bitcodex.exception.ExistDataException;
import com.bitcodex.exception.ResourceNotFoundException;
import com.bitcodex.exception.ValidationException;
import com.bitcodex.repository.RoleRepository;
import com.bitcodex.repository.UserRepository;

@Component
public class Validation {
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public void categoryValidation(CategoryDto categoryDto) {
		
		Map<String, Object> error = new LinkedHashMap<>();
		
		if(ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("category object/JSON shouldn't be null or empty");
		}
		else {
			
			//validation name field
			if(ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name","name field is empty or null");
			}else {
				if(categoryDto.getName().length()<3) {
					error.put("name","name length min 3");
				}
				if(categoryDto.getName().length()>100){
					error.put("name","name length max 100");
				}
			}
			
			//validation description
			if(ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description","description field is empty or null");
			}
			
			//validation isActive
			if(ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive","isActive field is empty or null");
			}else {
				if(categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive() != Boolean.FALSE.booleanValue()) {
					error.put("isActive","inavlid value isActive field");
				}
			}
		}
		
		if(!error.isEmpty()) {
			throw new ValidationException(error);
		}
	}
	
	public void todoValidation(TodoDto todo) throws ResourceNotFoundException {
		
		StatusDto reqStatus = todo.getStatus();
		Boolean statusFound = false;
		
		for(TodoStatus st:TodoStatus.values()) {
			if(st.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		
		if(!statusFound) {
			throw new ResourceNotFoundException("Invalid status");
		}
	}
	
	public void userValidation(UserRequest userDto) {
		
		if(!StringUtils.hasText(userDto.getFirstName())) {
			throw new IllegalArgumentException("First Name is invalid!");
		}
		
		if(!StringUtils.hasText(userDto.getLastName())) {
			throw new IllegalArgumentException("Last Name is invalid!");
		}
		
		if(!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
			throw new IllegalArgumentException("Email is invalid!");
		}else {
			Boolean existEmail = userRepo.existsByEmail(userDto.getEmail());
			if(existEmail) {
				throw new ExistDataException("Email id already exist!");
			}
		}
		
		if(!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.MOBILE_REGEX)) {
			throw new IllegalArgumentException("Mobile Number is invalid!");
		}
		
		if(CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("Role is Invalid!");
		}
		else {
			List<Integer> rolesIds = roleRepo.findAll().stream().map(r -> r.getId()).toList();
			
			List<Integer> invalidReqRoleIds = userDto.getRoles().stream().map(r -> r.getId()).filter(roleId -> !rolesIds.contains(roleId)).toList();
			if(!CollectionUtils.isEmpty(invalidReqRoleIds)) {
				throw new IllegalArgumentException("Role is invalid!" + invalidReqRoleIds);
			}
		}
	}
}
