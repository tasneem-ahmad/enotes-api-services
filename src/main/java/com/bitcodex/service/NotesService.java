package com.bitcodex.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bitcodex.dto.NotesDto;
import com.bitcodex.dto.NotesResponse;
import com.bitcodex.entity.FileDetails;
import com.bitcodex.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
	
	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FileDetails fileDtls)throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId,Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id) throws ResourceNotFoundException;

	public void restoreNotes(Integer id) throws ResourceNotFoundException;

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void hardDeleteNotes(Integer id) throws ResourceNotFoundException;

	public void emptyRecycleBin(int userId);
}
