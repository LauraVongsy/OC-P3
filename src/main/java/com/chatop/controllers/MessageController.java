package com.chatop.controllers;

import com.chatop.dtos.MessageRequestDTO;
import com.chatop.dtos.MessageResponseDTO;
import com.chatop.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping()
    @Operation(description = "Sends a message to the rental's owner", summary = "Sends a message to the rental's owner", responses = {
            @ApiResponse(
                    description = "Message sent with success",
                    responseCode = "201",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"message\":\"Message sent with success\"}"))})})
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        MessageResponseDTO messageResponseDTO = this.messageService.createMessage(messageRequestDTO);
        if (messageResponseDTO != null) {
            Map<String, String> responseMessage = new HashMap<>();
            responseMessage.put("message", "Message sent with success");
            return ResponseEntity.ok(responseMessage);
        } else {
            throw new RuntimeException("Error while sending message");
        }
    }
}
