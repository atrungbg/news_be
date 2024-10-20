package com.example.NewBackEnd.dto.response.attachment;

import com.example.NewBackEnd.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentResponse extends BaseResponse {
    private String fileUrl;
    private UUID noteId;
}
