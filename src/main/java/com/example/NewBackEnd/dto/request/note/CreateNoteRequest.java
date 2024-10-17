package com.example.NewBackEnd.dto.request.note;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateNoteRequest {
    private UUID userId;         // ID của user tạo note
    private String title;        // Tiêu đề note
    private String content;      // Nội dung note
    private Boolean isPinned;    // Note có ghim hay không (tuỳ chọn)
}
