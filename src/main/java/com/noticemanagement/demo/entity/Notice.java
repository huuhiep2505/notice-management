package com.noticemanagement.demo.entity;

import com.noticemanagement.demo.model.FileDetail;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * Entity class to interactive with database
 * @Author thanhbc1@fsoft.com.vn
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "notice")
public class Notice {
  @Id
  @GeneratedValue
  private Long id;

  // Title of notice
  private String title;

  // Content of notice
  private String content;

  // Date notice was started
  @Column(name = "notice_start_date")
  private Date noticeStartDate;

  // Date notice will be terminated
  @Column(name = "notice_start_date")
  private Date noticeEndDate;

  // Addition files for describing notice
  @Column(name = "files")
  private List<FileDetail> files;

  // Creator of notice
  private String author;

  // Number of views
  private long views;

  // Notice initialization date
  @Column(name = "registration_date")
  private Date registrationDate;

  // Flag for deleting notice in case we need to read history of notice or reused it. Default is false when init
  @Column(name = "is_deleted")
  private boolean isDeleted = false;

  // Last editor
  @Column(name = "modify_by")
  private String modifyBy;

  // Last time edited
  @Column(name = "modify_date")
  private Date modifyDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Notice notice = (Notice) o;
    return id != null && Objects.equals(id, notice.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
