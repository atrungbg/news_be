package com.example.NewBackEnd.repository;

import com.example.NewBackEnd.entity.Note;
import com.example.NewBackEnd.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findAllByOrderByCreatedDate(Pageable pageable);
    List<Note> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
    int countByStatus(String status);
}
