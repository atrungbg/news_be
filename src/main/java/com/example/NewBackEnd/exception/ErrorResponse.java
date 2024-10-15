package com.example.NewBackEnd.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int errorCode;
    private String errorMessage;
    private String errorStatus;

    public ErrorResponse(int errorCode, String errorMessage, String errorStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorStatus = errorStatus;
    }
}
