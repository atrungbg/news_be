package com.example.NewBackEnd.dto.request.note;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UpdateNoteRequest {
    private String title;        // Tiêu đề mới của note
    private String content;      // Nội dung mới của note
    private Boolean isPinned;    // Cập nhật trạng thái ghim
}
