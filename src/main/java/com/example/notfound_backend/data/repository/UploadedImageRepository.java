package com.example.notfound_backend.data.repository;

import com.example.notfound_backend.data.entity.UploadedImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedImageRepository extends JpaRepository<UploadedImageEntity, Long> {
}
