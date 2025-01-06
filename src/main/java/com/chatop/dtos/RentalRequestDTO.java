package com.chatop.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalRequestDTO {
    private String name;
    private Integer surface;
    private Integer price;
    private String description;
}
