package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.QuestionType;
import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.QuestionTypeDTO;
import com.mycompany.myapp.service.dto.SubjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "subject", source = "subject", qualifiedByName = "subjectName")
    @Mapping(target = "type", source = "type", qualifiedByName = "questionTypeLabel")
    QuestionDTO toDto(Question s);

    @Named("subjectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SubjectDTO toDtoSubjectName(Subject subject);

    @Named("questionTypeLabel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "label", source = "label")
    QuestionTypeDTO toDtoQuestionTypeLabel(QuestionType questionType);
}
