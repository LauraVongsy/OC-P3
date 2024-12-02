package com.chatop.controllers;

import com.chatop.dtos.MessageDTO;
import com.chatop.models.Message;
import com.chatop.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping()
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageDTO messageDTO) {
        Message message = this.messageService.createMessage(messageDTO);
        if (message != null) {
            Map<String, String> responseMessage = new HashMap<>();
            responseMessage.put("message", "Message send with success");
            return ResponseEntity.ok(responseMessage);
        } else {
            throw new RuntimeException("Error while sending message");
        }
    }
}
