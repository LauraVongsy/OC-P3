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

    /**
     * Registers a new user by saving the user details to the database.
     * If the email is already in use, returns a message indicating that the email is already in use.
     *
     * @param userDTO User details to be saved
     * @return Token if registration is successful, message if email is already in use
     */
    public String register(UserRequestDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return "This email is already in use";
        }

        Users user = new Users();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
        return jwtService.generateToken(userDTO.getEmail(), user.getId());
    }

    /**
     * Logs in a user by checking if the email and password match the user details in the database.
     * If the email is not found, returns a message indicating that the user is not found.
     * If the password is incorrect, returns a message indicating that the password is incorrect.
     * If the email and password match, returns a token for the user.
     *
     * @param userDTO User details to be checked
     * @return Token if login is successful, message if user is not found or password is incorrect
     */
    public String login(UserRequestDTO userDTO) {
        Optional<Users> userOptional = userRepository.findByEmail(userDTO.getEmail());

        if (userOptional.isPresent()) {
            Users user = userOptional.get();

            // Checks if the password matches the user's password
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                //If the password is correct, generates a token for the user
                return jwtService.generateToken(userDTO.getEmail(), user.getId());
            } else {
                return "Password incorrect";
            }
        } else {
            return "User not found";
        }
    }

    /**
     * Finds a user by email and returns the user details.
     * If the user is not found, throws a NotFoundException.
     *
     * @param email
     * @return
     */
    public UserResponseDTO findByEmail(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        System.out.println(user.getId());
        return usersMapper.toResponseDTO(user);

    }

    /**
     * Finds a user by id and returns the user details.
     * If the user is not found, throws a NotFoundException.
     *
     * @param id
     * @return
     */
    public UserResponseDTO findById(Integer id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found!"));
        return usersMapper.toResponseDTO(user);
    }

}


