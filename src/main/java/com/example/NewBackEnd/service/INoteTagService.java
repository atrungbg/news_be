package com.example.NewBackEnd.service;

import com.example.NewBackEnd.controller.NoteTagController;
import com.example.NewBackEnd.dto.request.note_tag.CreateNoteTagRequest;
import com.example.NewBackEnd.dto.request.note_tag.UpdateNoteTagRequest;
import com.example.NewBackEnd.dto.response.notetag.NoteTagResponse;
import com.example.NewBackEnd.exception.BaseException;

import java.util.UUID;

public interface INoteTagService extends IGenericService<NoteTagResponse>{
    NoteTagResponse createNoteTag(CreateNoteTagRequest createNoteTagRequest) throws BaseException;
    NoteTagResponse updateNoteTag(UUID id , UpdateNoteTagRequest updateNoteTagRequest) throws BaseException;
    boolean deleteNoteTag(UUID id) throws BaseException;
}
