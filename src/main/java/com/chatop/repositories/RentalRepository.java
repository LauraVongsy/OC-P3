package com.chatop.repositories;

import com.chatop.models.Rentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rentals, Integer> {
    Rentals findByName(String name);
}
