package com.example.NewBackEnd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Tags")
public class Tag extends BaseEntity{
    @Column(nullable = false, unique = true, length = 100)
    private String tagName;
}
