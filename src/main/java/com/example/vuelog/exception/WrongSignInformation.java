package com.example.vuelog.exception;

public class WrongSignInformation extends RootException{

    private static final String EXCEPTION_MESSAGE = "id or passwd not correct";

    public WrongSignInformation() {
        super(EXCEPTION_MESSAGE);
    }

    public WrongSignInformation(String fieldName, String message) {
        super(EXCEPTION_MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
