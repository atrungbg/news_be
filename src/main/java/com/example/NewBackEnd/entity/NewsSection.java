package com.example.NewBackEnd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "NewsSection")
public class NewsSection extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    private String content;
    private String imageUrl;
}
