package com.chatop.services;

import com.chatop.dtos.MessageRequestDTO;
import com.chatop.dtos.MessageResponseDTO;
import com.chatop.mapper.MessagesMapper;
import com.chatop.models.Message;
import com.chatop.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessagesMapper messagesMapper;

    /**
     * Creates a new message and saves it to the database
     *
     * @param messageRequestDTO
     * @return MessageResponseDTO
     */
    public MessageResponseDTO createMessage(MessageRequestDTO messageRequestDTO) {

        Message message = messagesMapper.toEntity(messageRequestDTO);

        return messagesMapper.toResponseDTO(messageRepository.save(message));
    }
}
