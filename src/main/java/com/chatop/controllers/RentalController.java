package com.chatop.controllers;

import com.chatop.dtos.RentalDTO;
import com.chatop.models.Rentals;
import com.chatop.models.Users;
import com.chatop.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("/{id}")
    public ResponseEntity<Rentals> getOneRentalById(@PathVariable Integer id) {
        Rentals rental = rentalService.findById(id);
        if (rental != null) {
            return ResponseEntity.ok(rental);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Rentals>>> getAllRentals() {
        List<Rentals> rentals = rentalService.findAll();
        Map<String, List<Rentals>> testReturn = new HashMap<>();
        testReturn.put("rentals", rentals);
        return ResponseEntity.ok(testReturn);

    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Rentals> createRental(@ModelAttribute RentalDTO rentalDTO, @RequestPart(value = "picture", required = false) MultipartFile file, @RequestHeader("Authorization") String token) {
        System.out.println(file);
        Rentals rental = rentalService.createRental(rentalDTO, file, token);
        return ResponseEntity.ok(rental);
    }


    private Users getCurrentUser() {
        // Implémentez la logique pour obtenir l'utilisateur authentifié ici.
        return null; // Retournez l'utilisateur actuellement connecté
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rentals> updateRental(
            @PathVariable Integer id,
            @RequestBody RentalDTO rentalDTO,
            @RequestPart(value = "picture", required = false) MultipartFile file,
            @RequestHeader("UserId") Integer userId // Par exemple, récupéré dans l'en-tête de la requête
    ) {
        Rentals updatedRental = rentalService.updateRental(id, rentalDTO, file, userId);
        return ResponseEntity.ok(updatedRental);
    }
}
