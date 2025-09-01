package com.bitcodex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitcodex.dto.CategoryDto;
import com.bitcodex.dto.CategoryResponse;
import com.bitcodex.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;

	@PostMapping("/save")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto category){
		Boolean saveCategory = categoryService.saveCategory(category);
		
		if(saveCategory) {
			return new ResponseEntity<>("saved success",HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>("not saved",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllCategory(){
//		String m = null;
//		m.toUpperCase();
		List<CategoryDto> allCategory = categoryService.getAllCategory();
		
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}
		else {
			return new ResponseEntity<>(allCategory,HttpStatus.OK);
		}
	}
	
	@GetMapping("/active")
	public ResponseEntity<?> getACtiveCategory(){
		List<CategoryResponse> allCategory = categoryService.getActiveCategory();
		
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}
		else {
			return new ResponseEntity<>(allCategory,HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception{

		CategoryDto categoryDto = categoryService.getCategoryById(id);
		if(ObjectUtils.isEmpty(categoryDto)) {
			return new ResponseEntity<>("Internal Server Error",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(categoryDto,HttpStatus.OK);
	
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryDetailsById(@PathVariable Integer id){
		
		Boolean deleted = categoryService.deleteCategory(id);
		if(deleted) {
			return new ResponseEntity<>("Category deleted successfully="+id,HttpStatus.OK);
		}
		return new ResponseEntity<>("Category not deleted",HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
