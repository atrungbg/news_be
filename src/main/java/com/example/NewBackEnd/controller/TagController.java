package com.example.NewBackEnd.controller;

import com.example.NewBackEnd.constant.ConstAPI;
import com.example.NewBackEnd.dto.request.tag.CreateTagRequest;
import com.example.NewBackEnd.dto.request.tag.UpdateTagRequest;
import com.example.NewBackEnd.dto.response.tag.TagResponse;
import com.example.NewBackEnd.dto.response.user.UserResponse;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.service.ITagService;
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
@Tag(name = "Tag Controller")
public class TagController {

    @Autowired
    private ITagService tagService;

    @Operation(summary = "Create tag", description = "API create tag")
    @PostMapping(value = ConstAPI.TagAPI.CREATE_TAG)
    public TagResponse createTag(@Valid @RequestBody CreateTagRequest createTagRequest) throws BaseException {
        return tagService.createTag(createTagRequest);
    }

    @Operation(summary = "Update tag", description = "API update tag")
    @PatchMapping(value = ConstAPI.TagAPI.UPDATE_TAG + "{id}")
    public TagResponse updateTag(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateTagRequest updateTagRequest) throws BaseException {
        return tagService.updateTag(id, updateTagRequest);
    }


    @Operation(summary = "Delete tag", description = "API delete tag")
    @DeleteMapping(value = ConstAPI.TagAPI.DELETE_TAG + "{id}")
    public boolean deleteTag(@PathVariable("id") UUID id) throws BaseException {
        return tagService.deleteTag(id);
    }

    @Operation(summary = "Get tag by id", description = "API get tag by id")
    @GetMapping(value = ConstAPI.TagAPI.GET_TAG_BY_ID + "{id}")
    public TagResponse findById(@PathVariable("id") UUID id) throws BaseException {
        return tagService.findById(id);
    }

    @Operation(summary = "Get all tag", description = "API get all tag")
    @GetMapping(value = ConstAPI.TagAPI.GET_ALL_TAG)
    public PagingModel<TagResponse> getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return tagService.getAll(page, limit);
    }

    @Operation(summary = "Get all tag by status active", description = "API get all tag by status active")
    @GetMapping(value = ConstAPI.TagAPI.GET_ALL_TAG_BY_STATUS_ACTIVE)
    public PagingModel<TagResponse> findAllByStatusTrue(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return tagService.findAllByStatusTrue(page, limit);
    }

}
