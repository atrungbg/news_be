package com.example.NewBackEnd.repository;

import com.example.NewBackEnd.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByOrderByCreatedDate(Pageable pageable);
    List<User> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
    int countByStatus(String status);
}

