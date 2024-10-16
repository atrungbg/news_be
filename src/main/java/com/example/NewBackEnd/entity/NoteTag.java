package com.example.NewBackEnd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Entity
@Table(name = "NoteTags")
public class NoteTag extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "noteId", nullable = false)
    private Note note;

    @ManyToOne
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;
}
