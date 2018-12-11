package com.dxc.minifilesharing.file.common;

import org.springframework.http.HttpStatus;

public enum FileServiceError {
    UNEXPECTED(0, HttpStatus.INTERNAL_SERVER_ERROR),
    LONG_ID_INVALID(1002, HttpStatus.BAD_REQUEST),
    NOT_FOUND(2000, HttpStatus.NOT_FOUND),
    MARKED_DELETE(2001, HttpStatus.BAD_REQUEST),
    INPUT_FILE_INVALID(2002, HttpStatus.BAD_REQUEST),
    FILENAME_INVALID(1002,HttpStatus.BAD_REQUEST),
    UPLOAD_FAILED(1001, HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final HttpStatus httpStatus;

    FileServiceError(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
