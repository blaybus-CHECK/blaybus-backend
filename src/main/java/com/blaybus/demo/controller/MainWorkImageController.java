package com.blaybus.demo.controller;

import com.blaybus.demo.entity.MainWork;
import com.blaybus.demo.entity.MainWorkImage;
import com.blaybus.demo.entity.MainWorkRepository;
import com.blaybus.demo.result.ArrayCarrier;
import com.blaybus.demo.view.MainWorkImageView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.*;

@RestController
public record MainWorkImageController(
    MainWorkRepository repository
) {


    @PostMapping("/api/main-work/{mainWorkId}/images")
    ResponseEntity<?> mainWorkImage(@PathVariable("mainWorkId") String mainWorkId, @RequestPart("files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            if (!isFileValid(file)) {
                return ResponseEntity.badRequest().build(); // 5 MB limit
            }
        }
        if (files.length == 0 || files.length > 4) {
            return ResponseEntity.badRequest().build();
        }
        // mainWorkId가 UUID 형식인지 확인
        try {
            UUID.fromString(mainWorkId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<MainWork> mainWork = repository.findById(UUID.fromString(mainWorkId));
        if (mainWork.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 폴더 참조 및 생성
        String location = "main-work/" + mainWorkId + "/images";
        File dir = new File(location);
        // 폴더 없는 경우 폴더 생성
        if (!dir.exists()) dir.mkdirs();

        for (MultipartFile file : files) {
            Path path = Paths.get(dir.getAbsolutePath(), file.getOriginalFilename());
            file.transferTo(path);
        }

        URI uri = URI.create(location);
        return ResponseEntity.created(uri).build();
    }

    private static boolean isFileValid(MultipartFile file) throws IOException {
        return isFileTypeValid(file)
            && isFileSizeValid(file);
    }

    private static boolean isFileTypeValid(MultipartFile file) {
        return requireNonNull(file.getOriginalFilename()).endsWith(".jpg") && (file.getOriginalFilename().endsWith(".jpeg") || !file.getOriginalFilename().endsWith("png"));
    }

    private static boolean isFileSizeValid(MultipartFile file) throws IOException {
        return file.getBytes().length <= 1024 * 1024 * 5;
    }
}
