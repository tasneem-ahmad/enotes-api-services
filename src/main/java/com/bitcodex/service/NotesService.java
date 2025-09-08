package com.bitcodex.service;

import java.util.List;

import com.bitcodex.dto.NotesDto;

public interface NotesService {

	public Boolean saveNotes(NotesDto notesDto) throws Exception;
	
	public List<NotesDto> getAllNotes();
}
