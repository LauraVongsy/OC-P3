package com.chatop.services;

import com.chatop.dtos.UserDTO;
import com.chatop.models.Users;
import com.chatop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import utils.JwtUtils;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return "Cet email est déjà utilisé";
        }

        Users user = new Users();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));


        userRepository.save(user);
        return "Utilisateur enregistré avec succès";
    }

    public String login(UserDTO userDTO) {
        Optional<Users> userOptional = userRepository.findByEmail(userDTO.getLogin());

        if (userOptional.isPresent()) {
            Users user = userOptional.get();

            // Vérifier si le mot de passe est correct
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                // Si le mot de passe est correct, retourner un jeton ou un message de succès
                return JwtUtils.generateToken(user.getEmail());
            } else {
                return "Mot de passe incorrect";
            }
        } else {
            return "Utilisateur non trouvé";
        }
    }
    public Users findByEmail(String email) {
        System.out.println(email);
        Optional<Users> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null);
    }
}


