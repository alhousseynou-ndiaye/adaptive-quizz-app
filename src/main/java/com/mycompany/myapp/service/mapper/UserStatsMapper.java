package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserStats;
import com.mycompany.myapp.service.dto.SubjectDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.service.dto.UserStatsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserStats} and its DTO {@link UserStatsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserStatsMapper extends EntityMapper<UserStatsDTO, UserStats> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "subject", source = "subject", qualifiedByName = "subjectName")
    UserStatsDTO toDto(UserStats s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("subjectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SubjectDTO toDtoSubjectName(Subject subject);
}
