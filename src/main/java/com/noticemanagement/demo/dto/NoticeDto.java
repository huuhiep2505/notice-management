package com.noticemanagement.demo.dto;

import com.noticemanagement.demo.model.FileDetail;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * DTO for interactive with Rest Controller base on Notice Entity
 * @Author thanhbc1@fsoft.com.vn
 */
@Data
public class NoticeDto {
  private Long id;
  // Title of notice
  private String title;
  // Content of notice
  private String content;
  // Date notice was started
  private Date noticeStartDate;
  // Date notice will be terminated
  private Date noticeEndDate;
  // Addition files for describing notice
  private List<FileDetail> files;
  // Creator of notice
  private String author;
  // Number of views
  private long views;
  // Notice initialization date
  private Date registrationDate;
  // Last editor
  private String modifyBy;
  // Last time edited
  private Date modifyDate;
  // private String otherElements; -> To be defined later for other business.
}
