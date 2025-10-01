package com.bitcodex.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.bitcodex.entity.User;
import com.bitcodex.util.CommonUtil;

public class AuditAwareConfig implements AuditorAware<Integer>{
	
	public Optional<Integer> getCurrentAuditor(){
		
		User loggedInUser = CommonUtil.getLoggedInUser();
		
		return Optional.of(loggedInUser.getId());
	}
}
