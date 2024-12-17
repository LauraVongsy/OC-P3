package com.chatop.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    private String email;
    private String name;
    private String password;
}
