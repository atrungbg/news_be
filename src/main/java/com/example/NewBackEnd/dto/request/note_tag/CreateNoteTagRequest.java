package com.example.NewBackEnd.dto.request.note_tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNoteTagRequest {
    private UUID noteId;
    private UUID tagId;
}
