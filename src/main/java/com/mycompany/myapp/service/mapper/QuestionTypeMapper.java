package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.QuestionType;
import com.mycompany.myapp.service.dto.QuestionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionType} and its DTO {@link QuestionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionTypeMapper extends EntityMapper<QuestionTypeDTO, QuestionType> {}
