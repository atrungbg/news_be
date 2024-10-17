package com.example.NewBackEnd.dto.response.notetag;

import com.example.NewBackEnd.dto.response.BaseResponse;
import com.example.NewBackEnd.dto.response.note.NoteResponse;
import com.example.NewBackEnd.dto.response.tag.TagResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteTagResponse extends BaseResponse {
    private NoteResponse note;
    private TagResponse tag;
}
