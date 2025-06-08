package dev.wortel.meshok.mapper;

import dev.wortel.meshok.dto.RegistrationForm;
import dev.wortel.meshok.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "status", constant = "NEED_CONFIRM")
    User mapToUser(RegistrationForm form, String hashPassword);
}
