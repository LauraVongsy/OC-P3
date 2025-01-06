package com.chatop.mapper;

import com.chatop.dtos.UserRequestDTO;
import com.chatop.dtos.UserResponseDTO;
import com.chatop.models.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsersMapper {

    // Convertir Users (entité) en UserResponseDTO (pour les réponses sortantes)
    public UserResponseDTO toResponseDTO(Users user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setCreated_at(user.getCreated_at());
        dto.setUpdated_at(user.getUpdated_at());

        // Password est ignoré grâce à @JsonIgnore dans UserResponseDTO
        return dto;
    }

    // Convertir UserRequestDTO (pour les requêtes entrantes) en Users (entité)
    public Users toEntity(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }

        Users user = new Users();
        user.setEmail(userRequestDTO.getEmail());
        user.setName(userRequestDTO.getName());
        user.setPassword(userRequestDTO.getPassword());
        return user;
    }

    // Convertir une liste de Users en une liste de UserResponseDTO
    public List<UserResponseDTO> toResponseDTOList(List<Users> users) {
        if (users == null || users.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserResponseDTO> responseDTOList = new ArrayList<>();
        for (Users user : users) {
            responseDTOList.add(toResponseDTO(user));
        }
        return responseDTOList;
    }

    // Convertir une liste de UserRequestDTO en une liste de Users
    public List<Users> toEntityList(List<UserRequestDTO> userRequestDTOs) {
        if (userRequestDTOs == null || userRequestDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Users> usersList = new ArrayList<>();
        for (UserRequestDTO dto : userRequestDTOs) {
            usersList.add(toEntity(dto));
        }
        return usersList;
    }
}
