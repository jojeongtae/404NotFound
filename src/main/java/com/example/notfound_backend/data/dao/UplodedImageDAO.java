package com.example.notfound_backend.data.dao;

import com.example.notfound_backend.data.entity.UploadedImageEntity;
import com.example.notfound_backend.data.repository.UploadedImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UplodedImageDAO {
    private final UploadedImageRepository uploadedImageRepository;

//    public void handleFileUpload(UploadedImageEntity entity) {
//        uploadedImageRepository.save(entity);
//    }

    public void save(UploadedImageEntity entity) {
        uploadedImageRepository.save(entity);
    }


}
