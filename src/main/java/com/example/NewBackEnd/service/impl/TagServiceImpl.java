package com.example.NewBackEnd.service.impl;

import com.example.NewBackEnd.constant.ConstError;
import com.example.NewBackEnd.constant.ConstStatus;
import com.example.NewBackEnd.dto.request.tag.CreateTagRequest;
import com.example.NewBackEnd.dto.request.tag.UpdateTagRequest;
import com.example.NewBackEnd.dto.response.tag.TagResponse;
import com.example.NewBackEnd.dto.response.user.UserResponse;
import com.example.NewBackEnd.entity.Tag;
import com.example.NewBackEnd.entity.User;
import com.example.NewBackEnd.enums.ErrorCode;
import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;
import com.example.NewBackEnd.repository.TagRepository;
import com.example.NewBackEnd.service.ITagService;
import org.checkerframework.checker.units.qual.A;
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
public class TagServiceImpl implements ITagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    private int totalItem() {
        return (int) tagRepository.count();
    }

    private int totalItemWithStatusActive() {
        return  tagRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public TagResponse findById(UUID id) throws BaseException {
        try{
            Optional<Tag> tag = tagRepository.findById(id);
            boolean tagIsExít = tag.isPresent();
            if (!tagIsExít) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Tag.TAG_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }
            return modelMapper.map(tag.get(), TagResponse.class);
        }catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<TagResponse> getAll(Integer page, Integer limit) throws BaseException {
        try{
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<TagResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Tag> tags = tagRepository.findAllByOrderByCreatedDate(pageable);
            List<TagResponse> tagResponses = tags.stream().map(tag
                    -> modelMapper.map(tag, TagResponse.class)).collect(Collectors.toList());
            result.setListResult(tagResponses);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);
            return result;
        }catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<TagResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try{
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<TagResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);
            List<Tag> tags = tagRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            List<TagResponse> tagResponses = tags.stream().map(tag
                    -> modelMapper.map(tag, TagResponse.class)).collect(Collectors.toList());
            result.setListResult(tagResponses);
            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);
            return result;
        }catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public TagResponse createTag(CreateTagRequest createTagRequest) throws BaseException {
        try {
            Tag tag = modelMapper.map(createTagRequest, Tag.class);
            tag.setStatus(ConstStatus.ACTIVE_STATUS);
            tagRepository.save(tag);
            return modelMapper.map(tag, TagResponse.class);
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public TagResponse updateTag(UUID id, UpdateTagRequest updateTagRequest) throws BaseException {
        try {
            // Lấy đối tượng Tag từ repository
            Tag tag = tagRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.Tag.TAG_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));

            // Kiểm tra và cập nhật trường tagName nếu không null hoặc không rỗng
            if (updateTagRequest.getTagName() != null && !updateTagRequest.getTagName().isEmpty()) {
                tag.setTagName(updateTagRequest.getTagName());
            }

            // Lưu lại đối tượng đã cập nhật
            tagRepository.save(tag);

            // Chuyển đổi đối tượng Tag thành TagResponse
            return modelMapper.map(tag, TagResponse.class);
        } catch (Exception e) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    e.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }


    @Override
    public boolean deleteTag(UUID id) throws BaseException {
            try {
                Tag tag = tagRepository.findById(id)
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_500.getCode(),
                                ConstError.Tag.TAG_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()
                        ));
                tag.setStatus(ConstStatus.INACTIVE_STATUS);
                tagRepository.save(tag);
                return true;
            } catch (Exception baseException) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
            }
    }
}
