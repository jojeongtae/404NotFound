package com.example.notfound_backend.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Lob
    @Column(name = "body", nullable = false)
    private String body;

    @Size(max = 255)
    @Column(name = "imgsrc")
    private String imgsrc;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private UserAuthEntity author;

    @ColumnDefault("0")
    @Column(name = "recommend")
    private Integer recommend;

    @ColumnDefault("0")
    @Column(name = "views")
    private Integer views;

    @Size(max = 30)
    @Column(name = "category", length = 30)
    private String category;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ColumnDefault("'VISIBLE'")
    @Lob
    @Column(name = "status")
    private String status;

}