package com.example.NewBackEnd.service;

import com.example.NewBackEnd.dto.request.attachment.AttachmentRequest;
import com.example.NewBackEnd.dto.request.attachment.UpdateAttachmentRequest;
import com.example.NewBackEnd.dto.request.note.CreateNoteRequest;
import com.example.NewBackEnd.dto.request.note.UpdateNoteRequest;
import com.example.NewBackEnd.dto.response.attachment.AttachmentResponse;
import com.example.NewBackEnd.dto.response.note.NoteResponse;
import com.example.NewBackEnd.exception.BaseException;

import java.util.UUID;

public interface IAttachmentService extends IGenericService<AttachmentResponse> {
    AttachmentResponse createAttachment(AttachmentRequest attachmentRequest) throws BaseException;
    AttachmentResponse updateAttachment(UUID id, UpdateAttachmentRequest updateAttachmentRequest) throws BaseException;
    boolean deleteAttachment(UUID id) throws BaseException;
}
