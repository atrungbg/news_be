package com.example.NewBackEnd.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends Exception{
    private int errorCode;
    private String errorMessage;
    private String errorStatus;

    public BaseException(int errorCode, String errorMessage, String errorStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorStatus = errorStatus;
    }
}
