package com.chatop.controllers;

import com.chatop.dtos.RentalRequestDTO;
import com.chatop.dtos.RentalResponseDTO;
import com.chatop.services.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    @Operation(description = "Retrieves a rental by its id", summary = "Retrieves a rental by its id",
            responses = {@ApiResponse(
                    description = "Successfully retrieved the rental",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RentalResponseDTO.class)),
                            examples = @ExampleObject(value = "{ \"rentals\": [ { \"id\": 1, \"name\": \"Example Rental\", \"surface\": 100, \"price\": 1000.00, \"picture\": \"example.jpg\", \"description\": \"Example description \", \"owner_id\": 1, \"created_at\": \"2024-12-23T12:00:00\", \"updated_at\": \"2024-12-23T13:00:00\" } ] }")))
            })
    @Parameter(name = "id", description = "The unique identifier of the rental", required = true, example = "123")
    public ResponseEntity<RentalResponseDTO> getOneRentalById(@PathVariable("id") Integer id) {
        RentalResponseDTO rentalResponseDTO = rentalService.findById(id);
        if (rentalResponseDTO != null) {
            return ResponseEntity.ok(rentalResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    @Operation(description = "Retrieves all rentals", summary = "Retrieves all rentals",
            responses = {@ApiResponse(
                    description = "Successfully retrieved all rentals",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RentalResponseDTO.class)),
                            examples = @ExampleObject(value = "{ \"rentals\": [ { \"id\": 1, \"name\": \"Example Rental1\", \"surface\": 100, \"price\": 1000.00, \"picture\": \"example1.jpg\", \"description\": \"Example description 1\", \"owner_id\": 1, \"created_at\": \"2024-12-23T12:00:00\", \"updated_at\": \"2024-12-23T13:00:00\" }," + "{ \"id\": 1, \"name\": \"Example Rental2\", \"surface\": 100, \"price\": 1000.00, \"picture\": \"example2.jpg\", \"description\": \"Example description 2\", \"owner_id\": 2, \"created_at\": \"2024-12-23T12:00:00\", \"updated_at\": \"2024-12-23T13:00:00\" } ] }"))),
            })
    public ResponseEntity<Map<String, List<RentalResponseDTO>>> getAllRentals() {
        List<RentalResponseDTO> rentalResponseDTOS = rentalService.findAll();

        // Creating a map with key "rentals" and the list of rentalDTOs as the value
        Map<String, List<RentalResponseDTO>> response = new HashMap<>();
        response.put("rentals", rentalResponseDTOS);

        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Creates a new rental", summary = "Creates a new rental",
            responses = {@ApiResponse(description = "Rental created with success",
                    responseCode = "201",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"message\":\"Rental created with success\"}"))}),
            })
    public ResponseEntity<RentalResponseDTO> createRental(@ModelAttribute RentalRequestDTO rentalRequestDTO, @RequestPart(value = "picture", required = false) MultipartFile file) {

        RentalResponseDTO rentalResponseDTO = rentalService.createRental(rentalRequestDTO, file);
        return ResponseEntity.ok(rentalResponseDTO);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Updates an existing rental", summary = "Updates an existing rental",
            responses = {@ApiResponse(description = "Rental updated with success",
                    responseCode = "201",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"message\":\"Rental updated with success\"}"))}),
            })
    public ResponseEntity<RentalResponseDTO> updateRental(@PathVariable("id") Integer id, @ModelAttribute RentalRequestDTO rentalRequestDTO
    ) {
        RentalResponseDTO updatedRental = rentalService.updateRental(id, rentalRequestDTO);
        return ResponseEntity.ok(updatedRental);
    }
}
