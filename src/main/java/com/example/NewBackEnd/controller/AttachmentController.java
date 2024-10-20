package com.example.NewBackEnd.controller;

import com.example.NewBackEnd.constant.ConstAPI;
import com.example.NewBackEnd.dto.request.attachment.AttachmentRequest;
import com.example.NewBackEnd.dto.response.attachment.AttachmentResponse;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.service.IAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Attachment Controller")
public class AttachmentController {

    @Autowired
    private IAttachmentService attachmentService;

    @Operation(summary = "Create attachment", description = "API to create a new attachment with a file upload")
    @PostMapping(value = ConstAPI.AttachmentAPI.CREATE_ATTACHMENT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AttachmentResponse createAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("noteId") UUID noteId) throws BaseException, IOException {
        String imageBase64 = java.util.Base64.getEncoder().encodeToString(file.getBytes());
        AttachmentRequest attachmentRequest = new AttachmentRequest();
        attachmentRequest.setImageBase64(imageBase64);
        attachmentRequest.setNoteId(noteId);
        return attachmentService.createAttachment(attachmentRequest);
    }


    @Operation(summary = "Update attachment", description = "API to update an existing attachment")
    @PatchMapping(value = ConstAPI.AttachmentAPI.UPDATE_ATTACHMENT + "{id}")
    public AttachmentResponse updateAttachment(
            @PathVariable("id") UUID id,
            @Valid @RequestBody AttachmentRequest attachmentRequest) throws BaseException {
        return attachmentService.updateAttachment(id, attachmentRequest);
    }

    @Operation(summary = "Delete attachment", description = "API to delete an attachment by setting status to inactive")
    @DeleteMapping(value = ConstAPI.AttachmentAPI.DELETE_ATTACHMENT + "{id}")
    public boolean deleteAttachment(@PathVariable("id") UUID id) throws BaseException {
        return attachmentService.deleteAttachment(id);
    }

    @Operation(summary = "Get attachment by ID", description = "API to retrieve an attachment by its ID")
    @GetMapping(value = ConstAPI.AttachmentAPI.GET_ATTACHMENT_BY_ID + "{id}")
    public AttachmentResponse findById(@PathVariable("id") UUID id) throws BaseException {
        return attachmentService.findById(id);
    }

    @Operation(summary = "Get all attachments", description = "API to get a paginated list of all attachments")
    @GetMapping(value = ConstAPI.AttachmentAPI.GET_ALL_ATTACHMENTS)
    public PagingModel<AttachmentResponse> getAll(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return attachmentService.getAll(page, limit);
    }

    @Operation(summary = "Get all active attachments", description = "API to get all active attachments with pagination")
    @GetMapping(value = ConstAPI.AttachmentAPI.GET_ALL_ATTACHMENTS_BY_STATUS_ACTIVE)
    public PagingModel<AttachmentResponse> findAllByStatusTrue(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return attachmentService.findAllByStatusTrue(page, limit);
    }
}
