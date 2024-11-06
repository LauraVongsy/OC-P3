package com.chatop.controllers;

import com.chatop.models.Rentals;
import com.chatop.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentalController {
    @Autowired
    private RentalService rentalService ;
    @GetMapping("/rentals")
    List<Rentals> getAllRentals(){
    return rentalService.findAll();
    }
}
