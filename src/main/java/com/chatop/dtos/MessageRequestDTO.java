package com.chatop.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDTO {
    private String message;
    private Integer user_id;
    private Integer rental_id;
}
