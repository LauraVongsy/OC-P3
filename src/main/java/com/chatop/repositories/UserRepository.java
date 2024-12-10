package com.chatop.repositories;

import com.chatop.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    /**
     * Specialized function used to retrieve a User based on parameter
     *
     * @param email Email of the user
     * @return User if user exists
     */
    Optional<Users> findByEmail(String email);
}
