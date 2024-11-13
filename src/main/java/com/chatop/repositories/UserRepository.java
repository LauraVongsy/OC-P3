package com.chatop.repositories;

import com.chatop.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
   Users findByName(String name);
   Optional<Users> findByEmail(String email);
}
