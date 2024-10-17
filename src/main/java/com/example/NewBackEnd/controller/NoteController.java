package com.example.NewBackEnd.controller;

import com.example.NewBackEnd.constant.ConstAPI;
import com.example.NewBackEnd.dto.request.note.CreateNoteRequest;
import com.example.NewBackEnd.dto.request.note.UpdateNoteRequest;
import com.example.NewBackEnd.dto.response.note.NoteResponse;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.service.INoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Note Controller")
public class NoteController {

    @Autowired
    private INoteService noteService;

    @Operation(summary = "Create note", description = "API to create a new note")
    @PostMapping(value = ConstAPI.NoteAPI.CREATE_NOTE)
    public NoteResponse createNote(@Valid @RequestBody CreateNoteRequest createNoteRequest) throws BaseException {
        return noteService.createNote(createNoteRequest);
    }

    @Operation(summary = "Update note", description = "API to update an existing note")
    @PatchMapping(value = ConstAPI.NoteAPI.UPDATE_NOTE + "{id}")
    public NoteResponse updateNote(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateNoteRequest updateNoteRequest) throws BaseException {
        return noteService.updateNote(id, updateNoteRequest);
    }

    @Operation(summary = "Delete note", description = "API to delete a note by setting status to inactive")
    @DeleteMapping(value = ConstAPI.NoteAPI.DELETE_NOTE + "{id}")
    public boolean deleteNote(@PathVariable("id") UUID id) throws BaseException {
        return noteService.deleteNote(id);
    }

    @Operation(summary = "Get note by ID", description = "API to retrieve a note by its ID")
    @GetMapping(value = ConstAPI.NoteAPI.GET_NOTE_BY_ID + "{id}")
    public NoteResponse findById(@PathVariable("id") UUID id) throws BaseException {
        return noteService.findById(id);
    }

    @Operation(summary = "Get all notes", description = "API to get a paginated list of all notes")
    @GetMapping(value = ConstAPI.NoteAPI.GET_ALL_NOTES)
    public PagingModel<NoteResponse> getAll(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return noteService.getAll(page, limit);
    }

    @Operation(summary = "Get all active notes", description = "API to get all active notes with pagination")
    @GetMapping(value = ConstAPI.NoteAPI.GET_ALL_NOTES_BY_STATUS_ACTIVE)
    public PagingModel<NoteResponse> findAllByStatusTrue(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return noteService.findAllByStatusTrue(page, limit);
    }
}
