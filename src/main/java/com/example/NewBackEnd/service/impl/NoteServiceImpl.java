package com.example.NewBackEnd.service.impl;

import com.example.NewBackEnd.constant.ConstError;
import com.example.NewBackEnd.constant.ConstStatus;
import com.example.NewBackEnd.dto.request.note.CreateNoteRequest;
import com.example.NewBackEnd.dto.request.note.UpdateNoteRequest;
import com.example.NewBackEnd.dto.response.note.NoteResponse;
import com.example.NewBackEnd.entity.Note;
import com.example.NewBackEnd.enums.ErrorCode;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.repository.NoteRepository;
import com.example.NewBackEnd.repository.UserRepository;
import com.example.NewBackEnd.service.INoteService;
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
public class NoteServiceImpl implements INoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private int totalItems() {
        return (int) noteRepository.count();
    }

    private int totalActiveItems() {
        return noteRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public NoteResponse findById(UUID id) throws BaseException {
        try {
            Note note = noteRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.Note.NOTE_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));
            return modelMapper.map(note, NoteResponse.class);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<NoteResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || limit == null) {
                page = 1;
                limit = 10;
            }
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Note> notes = noteRepository.findAllByOrderByCreatedDate(pageable);
            List<NoteResponse> noteResponses = notes.stream()
                    .map(note -> modelMapper.map(note, NoteResponse.class))
                    .collect(Collectors.toList());

            PagingModel<NoteResponse> result = new PagingModel<>();
            result.setPage(page);
            result.setListResult(noteResponses);
            result.setTotalPage((int) Math.ceil((double) totalItems() / limit));
            result.setLimit(limit);
            return result;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<NoteResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || limit == null) {
                page = 1;
                limit = 10;
            }
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Note> notes = noteRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            List<NoteResponse> noteResponses = notes.stream()
                    .map(note -> modelMapper.map(note, NoteResponse.class))
                    .collect(Collectors.toList());

            PagingModel<NoteResponse> result = new PagingModel<>();
            result.setPage(page);
            result.setListResult(noteResponses);
            result.setTotalPage((int) Math.ceil((double) totalActiveItems() / limit));
            result.setLimit(limit);
            return result;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public NoteResponse createNote(CreateNoteRequest createNoteRequest) throws BaseException {
        try {
            Note note = modelMapper.map(createNoteRequest, Note.class);
            note.setStatus(ConstStatus.ACTIVE_STATUS);
            note.setUser(userRepository.findById(createNoteRequest.getUserId())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.User.USER_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    )));
            noteRepository.save(note);
            return modelMapper.map(note, NoteResponse.class);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public NoteResponse updateNote(UUID id, UpdateNoteRequest updateNoteRequest) throws BaseException {
        try {
            // Lấy đối tượng note từ repository
            Note note = noteRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.Note.NOTE_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));

            // Kiểm tra và cập nhật các trường nếu không null hoặc không rỗng
            if (updateNoteRequest.getTitle() != null && !updateNoteRequest.getTitle().isEmpty()) {
                note.setTitle(updateNoteRequest.getTitle());
            }
            if (updateNoteRequest.getContent() != null && !updateNoteRequest.getContent().isEmpty()) {
                note.setContent(updateNoteRequest.getContent());
            }
            if (updateNoteRequest.getIsPinned() != null) {
                note.setIsPinned(updateNoteRequest.getIsPinned());
            }

            // Lưu lại đối tượng đã cập nhật
            noteRepository.save(note);

            // Chuyển đổi đối tượng Note thành NoteResponse
            return modelMapper.map(note, NoteResponse.class);
        } catch (Exception e) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    e.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }


    @Override
    public boolean deleteNote(UUID id) throws BaseException {
        try {
            Note note = noteRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.Note.NOTE_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));
            note.setStatus(ConstStatus.INACTIVE_STATUS);
            noteRepository.save(note);
            return true;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), e.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
