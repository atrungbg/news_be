package com.example.NewBackEnd.dto.response.note;

import com.example.NewBackEnd.dto.response.BaseResponse;
import com.example.NewBackEnd.entity.Attachment;
import com.example.NewBackEnd.entity.Tag;
import com.example.NewBackEnd.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse extends BaseResponse {
    private UUID id;
    private String title;
    private String content;
    private Boolean isPinned = false;
    private UUID userId;
//    private List<UUID> tags;
//    private List<Attachment> attachments;
}
