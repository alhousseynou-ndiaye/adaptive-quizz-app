package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Answer;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.AnswerDTO;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Answer} and its DTO {@link AnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "question", source = "question", qualifiedByName = "questionPrompt")
    AnswerDTO toDto(Answer s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("questionPrompt")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "prompt", source = "prompt")
    QuestionDTO toDtoQuestionPrompt(Question question);

    default String map(byte[] value) {
        return new String(value);
    }
}
