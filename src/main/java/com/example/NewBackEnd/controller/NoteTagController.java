package com.example.NewBackEnd.controller;

import com.example.NewBackEnd.constant.ConstAPI;
import com.example.NewBackEnd.dto.request.note_tag.CreateNoteTagRequest;
import com.example.NewBackEnd.dto.request.note_tag.UpdateNoteTagRequest;
import com.example.NewBackEnd.dto.response.notetag.NoteTagResponse;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.service.INoteTagService;
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
@Tag(name = "NoteTag Controller")
public class NoteTagController {

    @Autowired
    private INoteTagService noteTagService;

    @Operation(summary = "Create note tag", description = "API to create a new note tag")
    @PostMapping(value = ConstAPI.NoteTagAPI.CREATE_NOTE_TAG)
    public NoteTagResponse createNoteTag(@Valid @RequestBody CreateNoteTagRequest createNoteTagRequest) throws BaseException {
        return noteTagService.createNoteTag(createNoteTagRequest);
    }

    @Operation(summary = "Update note tag", description = "API to update an existing note tag")
    @PatchMapping(value = ConstAPI.NoteTagAPI.UPDATE_NOTE_TAG + "{id}")
    public NoteTagResponse updateNoteTag(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateNoteTagRequest updateNoteTagRequest) throws BaseException {
        return noteTagService.updateNoteTag(id, updateNoteTagRequest);
    }

    @Operation(summary = "Delete note tag", description = "API to delete a note tag by setting status to inactive")
    @DeleteMapping(value = ConstAPI.NoteTagAPI.DELETE_NOTE_TAG + "{id}")
    public boolean deleteNoteTag(@PathVariable("id") UUID id) throws BaseException {
        return noteTagService.deleteNoteTag(id);
    }

    @Operation(summary = "Get note tag by ID", description = "API to retrieve a note tag by its ID")
    @GetMapping(value = ConstAPI.NoteTagAPI.GET_NOTE_TAG_BY_ID + "{id}")
    public NoteTagResponse findById(@PathVariable("id") UUID id) throws BaseException {
        return noteTagService.findById(id);
    }

    @Operation(summary = "Get all note tags", description = "API to get a paginated list of all note tags")
    @GetMapping(value = ConstAPI.NoteTagAPI.GET_ALL_NOTE_TAGS)
    public PagingModel<NoteTagResponse> getAll(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return noteTagService.getAll(page, limit);
    }

    @Operation(summary = "Get all active note tags", description = "API to get all active note tags with pagination")
    @GetMapping(value = ConstAPI.NoteTagAPI.GET_ALL_NOTE_TAGS_BY_STATUS_ACTIVE)
    public PagingModel<NoteTagResponse> findAllByStatusTrue(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return noteTagService.findAllByStatusTrue(page, limit);
    }
}
