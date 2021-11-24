package com.noticemanagement.demo.service;

import com.noticemanagement.demo.entity.FileStorageProperties;
import com.noticemanagement.demo.exception.FileStorageException;
import com.noticemanagement.demo.model.FileDetail;
import com.noticemanagement.demo.repository.FileStoragePropertiesRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileStorageService {

  @Value("${file.upload-dir}")
  private final Path fileStorageLocation;

  @Autowired
  FileStoragePropertiesRepository fileStoragePropertiesRepository;

  @Autowired
  public FileStorageService(FileStorageProperties fileStorageProperties) {
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        .toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  public FileDetail storeFile(MultipartFile file, Long noticeId) {
    FileDetail fileDetail = new FileDetail();
    // Normalize file name
    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileName = "";
    try {
      // Check if the file's name contains invalid characters
      if(originalFileName.contains("..")) {
        throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
      }
      String fileExtension = "";
      try {
        // Get file extension
        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
      } catch(Exception e) {
        fileExtension = "";
      }
      fileName = noticeId + "_" + fileExtension;
      // Copy file to the target location (Replacing existing file with the same name)
      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      // Check if FileStorageProperties exist or not
      FileStorageProperties doc = fileStoragePropertiesRepository.getDocumentByNoticeId(noticeId);
      if(doc != null) {
        doc.setDocumentFormat(file.getContentType());
        doc.setFileName(fileName);
        // If FileStorageProperties existed in database -> execute update
        fileStoragePropertiesRepository.saveAndFlush(doc);
      } else {
        FileStorageProperties newDoc = new FileStorageProperties();
        newDoc.setNoticeId(noticeId);
        newDoc.setDocumentFormat(file.getContentType());
        newDoc.setFileName(fileName);
        // If FileStorageProperties not existed in database -> create new one
        fileStoragePropertiesRepository.save(newDoc);
      }

      // Getting file download uri
      String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
          .path("/downloadFile/")
          .path(fileName)
          .toUriString();
      fileDetail.setFileName(fileName);
      fileDetail.setSize(file.getSize());
      fileDetail.setFileDownloadUri(fileDownloadUri);

      return fileDetail;
    } catch (IOException ex) {
      throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
    }
  }
  // TODO: should implement method to download file with given notice ID later
}
