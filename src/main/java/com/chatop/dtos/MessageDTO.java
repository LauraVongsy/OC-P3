package com.chatop.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MessageDTO {
    private String message;
    private Integer user_id;
    private Integer rental_id;
}
