package com.chatop.services;

import com.chatop.dtos.RentalRequestDTO;
import com.chatop.dtos.RentalResponseDTO;
import com.chatop.exceptions.NotFoundException;
import com.chatop.exceptions.UnauthorizedException;
import com.chatop.mapper.RentalsMapper;
import com.chatop.models.Rentals;
import com.chatop.models.Users;
import com.chatop.repositories.RentalRepository;
import com.chatop.repositories.UserRepository;
import com.chatop.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RentalService {

    private static final Logger logger = Logger.getLogger(RentalService.class.getName());
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RentalsMapper rentalsMapper;

    public List<RentalResponseDTO> findAll() {
        List<Rentals> rentals = rentalRepository.findAll();
        return rentalsMapper.toResponseDTOList(rentals);
    }

    public RentalResponseDTO findById(Integer id) {
        Rentals rental = rentalRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Rental with id " + id + " not found")
        );

        return rentalsMapper.toResponseDTO(rental);
    }

    /**
     * Creates a new rental and saves it to the database
     * If the file is not empty, it saves the file to the uploads/rentals directory
     * If the user is not found, it throws a NotFoundException
     * If the file cannot be saved, it throws a RuntimeException
     *
     * @param rentalRequestDTO
     * @param file
     * @return RentalResponseDTO
     */
    public RentalResponseDTO createRental(RentalRequestDTO rentalRequestDTO, MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();
        Integer userId = this.jwtService.extractUserId(token);
        Users user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));

        Rentals rental = rentalsMapper.toEntity(rentalRequestDTO);

        rental.setOwner(user);
        rental.setCreated_at(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            try {
                Path directoryPath = Paths.get("src/main/resources/static/uploads/rentals");
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath); // Create the directory if it doesn't exist
                }

                // Generates a unique file name for the uploaded file
                String imageFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path imagePath = directoryPath.resolve(imageFileName);

                // Save the file to the specified path
                Files.copy(file.getInputStream(), imagePath);

                // Set the relative path for the image in the Rentals object
                String relativePath = "uploads/rentals/" + imageFileName;
                rental.setPicture(relativePath);

            } catch (Exception e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        return rentalsMapper.toResponseDTO(rentalRepository.save(rental));
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex + 1) : "";
    }

    /**
     * Updates a rental in the database with the given id
     * If the rental is not found, it throws a NotFoundException
     * If the user is not allowed to modify the rental, it throws an UnauthorizedException
     *
     * @param id
     * @param rentalRequestDTO
     * @return RentalResponseDTO
     */
    public RentalResponseDTO updateRental(Integer id, RentalRequestDTO rentalRequestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();
        Integer userId = this.jwtService.extractUserId(token);
        Rentals existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rental with id " + id + " not found"));

        if (!existingRental.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You are not allowed to modify this rental.");
        }

        rentalsMapper.updateEntityFromDTO(rentalRequestDTO, existingRental);

        Rentals updatedRental = rentalRepository.save(existingRental);

        return rentalsMapper.toResponseDTO(updatedRental);
    }

}
