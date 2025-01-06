package com.chatop.controllers;

import com.chatop.dtos.UserRequestDTO;
import com.chatop.dtos.UserResponseDTO;
import com.chatop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("auth/register")

    @Operation(description = "Registers a new user", summary = "Registers a new user",
            responses = {@ApiResponse(
                    description = "Successfully registered the user",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRequestDTO.class)),
                            examples = @ExampleObject(value = "{ \"new user\": [ { \"email\": example@example.com, \"name\": \"John Doe\", \"password\": password } ] }")))
            })
    public ResponseEntity<Map<String, String>> register(@RequestBody UserRequestDTO userDTO) {
        String token = userService.register(userDTO);
        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "register failed"));
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("auth/login")
    @Operation(description = "Logs a user", summary = "Logs a user",
            responses = {@ApiResponse(
                    description = "Successfully logged the user",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRequestDTO.class)),
                            examples = @ExampleObject(value = "{ \"user\": [ { \"email\": example@example.com, \"password\": password } ] }")))
            })
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequestDTO userDTO) {
        String token = userService.login(userDTO);

        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants incorrects"));
        }
    }


    // Endpoint pour obtenir les informations de l'utilisateur connecté
    @SecurityRequirement(name = "Bearer Authentication")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("auth/me")
    @Operation(description = "Retrieves informations about the connected user", summary = "Retrieves informations about the connected user",
            responses = {@ApiResponse(
                    description = "Successfully retrieved informations about the connected user",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRequestDTO.class)),
                            examples = @ExampleObject(value = "{ \"user\": [ " +
                                    "{ \"id\": 1, " +
                                    "\"name\": \"John Doe\"," +
                                    " \"email\": \"example@example.com\" }  " +
                                    "\"created_at\": \"2024-12-31\", \"updated_at\": \"2024-12-31\" }]}"
                            )))
            })
    public ResponseEntity<UserResponseDTO> getUserDetails(Authentication authentication) {
        String email = authentication.getName();  // Récupère l'email de l'utilisateur connecté
        UserResponseDTO user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("user/{id}")
    @Operation(description = "Gets a user by it's id", summary = "Gets a user by it's id",
            responses = {@ApiResponse(
                    description = "Successfully retrieved the user by id",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRequestDTO.class)),
                            examples = @ExampleObject(value = "{ \"user\": [ " +
                                    "{ \"id\": 1, " +
                                    "\"name\": \"John Doe\"," +
                                    " \"email\": \"example@example.com\" }  " +
                                    "\"created_at\": \"2024-12-31\", \"updated_at\": \"2024-12-31\" }]}"
                            )))
            })
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        UserResponseDTO user = userService.findById(id);

        return ResponseEntity.ok(user);
    }
}
