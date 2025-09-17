package com.bitcodex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitcodex.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
