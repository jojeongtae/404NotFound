package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.UploadImageEntity;
import com.example.notfound_backend.data.repository.UploadedImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadImageDAO { // DB 저장 전담
    private final UploadedImageRepository uploadedImageRepository;

    public UploadImageEntity uploadImage(UploadImageEntity entity) {
        return uploadedImageRepository.save(entity);
    }


}
