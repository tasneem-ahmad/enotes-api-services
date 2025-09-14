package com.bitcodex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bitcodex.dto.FavouriteNoteDto;
import com.bitcodex.dto.NotesDto;
import com.bitcodex.dto.NotesResponse;
import com.bitcodex.entity.FileDetails;
import com.bitcodex.exception.ResourceNotFoundException;
import com.bitcodex.service.NotesService;
import com.bitcodex.util.CommonUtil;

@RestController
@RequestMapping("api/v1/notes")
public class NotesController {
	
	@Autowired
	private NotesService notesService;
	
	@PostMapping("/")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception{
		Boolean saveNotes = notesService.saveNotes(notes, file);
		if(saveNotes) {
			return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception{
		FileDetails fileDetails = notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDetails);
		
		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
		
		return ResponseEntity.ok().headers(headers).body(data);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllNotes(){
		List<NotesDto> notes = notesService.getAllNotes();
		
		if(CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(
			@RequestParam(name = "pageNo",defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
		
		Integer userId = 2;
		
		NotesResponse notes = notesService.getAllNotesByUser(userId,pageNo,pageSize);
		
//		if(CollectionUtils.isEmpty(notes)) {
//			return ResponseEntity.noContent().build();
//		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws ResourceNotFoundException{
		notesService.softDeleteNotes(id);
		
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
		
	}
	
	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws ResourceNotFoundException{
		notesService.restoreNotes(id);
		
		return CommonUtil.createBuildResponseMessage("Restore Success", HttpStatus.OK);
		
	}
	
	@GetMapping("/recycle-bin")
	public ResponseEntity<?> getUserRecycleBinNotes() throws ResourceNotFoundException{
		Integer userId =2;
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Notes not available in recycle bin", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws ResourceNotFoundException{
		notesService.hardDeleteNotes(id);
		
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> emptyRecycleBin() throws ResourceNotFoundException{
		int userId = 2;
		
		notesService.emptyRecycleBin(userId);
		
		return CommonUtil.createBuildResponseMessage("Delete Success", HttpStatus.OK);
		
	}
	
	@GetMapping("/fav/{noteId}")
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws ResourceNotFoundException{
		
		notesService.favouriteNotes(noteId);
		
		return CommonUtil.createBuildResponseMessage("Notes Added Favourite", HttpStatus.CREATED);
		
	}
	
	@GetMapping("/unfav/{favNoteId}")
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws ResourceNotFoundException{
		
		notesService.unFavouriteNotes(favNoteId);
		
		return CommonUtil.createBuildResponseMessage("Remove Favourite", HttpStatus.OK);
		
	}
	
	@GetMapping("/fav-note")
	public ResponseEntity<?> getUserFavourite() throws ResourceNotFoundException{
		
		List<FavouriteNoteDto> userFavouriteNotes = notesService.getUserFavouriteNotes();
		if(CollectionUtils.isEmpty(userFavouriteNotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(userFavouriteNotes, HttpStatus.OK);
		
	}
	
	@GetMapping("/copy/{id}")
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws ResourceNotFoundException{
		
		Boolean copyNotes = notesService.copyNotes(id);
		if(copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied Success", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Copied failed! Try Again", HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
