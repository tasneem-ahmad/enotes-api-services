package com.bitcodex.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bitcodex.dto.TodoDto;
import com.bitcodex.dto.TodoDto.StatusDto;
import com.bitcodex.entity.Todo;
import com.bitcodex.enums.TodoStatus;
import com.bitcodex.exception.ResourceNotFoundException;
import com.bitcodex.repository.TodoRepository;
import com.bitcodex.service.TodoService;
import com.bitcodex.util.Validation;

@Service
public class TodoServiceImpl implements TodoService{
	
	@Autowired
	private TodoRepository todoRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validation validation;

	@Override
	public Boolean saveTodo(TodoDto todoDto) throws ResourceNotFoundException {

		validation.todoValidation(todoDto);
		
		Todo todo = mapper.map(todoDto, Todo.class);
		todo.setStatusId(todoDto.getStatus().getId());
		Todo saveTodo = todoRepo.save(todo);
		if(!ObjectUtils.isEmpty(saveTodo)) {
			return true;
		}
		
		return false;
	}

	@Override
	public TodoDto getTodoById(Integer id) throws ResourceNotFoundException {

		Todo todo =  todoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found ! id invalid"));

		TodoDto todoDto = mapper.map(todo, TodoDto.class);
		setStatus(todoDto,todo);
		return todoDto;
	}


	private void setStatus(TodoDto todoDto, Todo todo) {

		for (TodoStatus st:TodoStatus.values()) {
			if(st.getId().equals(todo.getStatusId())) {
				StatusDto statusDto = new StatusDto().builder()
									  .id(st.getId())
									  .name(st.getName())
									  .build();
				
				todoDto.setStatus(statusDto);
			}
		}
	}

	@Override
	public List<TodoDto> geTodoByUser() {

		Integer userId = 2;
		List<Todo> todos = todoRepo.findByCreatedBy(userId);
		
		return todos.stream().map(td -> mapper.map(td, TodoDto.class)).toList();
	}
}
