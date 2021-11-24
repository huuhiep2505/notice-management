package com.noticemanagement.demo.repository;

import com.noticemanagement.demo.entity.FileStorageProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository to interactive with FileStorageProperties Entity
 * @author thanhbc1@fsoft.com.vn
 */
public interface FileStoragePropertiesRepository extends JpaRepository<FileStorageProperties, Integer> {
  // Get FileStorageProperties with the given notice
  @Query(value = "Select * from merchant_documents where notice_id = :noticeId", nativeQuery = true)
  FileStorageProperties getDocumentByNoticeId(Long noticeId);
}
