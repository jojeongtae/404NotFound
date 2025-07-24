package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.UploadImageDTO;
import com.example.notfound_backend.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class UploadImageController {
    private final UploadImageService uploadImageService;

    @PostMapping("/upload")
    public ResponseEntity<UploadImageDTO> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(uploadImageService.uploadImage(file));
    }

    // 수정, 삭제

}
