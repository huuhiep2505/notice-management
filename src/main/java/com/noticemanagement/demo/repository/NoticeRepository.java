package com.noticemanagement.demo.repository;

import com.noticemanagement.demo.entity.Notice;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;


/**
 * Repository to interactive with Notice Entity
 * @author thanhbc1@fsoft.com.vn
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query(value = "select * from notice where is_deleted = false", nativeQuery = true)
    Page<Notice> findAll(@NonNull Pageable pageable);

    @Query(value = "select * from notice where id = :id and is_deleted = false", nativeQuery = true)
    Notice findById(long id);
}
