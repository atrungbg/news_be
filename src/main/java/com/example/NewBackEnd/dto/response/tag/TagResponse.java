package com.example.NewBackEnd.dto.response.tag;

import com.example.NewBackEnd.dto.response.BaseResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponse extends BaseResponse {
    private UUID id;
    private String tagName;
//    private List<NoteTag> noteTags;
}
