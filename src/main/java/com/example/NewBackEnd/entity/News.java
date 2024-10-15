package com.example.NewBackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "News")
public class News extends BaseEntity{
    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 500)
    private String summary;

    @Column(name = "read_count")
    private int readCount;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    private List<NewsSection> sections;
}
