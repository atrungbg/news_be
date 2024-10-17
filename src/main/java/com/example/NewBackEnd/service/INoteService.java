package com.example.NewBackEnd.service;

import com.example.NewBackEnd.dto.request.note.CreateNoteRequest;
import com.example.NewBackEnd.dto.request.note.UpdateNoteRequest;
import com.example.NewBackEnd.dto.response.note.NoteResponse;
import com.example.NewBackEnd.dto.response.user.UserResponse;
import com.example.NewBackEnd.entity.Note;
import com.example.NewBackEnd.exception.BaseException;

import java.util.UUID;

public interface INoteService extends IGenericService<NoteResponse>{
    NoteResponse createNote(CreateNoteRequest createNoteRequest) throws BaseException;
    NoteResponse updateNote(UUID id, UpdateNoteRequest updateNoteRequest) throws BaseException;
    boolean deleteNote(UUID id) throws BaseException;
}
