package com.chatop.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponseDTO {
    private String message;
    private Integer user_id;
    private Integer rental_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
