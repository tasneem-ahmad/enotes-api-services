package com.bitcodex.service;

import java.util.List;

import com.bitcodex.entity.Category;

public interface CategoryService {

	public Boolean saveCategory(Category category);
	
	public List<Category> getAllCategory();
}
