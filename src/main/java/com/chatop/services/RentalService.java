package com.chatop.services;

import com.chatop.models.Rentals;
import com.chatop.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {
    @Autowired
    RentalRepository rentalRepository;

public List<Rentals> findAll(){
    return rentalRepository.findAll();
}
}
