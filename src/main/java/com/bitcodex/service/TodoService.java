package com.bitcodex.service;

import java.util.List;

import com.bitcodex.dto.TodoDto;
import com.bitcodex.exception.ResourceNotFoundException;

public interface TodoService {

	public Boolean saveTodo(TodoDto todo) throws ResourceNotFoundException;
	
	public TodoDto getTodoById(Integer id) throws ResourceNotFoundException;
	
	public List<TodoDto> geTodoByUser();
}
