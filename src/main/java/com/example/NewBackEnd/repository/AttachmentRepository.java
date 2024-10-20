//package com.example.NewBackEnd.repository;
//
//import com.example.NewBackEnd.entity.Attachment;
//import com.example.NewBackEnd.entity.NoteTag;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
//    List<Attachment> findAllByOrderByCreatedDate(Pageable pageable);
//    List<Attachment> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);
//    int countByStatus(String status);
//}
