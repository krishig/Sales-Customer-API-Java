package com.KrishiG.exception;

public class MethodArgumentNotValidException extends  RuntimeException{

    public MethodArgumentNotValidException(String msg)
    {
        super(msg);
    }
}
