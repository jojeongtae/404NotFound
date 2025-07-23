package com.example.notfound_backend.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "uploaded_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Size(max = 255)
    @NotNull
    @Column(name = "saved_name", nullable = false)
    private String savedName;

    @Size(max = 500)
    @NotNull
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "uploaded_at")
    private Instant uploadedAt;

}