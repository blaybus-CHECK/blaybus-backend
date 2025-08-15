package com.blaybus.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

@RestController
public class FileUploadController {

    @PostMapping("/api/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("파일 크기: " + file.getSize());
        final String DIR_NAME = "static";
        try {
            // 파일 확장자 확인
            String fileName = requireNonNull(file.getOriginalFilename());
            if (fileName == null || !fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다. 현재 파일명: " + fileName);
            }

            // MIME 타입 확인
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다. 현재 MIME 타입: " + contentType);
            }

            // 폴더 참조 및 생성
            File dir = new File(DIR_NAME);
            // 폴더 없는 경우 폴더 생성
            if (!dir.exists()) dir.mkdirs();

            Path path = Paths.get(dir.getAbsolutePath(), file.getOriginalFilename());
            file.transferTo(path);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
        }
    }
}
