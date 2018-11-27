package com.omniacom.omniapp.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.omniacom.omniapp.entity.UploadedFile;
import com.omniacom.omniapp.service.FileStorageService;
import com.omniacom.omniapp.service.IssueService;
import com.omniacom.omniapp.service.TaskService;
import com.omniacom.omniapp.service.UploadedFileService;
import com.omniacom.omniapp.validator.JsonResponse;

@RestController
public class FileController {

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private UploadedFileService fileService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IssueService issueService;

	@PostMapping("/upload")
	public ResponseEntity uploadFile(@RequestParam("id") long id, @RequestParam("file") MultipartFile[] files) {
		boolean success = true;
		UploadedFile dbFile = new UploadedFile();
		for (int i = 0; i < files.length; i++) {
			String fileName = fileStorageService.storeFile(taskService.findOne(id), files[i]);
			dbFile.setName(fileName);
			dbFile.setSize(files[i].getSize());
			dbFile.setType(files[i].getContentType());
			dbFile.setCreationDate(new Date());
			dbFile.setLocation(fileName);
			dbFile.setTask(taskService.findOne(id));

			if(fileService.saveFileToDatabase(dbFile) == null)
				success = false;
		}

		if (success)
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("All Files uploaded");

		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Some or all files were not uploaded");
	}

	@PostMapping(value = "/upload-issue-attachment")
	public ResponseEntity handleFileUploadForIssue(@RequestParam("id") long id,
			@RequestParam("file") MultipartFile[] files) {
		boolean success = true;
		UploadedFile dbFile = new UploadedFile();
		for (int i = 0; i < files.length; i++) {
			String fileName = fileStorageService.storeFile(issueService.findOne(id), files[i]);
			dbFile.setName(fileName);
			dbFile.setSize(files[i].getSize());
			dbFile.setType(files[i].getContentType());
			dbFile.setCreationDate(new Date());
			dbFile.setLocation(fileName);
			dbFile.setIssue(issueService.findOne(id));

			if(fileService.saveFileToDatabase(dbFile) == null)
				success = false;
		}

		if (success)
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("All Files uploaded");

		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Some or all files were not uploaded");

	}

	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/pic/{fileName:.+}")
	public ResponseEntity<Resource> getUserPic(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadPicAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@PostMapping("/delete-attachment")
	public @ResponseBody JsonResponse deleteFile(@RequestParam("id") long fileId) {
		JsonResponse response = new JsonResponse();

		if (fileService.deleteFile(fileId)) {

			response.setStatus("SUCCESS");
		} else {
			response.setStatus("FAIL");
		}
		return response;
	}

}
