package com.chatop.controllers;

import com.chatop.dtos.UserRequestDTO;
import com.chatop.dtos.UserResponseDTO;
import com.chatop.services.UserService;
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

    // Endpoint pour l'enregistrement
    @PostMapping("auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserRequestDTO userDTO) {
        String token = userService.register(userDTO);
        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "register failed"));
        }
    }

    @PostMapping("auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequestDTO userDTO) {
        String token = userService.login(userDTO);

        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants incorrects"));
        }
    }


    // Endpoint pour obtenir les informations de l'utilisateur connecté
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("auth/me")
    public ResponseEntity<UserResponseDTO> getUserDetails(Authentication authentication) {
        String email = authentication.getName();  // Récupère l'email de l'utilisateur connecté
        UserResponseDTO user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
}
