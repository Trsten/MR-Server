package com.uniza.mr.exception;

public class MRFileException extends Exception {
    public MRFileException(String message, String detail) {
        super(message);
    }
    //TODO: vypisať detail pri vyhodeny vinimky
}
