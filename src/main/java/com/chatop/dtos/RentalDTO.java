package com.chatop.dtos;

import java.time.LocalDateTime;

public class RentalDTO {
    private Integer id;
    private String name;
    private Integer surface;
    private Integer price;

    private String description;
    private Integer owner_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // Constructeur vide
    public RentalDTO() {
    }

    // Constructeur complet
    public RentalDTO(Integer id, String name, Integer surface, Integer price, String description, Integer ownerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;

        this.description = description;
        this.owner_id = ownerId;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Integer ownerId) {
        this.owner_id = ownerId;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
