package com.bitcodex.service;

import java.util.List;

import com.bitcodex.dto.CategoryDto;
import com.bitcodex.dto.CategoryResponse;

public interface CategoryService {

	public Boolean saveCategory(CategoryDto category);
	
	public List<CategoryDto> getAllCategory();

	public List<CategoryResponse> getActiveCategory();

	public CategoryDto getCategoryById(Integer id) throws Exception;

	public Boolean deleteCategory(Integer id);
}
