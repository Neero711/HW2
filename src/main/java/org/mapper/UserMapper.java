package org.mapper;

import org.dto.UserDTO;
import org.dto.UserRequestDTO;
import org.mapstruct.factory.Mappers;
import org.model.User;
import org.mapstruct.*;

import lombok.*;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRequestDTO dto);

    UserDTO toDTO(User user);
}