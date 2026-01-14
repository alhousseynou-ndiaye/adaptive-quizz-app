package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.service.dto.SubjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subject} and its DTO {@link SubjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {}
