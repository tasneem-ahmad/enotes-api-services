package com.bitcodex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitcodex.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findByIsActiveTrueAndIsDeletedFalse();

	Optional<Category> findByIdAndIsDeletedFalse(Integer id);

	List<Category> findByIsDeletedFalse();

}
