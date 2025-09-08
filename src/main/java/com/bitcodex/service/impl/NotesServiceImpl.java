package com.bitcodex.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.bitcodex.dto.NotesDto;
import com.bitcodex.dto.NotesDto.CategoryDto;
import com.bitcodex.entity.Notes;
import com.bitcodex.exception.ResourceNotFoundException;
import com.bitcodex.repository.CategoryRepository;
import com.bitcodex.repository.NotesRepository;
import com.bitcodex.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService{
	
	@Autowired
	private NotesRepository notesRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public Boolean saveNotes(NotesDto notesDto) throws Exception {
		
		//category validation
		checkCategoryExist(notesDto.getCategory());
		
		
		Notes notes = mapper.map(notesDto, Notes.class);
		
		Notes saveNotes = notesRepo.save(notes);
		
		if(!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		
		return false;
	}

	private void checkCategoryExist(CategoryDto category) throws Exception{

		categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category is invalid"));
		
	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepo.findAll().stream()
				.map(note -> mapper.map(note, NotesDto.class)).toList();
	}

}
