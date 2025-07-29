package com.example.notfound_backend.controller.utility;

import com.example.notfound_backend.data.dto.utility.UploadImageDTO;
import com.example.notfound_backend.service.utility.UploadImageService;
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

    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<UploadImageDTO> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        UploadImageDTO result = uploadImageService.uploadImage(file);
        return ResponseEntity.ok().body(result);
    }




}
