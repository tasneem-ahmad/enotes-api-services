package com.bitcodex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitcodex.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
