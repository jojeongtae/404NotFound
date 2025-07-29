package com.example.notfound_backend.data.dao.utility;

import com.example.notfound_backend.data.entity.utility.UploadImageEntity;
import com.example.notfound_backend.data.repository.utility.UploadedImageRepository;
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
