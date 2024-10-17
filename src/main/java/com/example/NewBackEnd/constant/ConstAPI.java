package com.example.NewBackEnd.constant;

public class ConstAPI {
    public static class AuthenticationAPI {
        public static final String LOGIN_WITH_PASSWORD_USERNAME = "api/v1/auth/login";
        public static final String LOGIN_WITH_GOOGLE = "api/v1/auth/login-google";
    }

    public static class UserAPI {

        public static final String CREATE_ACCOUNT = "api/v1/account/create";
        public static final String GET_ACCOUNT_BY_ID = "api/v1/account/";
        public static final String GET_ALL_ACCOUNT = "api/v1/account";
        public static  final String GET_ALL_ACCOUNT_ACTIVE = "api/v1/account-active";
        public static  final String UPDATE_USER = "api/v1/account";

    }

    public static class TagAPI {
        public static final String CREATE_TAG = "api/v1/tag";
        public static final String GET_ALL_TAG = "api/v1/tag";
        public static final String GET_ALL_TAG_BY_STATUS_ACTIVE = "api/v1/tag-active";
        public static final String GET_TAG_BY_ID = "api/v1/tag/";
        public static final String DELETE_TAG = "api/v1/tag/";
        public static final String UPDATE_TAG = "api/v1/tag/";
    }

    public static class NoteAPI {
        public static final String CREATE_NOTE = "api/v1/note";
        public static final String GET_ALL_NOTES = "api/v1/note";
        public static final String GET_ALL_NOTES_BY_STATUS_ACTIVE = "api/v1/note-active";
        public static final String GET_NOTE_BY_ID = "api/v1/note/";
        public static final String DELETE_NOTE = "api/v1/note/";
        public static final String UPDATE_NOTE = "api/v1/note/";
    }

    public static class NoteTagAPI {
        public static final String CREATE_NOTE_TAG = "api/v1/note-tag";
        public static final String GET_ALL_NOTE_TAGS = "api/v1/note-tag";
        public static final String GET_ALL_NOTE_TAGS_BY_STATUS_ACTIVE = "api/v1/note-tag-active";
        public static final String GET_NOTE_TAG_BY_ID = "api/v1/note-tag/";
        public static final String DELETE_NOTE_TAG = "api/v1/note-tag/";
        public static final String UPDATE_NOTE_TAG = "api/v1/note-tag/";
    }

}
