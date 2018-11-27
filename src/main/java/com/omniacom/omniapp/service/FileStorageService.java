package com.omniacom.omniapp.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.omniacom.omniapp.config.FileStorageProperties;
import com.omniacom.omniapp.entity.Client;
import com.omniacom.omniapp.entity.Issue;
import com.omniacom.omniapp.entity.Task;
import com.omniacom.omniapp.entity.User;
import com.omniacom.omniapp.exception.FileStorageException;
import com.omniacom.omniapp.exception.MyFileNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    
    private final Path picStorageLocation;
    
    private final Path logoStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.picStorageLocation = Paths.get(fileStorageProperties.getUploadPic())
                .toAbsolutePath().normalize();
        this.logoStorageLocation = Paths.get(fileStorageProperties.getUploadLogo())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            Files.createDirectories(this.picStorageLocation);
            Files.createDirectories(this.logoStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(Object object, MultipartFile file) {
        // Normalize file name
    	String fileName = "";
    	if(object instanceof Issue) {
    		Issue issue = (Issue) object;
    		fileName = StringUtils.cleanPath("issue_"+issue.getId()+"_"+file.getOriginalFilename());
    	}else if(object instanceof Task) {
    		Task task = (Task) object;
    		fileName = StringUtils.cleanPath("task_"+task.getId()+"_"+file.getOriginalFilename());
    	}
         

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    public String storePic(User user,MultipartFile file) {
        // Normalize file name
    	String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = StringUtils.cleanPath(user.getUserName()+"."+fileExtension);

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.picStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    
    public Resource loadPicAsResource(String fileName) {
        try {
            Path filePath = this.picStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

	public String storeLogo(Client client, MultipartFile file) {
		 // Normalize file name
    	String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = StringUtils.cleanPath(client.getName()+"."+fileExtension);

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.logoStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
	}
	
	 public Resource loadLogoAsResource(String fileName) {
	        try {
	            Path filePath = this.logoStorageLocation.resolve(fileName).normalize();
	            Resource resource = new UrlResource(filePath.toUri());
	            if(resource.exists()) {
	                return resource;
	            } else {
	                throw new MyFileNotFoundException("File not found " + fileName);
	            }
	        } catch (MalformedURLException ex) {
	            throw new MyFileNotFoundException("File not found " + fileName, ex);
	        }
	    }
}
