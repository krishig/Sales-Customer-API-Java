package com.KrishiG.exception;

public class SQLSyntaxErrorException extends RuntimeException{
    public SQLSyntaxErrorException(String message){
        super(message);
    }
    public SQLSyntaxErrorException() {
        super();
    }
}
