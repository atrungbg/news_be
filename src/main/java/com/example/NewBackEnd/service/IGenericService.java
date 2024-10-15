package com.example.NewBackEnd.service;


import com.example.NewBackEnd.exception.BaseException;
import com.example.NewBackEnd.model.PagingModel;

import java.util.UUID;

public interface IGenericService<T> {
    T findById(UUID id) throws BaseException;

    PagingModel<T> getAll(Integer page, Integer limit) throws BaseException;

    PagingModel<T> findAllByStatusTrue(Integer page, Integer limit) throws BaseException;
}
