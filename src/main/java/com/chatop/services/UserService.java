package com.chatop.services;

import com.chatop.dtos.UserRequestDTO;
import com.chatop.dtos.UserResponseDTO;
import com.chatop.exceptions.NotFoundException;
import com.chatop.mapper.UsersMapper;
import com.chatop.models.UserPrincipal;
import com.chatop.models.Users;
import com.chatop.repositories.UserRepository;
import com.chatop.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implements UserDetailsService providing a method to load user details from the database.
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsersMapper usersMapper;

    /**
     * Method used within Spring Security to retrieve user details from the database using email.
     * If user exists, returns the details wrapped in a UserPrincipal object for Spring Security authentication process.
     *
     * @param email Email of the user
     * @return User if found in database
     * @throws UsernameNotFoundException User not found exception from Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(email);
        return user.map(UserPrincipal::new).orElseThrow(() -> new UsernameNotFoundException("User email not found " + email));
    }

    public String register(UserRequestDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return "Cet email est déjà utilisé";
        }

        Users user = new Users();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
        return jwtService.generateToken(userDTO.getEmail(), user.getId());
    }

    public String login(UserRequestDTO userDTO) {
        Optional<Users> userOptional = userRepository.findByEmail(userDTO.getEmail());

        if (userOptional.isPresent()) {
            Users user = userOptional.get();

            // Vérifier si le mot de passe est correct
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                // Si le mot de passe est correct, retourner un jeton ou un message de succès
                return jwtService.generateToken(userDTO.getEmail(), user.getId());
            } else {
                return "Mot de passe incorrect";
            }
        } else {
            return "Utilisateur non trouvé";
        }
    }

    public UserResponseDTO findByEmail(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        System.out.println(user.getId());
        return usersMapper.toResponseDTO(user);

    }

    public UserResponseDTO findById(Integer id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found!"));
        return usersMapper.toResponseDTO(user);
    }

}


