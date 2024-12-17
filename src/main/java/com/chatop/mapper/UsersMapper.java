package com.chatop.mapper;

import com.chatop.dtos.UserRequestDTO;
import com.chatop.dtos.UserResponseDTO;
import com.chatop.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface UsersMapper {
    @Mapping(target = "id", source = "id")
    UserResponseDTO toResponseDTO(Users user); // For outgoing responses

    Users toEntity(UserRequestDTO userRequestDTO); // For incoming requests

    List<UserResponseDTO> toResponseDTOList(List<Users> users);

    List<Users> toEntityList(List<UserRequestDTO> userRequestDTOs);

}
