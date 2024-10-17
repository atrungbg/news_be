package com.example.NewBackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Notes")
public class Note extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "isPinned")
    private Boolean isPinned = false;

    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private List<NoteTag> noteTags;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

}