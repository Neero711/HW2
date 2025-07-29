package org.example.mapper;

import org.example.dto.UserDTO;
import org.example.dto.UserRequestDTO;
import org.mapstruct.factory.Mappers;
import org.example.model.User;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRequestDTO dto);

    UserDTO toDTO(User user);
}