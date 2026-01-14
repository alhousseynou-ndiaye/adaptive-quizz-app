package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.ReviewReminder;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.ReviewReminderDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReviewReminder} and its DTO {@link ReviewReminderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewReminderMapper extends EntityMapper<ReviewReminderDTO, ReviewReminder> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "question", source = "question", qualifiedByName = "questionPrompt")
    ReviewReminderDTO toDto(ReviewReminder s);

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
