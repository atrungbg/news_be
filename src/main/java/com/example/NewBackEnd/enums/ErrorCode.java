package com.example.NewBackEnd.enums;

public enum ErrorCode {
    SUCCESS(200, "Success"),
    ERROR_400(400, "Bad Request"),
    ERROR_401(401, "Unauthorized"),
    ERROR_403(403, "Forbidden"),
    ERROR_404(404, "Not Found"),
    ERROR_405(405, "Method Not Allowed"),
    ERROR_406(406, "Not Acceptable"),
    ERROR_409(409, "Conflict"),
    ERROR_415(415, "Unsupported Media Type"),
    ERROR_422(422, "Unprocessable Entity"),
    ERROR_429(429, "Too Many Requests"),
    ERROR_500(500, "Internal Server Error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
