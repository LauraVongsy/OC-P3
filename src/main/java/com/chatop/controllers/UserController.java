package com.chatop.controllers;

import com.chatop.dtos.UserDTO;
import com.chatop.models.Users;
import com.chatop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    // Endpoint pour l'enregistrement
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserDTO userDTO) {
        String token = userService.register(userDTO);
        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "register failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO);

        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants incorrects"));
        }
    }


    // Endpoint pour obtenir les informations de l'utilisateur connecté
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/me")
    public ResponseEntity<Users> getUserDetails(Authentication authentication) {
        String email = authentication.getName();  // Récupère l'email de l'utilisateur connecté
        Users user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
