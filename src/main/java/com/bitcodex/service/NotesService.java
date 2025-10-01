package com.bitcodex.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bitcodex.dto.FavouriteNoteDto;
import com.bitcodex.dto.NotesDto;
import com.bitcodex.dto.NotesResponse;
import com.bitcodex.entity.FileDetails;
import com.bitcodex.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
	
	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FileDetails fileDtls)throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id) throws ResourceNotFoundException;

	public void restoreNotes(Integer id) throws ResourceNotFoundException;

	public List<NotesDto> getUserRecycleBinNotes();

	public void hardDeleteNotes(Integer id) throws ResourceNotFoundException;

	public void emptyRecycleBin();
	
	public void favouriteNotes(Integer noteId) throws ResourceNotFoundException;
	
	public void unFavouriteNotes(Integer noteId) throws ResourceNotFoundException;
	
	public List<FavouriteNoteDto> getUserFavouriteNotes() throws ResourceNotFoundException;

	public Boolean copyNotes(Integer id) throws ResourceNotFoundException;
}
