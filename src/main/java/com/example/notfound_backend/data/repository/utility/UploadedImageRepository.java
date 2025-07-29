package com.example.notfound_backend.data.repository.utility;

import com.example.notfound_backend.data.entity.utility.UploadImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedImageRepository extends JpaRepository<UploadImageEntity, Integer> {
}
