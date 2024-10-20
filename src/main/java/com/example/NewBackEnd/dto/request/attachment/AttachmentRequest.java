package com.example.NewBackEnd.dto.request.attachment;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AttachmentRequest {
    private String imageBase64;
    private UUID noteId;
}
