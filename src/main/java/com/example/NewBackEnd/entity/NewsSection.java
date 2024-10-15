package com.example.NewBackEnd.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class NewsSection {
    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    private String content;
    private String imageUrl;
}
