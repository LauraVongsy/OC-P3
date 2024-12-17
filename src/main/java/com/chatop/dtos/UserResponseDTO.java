package com.chatop.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private Integer id;
    private String email;
    private String name;

    @JsonIgnore
    private String password;
}
