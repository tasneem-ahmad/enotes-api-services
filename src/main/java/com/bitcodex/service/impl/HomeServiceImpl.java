package com.bitcodex.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitcodex.entity.AccountStatus;
import com.bitcodex.entity.User;
import com.bitcodex.exception.ResourceNotFoundException;
import com.bitcodex.exception.SuccessException;
import com.bitcodex.repository.UserRepository;
import com.bitcodex.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService{
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {

		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("invalid user"));
		
		if(user.getStatus().getVerificationCode()==null) {
			throw new SuccessException("Account already verified");
		}
		
		if(user.getStatus().getVerificationCode().equals(verificationCode)) {
			AccountStatus status = user.getStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);
			
			userRepo.save(user);
			
			return true;
		}
		return null;
	}

}
