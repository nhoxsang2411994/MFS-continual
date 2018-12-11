package com.dxc.minifilesharing.file.advice;

import com.dxc.minifilesharing.file.common.FileServiceError;
import com.dxc.minifilesharing.file.exception.FileServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionControllerAdvice {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> exceptionHandler(Exception ex) {
        LOGGER.error("Un-expected error. Please contact your administrator.", ex);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(messageSourceAccessor.getMessage(FileServiceError.UNEXPECTED.name()),
                httpHeaders, FileServiceError.UNEXPECTED.getHttpStatus());
    }

    ResponseEntity<String> fileServiceException(FileServiceException ex) {
        FileServiceError response = ex.getResponse();
        String msgCode = response.name();
        String message = messageSourceAccessor.getMessage(msgCode, ex.getParameters().toArray(), msgCode);
        LOGGER.info(message, ex.getCause() == null? ex : ex.getCause());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        return new ResponseEntity<>(message, httpHeaders, response.getHttpStatus());
    }
}
