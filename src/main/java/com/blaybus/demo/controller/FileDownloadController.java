package com.blaybus.demo.controller;

import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

@RestController
public class FileDownloadController {

    @GetMapping("/api/file/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        final String DIR_NAME = "static";

        File file = new File(DIR_NAME, fileName);

        if(!file.exists() || !fileName.matches(".*\\.(jpg|jpeg|png|gif)$")){
            return ResponseEntity.badRequest().body("해당 파일이 존재하지 않습니다.");
        }

        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        try {
            UrlResource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
