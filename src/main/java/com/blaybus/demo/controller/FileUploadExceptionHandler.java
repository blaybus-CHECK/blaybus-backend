package com.blaybus.demo.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionHandler {

    public static final String ERROR_THE_REQUEST_WAS_REJECTED_BECAUSE_NO_MULTIPART_BOUNDARY_WAS_FOUND = "error: the request was rejected because no multipart boundary was found";

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<?> handle(FileUploadException e) {
        if(e.getMessage().equals(ERROR_THE_REQUEST_WAS_REJECTED_BECAUSE_NO_MULTIPART_BOUNDARY_WAS_FOUND)) {
            return ResponseEntity.badRequest().build();
        } else return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
