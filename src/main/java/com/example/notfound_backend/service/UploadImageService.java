package com.example.notfound_backend.service;

import com.example.notfound_backend.data.dao.UploadImageDAO;
import com.example.notfound_backend.data.dto.UploadImageDTO;
import com.example.notfound_backend.data.entity.UploadImageEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadImageService { // 파일 저장 로직
    private final UploadImageDAO uploadImageDAO;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public UploadImageDTO uploadImage(MultipartFile file) throws IOException {
        String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
        Long fileSize = file.getSize();
        String savedFileName = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDir, savedFileName);

        Files.createDirectories(filePath.getParent()); // 디렉토리 없으면 생성
        Files.write(filePath, file.getBytes()); // 파일 저장

        // DB에 경로 저장
        UploadImageEntity imageEntity = UploadImageEntity.builder()
                .originalName(originalFilename)
                .savedName(savedFileName)
                .filePath(filePath.toString())
                .uploadedAt(Instant.now())
                .fileSize(fileSize)
                .build();
        UploadImageEntity uploadImageEntity = uploadImageDAO.uploadImage(imageEntity);
        return UploadImageDTO.builder()
                .id(uploadImageEntity.getId())
                .originalName(uploadImageEntity.getOriginalName())
                .savedName(uploadImageEntity.getSavedName())
                .filePath(uploadImageEntity.getFilePath())
                .uploadedAt(uploadImageEntity.getUploadedAt())
                .fileSize(uploadImageEntity.getFileSize())
                .build();
    }

    // 게시판 이미지
    public String uploadBoardImage(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String savedFileName = UUID.randomUUID().toString() + "_" + originalFilename;
            Path filePath = Paths.get(uploadDir, savedFileName);

            Files.createDirectories(filePath.getParent()); // 디렉토리 없으면 생성
            Files.write(filePath, file.getBytes()); // 파일 저장

            return filePath.toString().replace("\\","/"); // 파일경로
        }
        return null;
    }

    public String updateBoardImage(MultipartFile file, String imagePath) throws IOException {
        if (file != null && !file.isEmpty()) {
            // 기존 이미지 삭제
            if (imagePath != null) {
                Path oldImgPath = Paths.get(imagePath);
                if (Files.exists(oldImgPath)) {
                    Files.delete(oldImgPath);
                }
            }
            // 새 파일 저장
            String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String savedFileName = UUID.randomUUID().toString() + "_" + originalFilename;
            Path filePath = Paths.get(uploadDir, savedFileName);
            Files.write(filePath, file.getBytes()); // 파일 저장
            return filePath.toString().replace("\\","/");
        }
        return imagePath;
    }

    public void deleteBoardImage(String imagePath) throws IOException {
        if (imagePath != null && !imagePath.isEmpty()) {
            Path path = Paths.get(imagePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        }
    }




}
