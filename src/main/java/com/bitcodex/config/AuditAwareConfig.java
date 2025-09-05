package com.bitcodex.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditAwareConfig implements AuditorAware<Integer>{
	
	public Optional<Integer> getCurrentAuditor(){
		
		return Optional.of(2);
	}
}
