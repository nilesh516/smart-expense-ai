package com.nilesh.smartexpense.repository;

import com.nilesh.smartexpense.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Check if email already exists during registration
    boolean existsByEmail(String email);
}