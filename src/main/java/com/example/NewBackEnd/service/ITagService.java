package com.example.NewBackEnd.service;

import com.example.NewBackEnd.dto.request.tag.CreateTagRequest;
import com.example.NewBackEnd.dto.request.tag.UpdateTagRequest;
import com.example.NewBackEnd.dto.response.tag.TagResponse;
import com.example.NewBackEnd.exception.BaseException;

import java.util.UUID;

public interface ITagService extends IGenericService<TagResponse>{
    TagResponse createTag(CreateTagRequest createTagRequest) throws BaseException;
    TagResponse updateTag(UUID id, UpdateTagRequest updateTagRequest) throws BaseException;
    boolean deleteTag(UUID id) throws BaseException;
}
