package com.noticemanagement.demo.controller;

import com.noticemanagement.demo.dto.NoticeDto;
import com.noticemanagement.demo.entity.Notice;
import com.noticemanagement.demo.model.JwtRequest;
import com.noticemanagement.demo.util.Utils;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import com.noticemanagement.demo.service.NoticeManagementService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Rest controller for CRUD Notice
 *
 * @author thanhbc1@fsoft.com.vn
 */
@RestController
@RequestMapping("/notice-management")
public class NoticeManagementController {

  @Autowired
  private NoticeManagementService noticeManagementService;

  /**
   * Get all notice with given page info
   *
   * @param offset: paging offset
   * @param limit:  paging limit
   * @return list of notice with paging
   */
  @GetMapping("/getAll")
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "10") int limit) {
    return ResponseEntity.status(HttpStatus.OK).body(noticeManagementService.findAll(offset, limit));
  }

  /**
   * Get notice details with given id
   *
   * @param id the notice id
   * @return notice
   */
  @GetMapping("/get/{id}")
  public ResponseEntity<?> getNoticeById(@PathVariable("id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(noticeManagementService.getNoticeById(Long.parseLong(id)));
  }

  /**
   * Create new Notice
   *
   * @param noticeDto: Notice DTO need to be updated
   * @param request:   HttpServletRequest
   * @return true if Notice created successfully, otherwise is false
   * @throws Exception: exception when cannot get session info
   */
  @PostMapping(path = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<?> initNotice(@RequestPart NoticeDto noticeDto, @RequestPart List<MultipartFile> documents,
      HttpServletRequest request) throws Exception {
    JwtRequest jwtRequest = Objects.requireNonNull(Utils.sessionFromReq(request));
    return noticeManagementService.initNotice(noticeDto, documents, jwtRequest);
  }

  /**
   * Update notice
   *
   * @param noticeDto: Notice need to update
   * @param request:   HttpServletRequest
   * @return Notice updated
   * @throws Exception: exception when cannot get session info
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateCustomer(@RequestPart NoticeDto noticeDto, @RequestPart List<MultipartFile> documents,
      HttpServletRequest request) throws Exception {
    JwtRequest jwtRequest = Objects.requireNonNull(Utils.sessionFromReq(request));
    return noticeManagementService.modifyNotice(noticeDto, documents, jwtRequest);
  }

  /**
   * Delete notice
   *
   * @param id: Notice ID need to delete
   * @return true if Notice deleted successfully, otherwise is false
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteNoticeById(@PathVariable("id") long id) {
    return noticeManagementService.deleteNotice(id);
  }
}
