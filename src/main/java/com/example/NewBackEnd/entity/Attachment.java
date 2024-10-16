package com.example.NewBackEnd.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "Attachments")
public class Attachment extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "noteId", nullable = false)
    private Note note;

    @Column(nullable = false, length = 255)
    private String fileUrl;
}
