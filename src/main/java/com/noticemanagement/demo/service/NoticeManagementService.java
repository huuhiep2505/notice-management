package com.noticemanagement.demo.service;

import com.noticemanagement.demo.dto.NoticeDto;
import com.noticemanagement.demo.entity.Notice;
import com.noticemanagement.demo.mapper.NoticeMapper;
import com.noticemanagement.demo.model.FileDetail;
import com.noticemanagement.demo.model.JwtRequest;
import com.sun.org.apache.xpath.internal.operations.Mult;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.noticemanagement.demo.repository.NoticeRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Service to handle request from NoticeManagementController
 * @author thanhbc1@fsoft.com.vn
 *
 */
@Service
public class NoticeManagementService {
    private static final Logger logger = Logger.getLogger(NoticeManagementService.class);

    @Value("${file.upload-dir}")
    private String fileStoragePath;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Method to list out page of Notice list with given limit and offset.
     *      Addition param limit, offset to improve performance
     * @param limit: limit number or records
     * @param offset: offset of page
     * @return Pageable of notice
     */
    @NonNull
    public ResponseEntity<?> findAll(int limit,int offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        return ResponseEntity.ok(noticeRepository.findAll(pageable));
    }

    /**
     * Method to get detail of notice in Notice list
     * @param id: id of Notice
     * @return the notice with given id
     */
    public ResponseEntity<?> getNoticeById(long id) {
        Notice notice = noticeRepository.findById(id);
        if (notice == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            long numberOfViews = notice.getViews();
            // Increase numberOfViews each time request views called
            notice.setViews(++numberOfViews);
            return ResponseEntity.ok(noticeMapper.entityToDTO(noticeRepository.saveAndFlush(notice)));
        }
    }

    /**
     * Method to init a new notice
     * @param dto: detail of notice from user
     * @return notice was created
     *       MultipartFile will be saved into database, for long-term we should store file to other storage
     */
    public ResponseEntity<?> initNotice(NoticeDto dto, List<MultipartFile> documents, JwtRequest request) {
        Notice notice = noticeMapper.dtoToEntity(dto);
        notice.setAuthor(request.getUsername()); // Get current login user to set Author
        //Set default isDeleted
        notice.setDeleted(false);
        transferFileToStorage(documents, notice);
        List<FileDetail> fileDetailList = new ArrayList<>();
        // Storage files and mapping with Notice
        for (MultipartFile file :documents) {
            FileDetail fileDetail = fileStorageService.storeFile(file, notice.getId());
            fileDetailList.add(fileDetail);
        }
        notice.setFiles(fileDetailList);
        // Save notice and return
        return ResponseEntity.ok(noticeRepository.save(notice));
    }

    /**
     * @param dto: notice dto for updating
     * @param request: request for authen
     * @return notice updated
     */
    public ResponseEntity<?> modifyNotice(NoticeDto dto, List<MultipartFile> documents, JwtRequest request) {
        Optional<Notice> optional = noticeRepository.findById(dto.getId());
        if (!optional.isPresent()) {
            logger.info("The given notice did not existed in system !");
            logger.info("Create new notice ");
            // If notice did not exist in database, create new one
            return initNotice(dto, documents, request);
        } else {
            Notice noticeToBeEdited = optional.get();
            // Update modifier info
            noticeToBeEdited.setModifyBy(request.getUsername());
            noticeToBeEdited.setModifyDate(new Date());
            // Set isDeleted = false in case notice was deleted before
            noticeToBeEdited.setDeleted(false);
            transferFileToStorage(documents, noticeToBeEdited);
            // Update notice and return
            return ResponseEntity.ok(noticeRepository.saveAndFlush(noticeToBeEdited));
        }
    }

    /**
     * Method to update isDeleted status of notice to true when need to delete
     * @param id: id of notice need to be deleted
     * @return true if delete success, otherwise return false
     */
    public ResponseEntity<?> deleteNotice(long id) {
        // Check if notice exist or not
        Notice notice = noticeRepository.findById(id);
        if (notice == null) {
            logger.info("Cannot found notice with given id");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            notice.setDeleted(true);
            // Update isDeleted to true and save to database
            noticeRepository.saveAndFlush(notice);
            logger.info("Notice deleted successfully !");
            return ResponseEntity.ok(true);
        }
    }

    /**
     * Storage file and mapping with notice
     * @param files list file need to storage and mapping with given notice
     * @param notice notice
     */
    private void transferFileToStorage(List<MultipartFile> files, Notice notice) {
        List<FileDetail> fileDetailList = new ArrayList<>();
        for (MultipartFile file : files) {
            FileDetail fileDetail = fileStorageService.storeFile(file, notice.getId());
            fileDetailList.add(fileDetail);
        }
        notice.setFiles(fileDetailList);
    }
}
