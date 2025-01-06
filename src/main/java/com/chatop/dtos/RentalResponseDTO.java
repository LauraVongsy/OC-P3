package com.chatop.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalResponseDTO {
    private Integer id;
    private String name;
    private Integer surface;
    private Integer price;
    private String picture;
    private String description;
    private Integer owner_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
