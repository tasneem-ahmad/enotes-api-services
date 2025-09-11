package com.bitcodex.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bitcodex.dto.NotesDto;
import com.bitcodex.dto.NotesDto.CategoryDto;
import com.bitcodex.dto.NotesResponse;
import com.bitcodex.entity.FileDetails;
import com.bitcodex.entity.Notes;
import com.bitcodex.exception.ResourceNotFoundException;
import com.bitcodex.repository.CategoryRepository;
import com.bitcodex.repository.FileRepository;
import com.bitcodex.repository.NotesRepository;
import com.bitcodex.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService{
	
	@Autowired
	private NotesRepository notesRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private FileRepository fileRepo;
	
	@Value("${file.upload.path}")
	private String uploadPath;

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
		
		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto =  ob.readValue(notes, NotesDto.class);
		
		//category validation
		checkCategoryExist(notesDto.getCategory());
		
		Notes notesMap = mapper.map(notesDto, Notes.class);
		
		FileDetails fileDtls = saveFileDetails(file);
		
		if(!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		}else {
			notesMap.setFileDetails(null);
		}
		
		Notes saveNotes = notesRepo.save(notesMap);
		
		if(!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		
		return false;
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {

		if(!file.isEmpty() && !ObjectUtils.isEmpty(file)) {
			
			String originaleFileName = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originaleFileName);
			
			List<String> extensionAllow =  Arrays.asList("pdf","xlsx","jpg", "png", "docx");
			if(!extensionAllow.contains(extension)) {
				throw new IllegalArgumentException("invalid file format ! upload only .pdf, .xlsx, .jpg, .docx");
			}
			
			
			
			
			
			
			String rndString = UUID.randomUUID().toString();
			String uploadFileName = rndString+"."+extension;
						
			File saveFile = new File(uploadPath);
			if(!saveFile.exists()) {
				saveFile.mkdir();
			}
			// path : enotesapiservice/notes/java.pdf
			String storePath = uploadPath.concat(uploadFileName);
						
			// upload file
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
			if(upload!=0) {
				FileDetails fileDtls = new FileDetails();
				fileDtls.setOriginalFileName(originaleFileName);
				fileDtls.setDisplayFileName(getDisplayName(originaleFileName));
				fileDtls.setUploadFileName(uploadFileName);
				fileDtls.setFileSize(file.getSize());
				fileDtls.setPath(storePath);
				FileDetails saveFileDtls = fileRepo.save(fileDtls); 
				return saveFileDtls;
			}
		}
		
		return null;
	}

	private String getDisplayName(String originaleFileName) {
		// TODO Auto-generated method stub
		
		String extension = FilenameUtils.getExtension(originaleFileName);
		String fileName = FilenameUtils.removeExtension(originaleFileName);
		
		if(fileName.length()>8) {
			fileName = fileName.substring(0,7);
		}
		fileName = fileName+"."+extension;
		return fileName;
	}

	private void checkCategoryExist(CategoryDto category) throws Exception{

		categoryRepo.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category is invalid"));
		
	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepo.findAll().stream()
				.map(note -> mapper.map(note, NotesDto.class)).toList();
	}

	@Override
	public byte[] downloadFile(FileDetails fileDtls) throws Exception {
		
		InputStream io = new FileInputStream(fileDtls.getPath());
		
		byte[] byteData = StreamUtils.copyToByteArray(io);
		return StreamUtils.copyToByteArray(io);
	}


	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		
		FileDetails fileDtls = fileRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("File is not available"));
		return fileDtls;
	}


	@Override
	public NotesResponse getAllNotesByUser(Integer userId,Integer pageNo, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = notesRepo.findByCreatedBy(userId,pageable);
		
		List<NotesDto> notesDto =  pageNotes.get().map(n->mapper.map(n,NotesDto.class)).toList();
		
		NotesResponse notes = NotesResponse.builder()
											.notes(notesDto)
											.pageNo(pageNotes.getNumber())
											.pageSize(pageNotes.getSize())
											.totalElements(pageNotes.getTotalElements())
											.totalPages(pageNotes.getTotalPages())
											.isFirst(pageNotes.isFirst())
											.isLast(pageNotes.isLast())
											.build();
											
		return notes;
	}
}
