package com.example.NewBackEnd.service.impl;

import com.example.NewBackEnd.constant.ConstError;
import com.example.NewBackEnd.constant.ConstStatus;
import com.example.NewBackEnd.dto.request.note_tag.CreateNoteTagRequest;
import com.example.NewBackEnd.dto.request.note_tag.UpdateNoteTagRequest;
import com.example.NewBackEnd.dto.response.notetag.NoteTagResponse;
import com.example.NewBackEnd.entity.Note;
import com.example.NewBackEnd.entity.Tag;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.entity.NoteTag; // Giả sử bạn có entity này
import com.example.NewBackEnd.enums.ErrorCode;
import com.example.NewBackEnd.repository.NoteRepository;
import com.example.NewBackEnd.repository.NoteTagRepository;
import com.example.NewBackEnd.repository.TagRepository;
import com.example.NewBackEnd.service.INoteTagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NoteTagServiceImpl implements INoteTagService {

    @Autowired
    private NoteTagRepository noteTagRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    private int totalItem() {
        return (int) noteTagRepository.count();
    }

    private int totalItemWithStatusActive() {
        return  noteTagRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public NoteTagResponse findById(UUID id) throws BaseException {
        try {
            NoteTag noteTag = noteTagRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.NoteTag.NOTE_TAG_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));
            return modelMapper.map(noteTag, NoteTagResponse.class);
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<NoteTagResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || limit == null) {
                page = 1;
                limit = 10;
            }
            PagingModel<NoteTagResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<NoteTag> noteTags = noteTagRepository.findAllByOrderByCreatedDate(pageable);
            List<NoteTagResponse> noteTagResponses = noteTags.stream()
                    .map(noteTag -> modelMapper.map(noteTag, NoteTagResponse.class))
                    .collect(Collectors.toList());
            result.setListResult(noteTagResponses);
            result.setTotalPage((int) Math.ceil((double) noteTagRepository.count() / limit));
            result.setLimit(limit);
            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<NoteTagResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || limit == null) {
                page = 1;
                limit = 10;
            }
            PagingModel<NoteTagResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<NoteTag> noteTags = noteTagRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS,pageable);
            List<NoteTagResponse> noteTagResponses = noteTags.stream()
                    .map(noteTag -> modelMapper.map(noteTag, NoteTagResponse.class))
                    .collect(Collectors.toList());
            result.setListResult(noteTagResponses);
            result.setTotalPage((int) Math.ceil((double) totalItemWithStatusActive() / limit));
            result.setLimit(limit);
            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public NoteTagResponse createNoteTag(CreateNoteTagRequest createNoteTagRequest) throws BaseException {
        try {
            // Tạo mới đối tượng NoteTag
            NoteTag noteTag = new NoteTag();
            noteTag.setStatus(ConstStatus.ACTIVE_STATUS);

            // Thiết lập Note
            if (createNoteTagRequest.getNoteId() != null) {
                Note note = noteRepository.findById(createNoteTagRequest.getNoteId())
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_404.getCode(),
                                ConstError.Note.NOTE_NOT_FOUND,
                                ErrorCode.ERROR_404.getMessage()
                        ));
                noteTag.setNote(note);
            }

            // Thiết lập Tag
            if (createNoteTagRequest.getTagId() != null) {
                Tag tag = tagRepository.findById(createNoteTagRequest.getTagId())
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_404.getCode(),
                                ConstError.Tag.TAG_NOT_FOUND,
                                ErrorCode.ERROR_404.getMessage()
                        ));
                noteTag.setTag(tag);
            }

            // Lưu NoteTag
            noteTagRepository.save(noteTag);
            return modelMapper.map(noteTag, NoteTagResponse.class);
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }



    @Override
    public NoteTagResponse updateNoteTag(UUID id, UpdateNoteTagRequest updateNoteTagRequest) throws BaseException {
        try {
            NoteTag noteTag = noteTagRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.NoteTag.NOTE_TAG_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));

            if (updateNoteTagRequest.getNoteId() != null) {
                Note note = noteRepository.findById(updateNoteTagRequest.getNoteId())
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_404.getCode(),
                                ConstError.Note.NOTE_NOT_FOUND,
                                ErrorCode.ERROR_404.getMessage()
                        ));
                noteTag.setNote(note);
            }

            if (updateNoteTagRequest.getTagId() != null) {
                Tag tag = tagRepository.findById(updateNoteTagRequest.getTagId())
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_404.getCode(),
                                ConstError.Tag.TAG_NOT_FOUND,
                                ErrorCode.ERROR_404.getMessage()
                        ));
                noteTag.setTag(tag);
            }
            noteTagRepository.save(noteTag);
            return modelMapper.map(noteTag, NoteTagResponse.class);
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


    @Override
    public boolean deleteNoteTag(UUID id) throws BaseException {
        try {
            NoteTag noteTag = noteTagRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.NoteTag.NOTE_TAG_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));
            noteTag.setStatus(ConstStatus.INACTIVE_STATUS);
            noteTagRepository.save(noteTag);
            return true;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
