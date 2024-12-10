package com.chatop.services;

import com.chatop.dtos.RentalDTO;
import com.chatop.exceptions.NotFoundException;
import com.chatop.exceptions.UnauthorizedException;
import com.chatop.models.Rentals;
import com.chatop.models.Users;
import com.chatop.repositories.RentalRepository;
import com.chatop.repositories.UserRepository;
import com.chatop.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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

    public List<Rentals> findAll() {
        return rentalRepository.findAll();
    }

    public Rentals findById(Integer id) {
        return rentalRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Rental with id " + id + " not found")
        );
    }

    public Rentals createRental(RentalDTO rentalDTO, MultipartFile file, String token) {
        //TODO: store the file in a specific folder
        //TODO: store the relative path to the file and add this to the Rentals object before saving
        Integer userId = this.jwtService.extractUserId(token.substring(7));
        Users user = this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));

        Rentals rental = new Rentals();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());

        rental.setDescription(rentalDTO.getDescription());
        rental.setOwner(user);
        rental.setCreatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            try {
                // Define the storage path under resources/static/uploads/rentals
                Path directoryPath = Paths.get("src/main/resources/static/uploads/rentals");
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath); // Create the directory if it doesn't exist
                }

                // Generate a unique file name for the uploaded file
                String imageFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path imagePath = directoryPath.resolve(imageFileName);

                // Save the file to the specified path
                Files.copy(file.getInputStream(), imagePath);

                // Set the relative path for the image in the Rentals object
                String relativePath = "uploads/rentals/" + imageFileName;
                rental.setPicture(relativePath);

              /*  // Define the local folder path to store the file
                // Use an absolute path relative to the current working directory
                String uploadDirectory = "uploads/rentals/";
                Path path = Paths.get(uploadDirectory).toAbsolutePath();

                // Create the folder if it doesn't exist
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                String fileExtension = getFileExtension(file.getOriginalFilename());
                String uniqueFileName = UUID.randomUUID() + "." + fileExtension;
                Path filePath = path.resolve(uniqueFileName);

                file.transferTo(filePath.toFile());
                String relativePath = uploadDirectory + uniqueFileName;

                rental.setPicture(relativePath); // Utiliser le chemin relatif ici*/
            } catch (Exception e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        return rentalRepository.save(rental);
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex + 1) : "";
    }

    public Rentals updateRental(Integer id, RentalDTO rentalDTO, MultipartFile file, Integer userId) {
        logger.info("Starting updateRental process for Rental ID: " + id + " by User ID: " + userId);

        Rentals existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rental with id " + id + " not found"));

        // Vérifier si l'utilisateur est bien le propriétaire du rental
        if (!existingRental.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You are not allowed to modify this rental.");
        }

        // Mettre à jour les champs
        existingRental.setName(rentalDTO.getName());
        existingRental.setSurface(rentalDTO.getSurface());
        existingRental.setPrice(rentalDTO.getPrice());
        existingRental.setDescription(rentalDTO.getDescription());
        existingRental.setUpdatedAt(LocalDateTime.now());

        // Vérifier et gérer l'upload de l'image si elle est présente
        if (file != null && !file.isEmpty()) {
            try {
                String uploadDirectory = "uploads/rentals/";
                Path path = Paths.get(uploadDirectory).toAbsolutePath();

                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }

                String fileExtension = getFileExtension(file.getOriginalFilename());
                String uniqueFileName = UUID.randomUUID() + "." + fileExtension;
                Path filePath = path.resolve(uniqueFileName);

                file.transferTo(filePath.toFile());
                String relativePath = uploadDirectory + uniqueFileName;

                existingRental.setPicture(relativePath); // Mettre à jour avec le nouveau chemin
            } catch (Exception e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        // Sauvegarder le rental mis à jour
        Rentals updatedRental = rentalRepository.save(existingRental);

        return new Rentals();
        // Retourner un RentalDTO
      /*  return new RentalDTO(
                updatedRental.getId(),
                updatedRental.getName(),
                updatedRental.getSurface(),
                updatedRental.getPrice(),
                updatedRental.getPicture(),
                updatedRental.getDescription(),
                updatedRental.getOwner().getId(),
                updatedRental.getCreatedAt(),
                updatedRental.getUpdatedAt()
        );*/
    }
}
