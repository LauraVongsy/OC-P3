package com.chatop.mapper;

import com.chatop.dtos.MessageRequestDTO;
import com.chatop.dtos.MessageResponseDTO;
import com.chatop.models.Message;
import com.chatop.repositories.RentalRepository;
import com.chatop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessagesMapper {

    @Autowired
    RentalsMapper rentalsMapper;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    UserRepository userRepository;

    public MessageResponseDTO toResponseDTO(Message message) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setMessage(message.getMessage());
        dto.setRental_id(rentalsMapper.toResponseDTO(message.getRental()).getId());
        dto.setUser_id(usersMapper.toResponseDTO(message.getUser()).getId());
        dto.setCreated_at(message.getCreated_at());
        dto.setUpdated_at(message.getUpdated_at());

        return dto;
    }

    public Message toEntity(MessageRequestDTO messageRequestDTO) {
        Message message = new Message();
        message.setMessage(messageRequestDTO.getMessage());
        message.setUser(userRepository.getReferenceById(messageRequestDTO.getUser_id()));
        message.setRental(rentalRepository.getReferenceById(messageRequestDTO.getRental_id()));
        message.setCreated_at(LocalDateTime.now());
        return message;
    }

}
