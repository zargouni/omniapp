package com.omniacom.omniapp.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.omniacom.omniapp.entity.UploadedFile;
import com.omniacom.omniapp.repository.UploadedFileRepository;

@Service
public class UploadedFileService {

	@Autowired
	UploadedFileRepository fileRepo;

	public UploadedFile saveFileToDatabase(UploadedFile uploadedFile) {

		return fileRepo.save(uploadedFile);

	}

	public UploadedFile getUploadedFileInfo(MultipartFile multipartFile) throws IOException {

		UploadedFile fileInfo = new UploadedFile();
		fileInfo.setName(multipartFile.getOriginalFilename());
		fileInfo.setSize(multipartFile.getSize());
		fileInfo.setType(multipartFile.getContentType());
		// fileInfo.setLocation(getDestinationLocation());

		return fileInfo;
	}

	public boolean deleteFile(long fileId) {
		UploadedFile file = fileRepo.findOne(fileId);
		if (file != null) {
			file.setDeleted(true);
			fileRepo.save(file);
			return true;
		}
		return false;
	}
}
