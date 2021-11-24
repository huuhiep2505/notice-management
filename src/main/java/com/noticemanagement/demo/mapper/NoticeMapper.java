package com.noticemanagement.demo.mapper;

import com.noticemanagement.demo.dto.NoticeDto;
import com.noticemanagement.demo.entity.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface for mapping between Notice Entity and DTO using mapstruct
 * @see <a href="https://github.com/mapstruct/mapstruct">...</a>
 */
@Mapper(componentModel = "spring")
public interface NoticeMapper {
  // Ignore files to temporary save to local storage
  public Notice dtoToEntity(NoticeDto dto);

  // Ignore files to temporary save to local storage
  public NoticeDto entityToDTO(Notice entity);
}
