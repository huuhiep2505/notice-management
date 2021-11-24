package com.noticemanagement.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Entity
@Table(name = "merchant_documents")
@Getter
@Setter
public class FileStorageProperties {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "file_id")
  private Integer fileId;

  // The notice id
  @Column(name = "notice_id")
  private Long noticeId;

  // File name
  @Column(name = "file_name")
  private String fileName;

  @Column(name = "document_format")
  private String documentFormat;

  // Dir to store file
  @Column(name = "upload_dir")
  private String uploadDir;
}
