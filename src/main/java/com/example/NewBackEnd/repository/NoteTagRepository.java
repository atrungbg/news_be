package com.example.NewBackEnd.repository;

import com.example.NewBackEnd.constant.ConstError;
import com.example.NewBackEnd.entity.NoteTag;
import com.example.NewBackEnd.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteTagRepository extends JpaRepository<NoteTag, UUID>{
    List<NoteTag> findAllByOrderByCreatedDate(Pageable pageable);
    List<NoteTag> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
    int countByStatus(String status);
}
