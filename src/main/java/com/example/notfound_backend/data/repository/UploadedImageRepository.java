package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.UploadImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedImageRepository extends JpaRepository<UploadImageEntity, Long> {
}
