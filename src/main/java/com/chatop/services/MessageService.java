package com.chatop.services;

import com.chatop.dtos.MessageDTO;
import com.chatop.models.Message;
import com.chatop.repositories.MessageRepository;
import com.chatop.repositories.RentalRepository;
import com.chatop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    UserRepository userRepository;

    public Message createMessage(MessageDTO messageDTO) {
        Message message = new Message();
        message.setMessage(messageDTO.getMessage());
        message.setRental(this.rentalRepository.getReferenceById(messageDTO.getRental_id()));
        message.setUser(this.userRepository.getReferenceById(messageDTO.getUser_id()));
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
