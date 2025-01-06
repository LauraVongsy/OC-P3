package com.chatop.mapper;

import com.chatop.dtos.RentalRequestDTO;
import com.chatop.dtos.RentalResponseDTO;
import com.chatop.models.Rentals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RentalsMapper {

    @Autowired
    UsersMapper usersMapper;

    public RentalResponseDTO toResponseDTO(Rentals rental) {

        RentalResponseDTO dto = new RentalResponseDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicture(rental.getPicture());
        dto.setDescription(rental.getDescription());
        dto.setOwner_id(usersMapper.toResponseDTO(rental.getOwner()).getId());
        dto.setCreated_at(rental.getCreated_at());
        dto.setUpdated_at(rental.getUpdated_at());

        return dto;
    }

    public Rentals toEntity(RentalRequestDTO rentalRequestDTO) {
        Rentals rental = new Rentals();
        rental.setName(rentalRequestDTO.getName());
        rental.setSurface(rentalRequestDTO.getSurface());
        rental.setPrice(rentalRequestDTO.getPrice());
        rental.setDescription(rentalRequestDTO.getDescription());

        return rental;
    }

    public List<RentalResponseDTO> toResponseDTOList(List<Rentals> rentals) {
        if (rentals.isEmpty()) {
            return new ArrayList<>();
        }

        List<RentalResponseDTO> responseDTOList = new ArrayList<>();
        for (Rentals rental : rentals) {
            responseDTOList.add(toResponseDTO(rental));
        }
        return responseDTOList;
    }

    public void updateEntityFromDTO(RentalRequestDTO rentalRequestDTO, Rentals existingRental) {
        if (rentalRequestDTO.getName() != null) {
            existingRental.setName(rentalRequestDTO.getName());
        }
        if (rentalRequestDTO.getSurface() != null) {
            existingRental.setSurface(rentalRequestDTO.getSurface());
        }
        if (rentalRequestDTO.getPrice() != null) {
            existingRental.setPrice(rentalRequestDTO.getPrice());
        }
        if (rentalRequestDTO.getDescription() != null) {
            existingRental.setDescription(rentalRequestDTO.getDescription());
        }
        existingRental.setUpdated_at(LocalDateTime.now());
    }

}
