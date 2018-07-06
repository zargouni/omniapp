package com.omniacom.omniapp.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.omniacom.omniapp.entity.UploadedFile;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.repository.UploadedFileRepository;

@Service
public class UploadedFileService {

	@Autowired
	UploadedFileRepository fileRepo;

	private String destinationLocation = "D:/files/";

	private String picturesDestinationLocation = new File("").getAbsolutePath()+"/src/main/resources/static";

	private String picturesLocation = "/assets/app/media/img/users/";

	public UploadedFile saveFileToDatabase(UploadedFile uploadedFile) {

		return fileRepo.save(uploadedFile);

	}

	public void saveFileToLocalDisk(MultipartFile multipartFile) throws IOException, FileNotFoundException {

		String outputFileName = getOutputFilename(multipartFile);

		FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(outputFileName));
	}

	public String savePicToLocalDisk(User user, MultipartFile multipartFile) throws IOException, FileNotFoundException {

		String outputFileName = getOutputPictureName(user, multipartFile);

		String miniFileName = getMinifiedPictureName(user, multipartFile);

		FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(outputFileName));
		
		return miniFileName;
	}

	public String getOutputFilename(MultipartFile multipartFile) {

		return getDestinationLocation() + getPicturesLocation() + multipartFile.getOriginalFilename();
	}

	public String getOutputPictureName(User user, MultipartFile multipartFile) {

		return getPicturesDestinationLocation() + getPicturesLocation() + user.getFirstName() + user.getLastName()
				+ multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().indexOf("."));
	}

	public String getMinifiedPictureName(User user, MultipartFile multipartFile) {

		return getPicturesLocation() + user.getFirstName() + user.getLastName()
				+ multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().indexOf("."));
	}

	public String getDestinationLocation() {
		return destinationLocation;
	}

	public String getPicturesDestinationLocation() {
		return picturesDestinationLocation;
	}

	public String getPicturesLocation() {
		return picturesLocation;
	}

	public UploadedFile getUploadedFileInfo(MultipartFile multipartFile) throws IOException {

		UploadedFile fileInfo = new UploadedFile();
		fileInfo.setName(multipartFile.getOriginalFilename());
		fileInfo.setSize(multipartFile.getSize());
		fileInfo.setType(multipartFile.getContentType());
		fileInfo.setLocation(getDestinationLocation());

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
