package com.example.NewBackEnd.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagingModel<T> {

    private int page;
    private int totalPage;
    private int limit;
    private List<T> listResult = new ArrayList<>();


}
