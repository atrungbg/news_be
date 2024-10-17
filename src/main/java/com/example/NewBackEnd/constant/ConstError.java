package com.example.NewBackEnd.constant;

public class ConstError {
    public static class Input{
        public static final String NO_INPUT = "No input";
    }
    public static class User {
        public static final String USER_NOT_FOUND = "User not found";
        public static final String USERNAME_EXISTED = "Username existed";
        public static final String EMAIL_EXISTED = "Email existed";
        public static final String INVALID_AUTHORIZATION_HEADER = "Invalid authorization header";
        public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    }

    public static class Role {
        public static final String ROLE_NOT_FOUND = "Role not found";
    }

    public static class Tag {
        public static final String TAG_NOT_FOUND = "Tag not found";
    }

    public static class Note {
        public static final String NOTE_NOT_FOUND = "Note not found";
    }

    public static class NoteTag {
        public static final String NOTE_TAG_NOT_FOUND = "Note tag not found";
    }

}
