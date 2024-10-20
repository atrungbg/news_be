package com.example.NewBackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "Tags")
public class Tag extends BaseEntity{
    @Column(nullable = false, unique = true, length = 100)
    private String tagName;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<NoteTag> noteTags;
}