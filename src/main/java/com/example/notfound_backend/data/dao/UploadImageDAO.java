package com.example.notfound_backend.service;

import com.example.notfound_backend.data.entity.UploadedImageEntity;
import com.example.notfound_backend.data.repository.UploadedImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageMetadataService { // DB 저장 전담
    private final UploadedImageRepository uploadedImageRepository;

    public void save(UploadedImageEntity entity) {
        uploadedImageRepository.save(entity);
    }


}
