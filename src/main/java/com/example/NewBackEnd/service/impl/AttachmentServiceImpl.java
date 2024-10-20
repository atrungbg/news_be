//package com.example.NewBackEnd.service.impl;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import com.example.NewBackEnd.config.CloudinaryConfig;
//import com.example.NewBackEnd.constant.ConstError;
//import com.example.NewBackEnd.dto.request.attachment.AttachmentRequest;
//import com.example.NewBackEnd.dto.response.attachment.AttachmentResponse;
//import com.example.NewBackEnd.entity.Attachment;
//import com.example.NewBackEnd.entity.Note;
//import com.example.NewBackEnd.entity.Tag;
//import com.example.NewBackEnd.exception.BaseException;
//import com.example.NewBackEnd.enums.ErrorCode;
//import com.example.NewBackEnd.model.PagingModel;
//import com.example.NewBackEnd.repository.AttachmentRepository;
//import com.example.NewBackEnd.constant.ConstStatus;
//import com.example.NewBackEnd.repository.NoteRepository;
//import com.example.NewBackEnd.repository.TagRepository;
//import com.example.NewBackEnd.service.IAttachmentService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.sql.Blob;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class AttachmentServiceImpl implements IAttachmentService {
//
//    @Autowired
//    private AttachmentRepository attachmentRepository;
//
//    @Autowired
//    private NoteRepository noteRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    private final Cloudinary cloudinary;
//
//    @Autowired
//    public AttachmentServiceImpl(Cloudinary cloudinary) {
//        this.cloudinary = cloudinary;
//    }
//
//    private int totalItems() {
//        return (int) attachmentRepository.count();
//    }
//
//    private int totalActiveItems() {
//        return attachmentRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
//    }
//
//    @Override
//    public AttachmentResponse createAttachment(AttachmentRequest attachmentRequest) throws BaseException {
//        try {
//            if (attachmentRequest.getImageBase64() == null || attachmentRequest.getImageBase64().isEmpty()) {
//                throw new BaseException(ErrorCode.ERROR_400.getCode(), "Image data is required", "Image data missing");
//            }
//            Note note = noteRepository.findById(attachmentRequest.getNoteId())
//                    .orElseThrow(() -> new BaseException(
//                            ErrorCode.ERROR_404.getCode(),
//                            ConstError.Tag.TAG_NOT_FOUND,
//                            ErrorCode.ERROR_404.getMessage()
//                    ));
//            String imageUrl = uploadImageToCloudinary(attachmentRequest.getImageBase64());
//            Attachment attachment = new Attachment();
//            attachment.setStatus(ConstStatus.ACTIVE_STATUS);
//            attachment.setNote(note);
//            attachment.setFileUrl(imageUrl);
//            attachmentRepository.save(attachment);
//
//            return modelMapper.map(attachment, AttachmentResponse.class);
//        } catch (Exception e) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }
//
//    public String uploadImageToCloudinary(String base64Image) throws IOException {
//        byte[] decodedImage = java.util.Base64.getDecoder().decode(base64Image);
//
//        Map<?, ?> uploadResult = cloudinary.uploader().upload(decodedImage,
//                ObjectUtils.asMap("resource_type", "image"));
//
//        return uploadResult.get("secure_url").toString();
//    }
//
//
//    @Override
//    public AttachmentResponse updateAttachment(UUID id, AttachmentRequest attachmentRequest) throws BaseException {
//        try {
//            Attachment attachment = attachmentRepository.findById(id)
//                    .orElseThrow(() -> new BaseException(
//                            ErrorCode.ERROR_404.getCode(),
//                            "Attachment not found",
//                            ErrorCode.ERROR_404.getMessage()
//                    ));
//            modelMapper.map(attachmentRequest, attachment); // Cập nhật thông tin từ request vào entity
//            attachmentRepository.save(attachment);
//            return modelMapper.map(attachment, AttachmentResponse.class);
//        } catch (Exception e) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }
//
//    @Override
//    public boolean deleteAttachment(UUID id) throws BaseException {
//        try {
//            Attachment attachment = attachmentRepository.findById(id)
//                    .orElseThrow(() -> new BaseException(
//                            ErrorCode.ERROR_404.getCode(),
//                            "Attachment not found",
//                            ErrorCode.ERROR_404.getMessage()
//                    ));
//            attachment.setStatus(ConstStatus.INACTIVE_STATUS); // Xóa mềm bằng cách chuyển trạng thái
//            attachmentRepository.save(attachment);
//            return true;
//        } catch (Exception e) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }
//
//    @Override
//    public AttachmentResponse findById(UUID id) throws BaseException {
//        try {
//            Attachment attachment = attachmentRepository.findById(id)
//                    .orElseThrow(() -> new BaseException(
//                            ErrorCode.ERROR_404.getCode(),
//                            "Attachment not found",
//                            ErrorCode.ERROR_404.getMessage()
//                    ));
//            return modelMapper.map(attachment, AttachmentResponse.class);
//        } catch (Exception e) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }
//
//    @Override
//    public PagingModel<AttachmentResponse> getAll(Integer page, Integer limit) throws BaseException {
//        try {
//            if (page == null || limit == null) {
//                page = 1;
//                limit = 10;
//            }
//            Pageable pageable = PageRequest.of(page - 1, limit);
//            List<Attachment> attachments = attachmentRepository.findAllByOrderByCreatedDate(pageable);
//            List<AttachmentResponse> responses = attachments.stream()
//                    .map(attachment -> modelMapper.map(attachment, AttachmentResponse.class))
//                    .collect(Collectors.toList());
//
//            PagingModel<AttachmentResponse> result = new PagingModel<>();
//            result.setPage(page);
//            result.setLimit(limit);
//            result.setTotalPage((int) Math.ceil((double) totalItems() / limit));
//            result.setListResult(responses);
//
//            return result;
//        } catch (Exception e) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }
//
//    @Override
//    public PagingModel<AttachmentResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
//        try {
//            if (page == null || limit == null) {
//                page = 1;
//                limit = 10;
//            }
//            Pageable pageable = PageRequest.of(page - 1, limit);
//            List<Attachment> attachments = attachmentRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
//            List<AttachmentResponse> responses = attachments.stream()
//                    .map(attachment -> modelMapper.map(attachment, AttachmentResponse.class))
//                    .collect(Collectors.toList());
//
//            PagingModel<AttachmentResponse> result = new PagingModel<>();
//            result.setPage(page);
//            result.setLimit(limit);
//            result.setTotalPage((int) Math.ceil((double) totalActiveItems() / limit));
//            result.setListResult(responses);
//
//            return result;
//        } catch (Exception e) {
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }
//}
