package com.example.NewBackEnd.repository;

import com.example.NewBackEnd.entity.Tag;
import com.example.NewBackEnd.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    List<Tag> findAllByOrderByCreatedDate(Pageable pageable);
    List<Tag> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
    int countByStatus(String status);
}
