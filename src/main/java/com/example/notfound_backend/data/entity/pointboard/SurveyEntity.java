package com.example.notfound_backend.data.entity.pointboard;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
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
@Table(name = "survey")
public class SurveyEntity {
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
    @Column(name = "question", nullable = false)
    private String question;

    @Size(max = 255)
    @NotNull
    @Column(name = "column1", nullable = false)
    private String column1;

    @Size(max = 255)
    @NotNull
    @Column(name = "column2", nullable = false)
    private String column2;

    @Size(max = 255)
    @Column(name = "column3")
    private String column3;

    @Size(max = 255)
    @Column(name = "column4")
    private String column4;

    @Size(max = 255)
    @Column(name = "column5")
    private String column5;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private UserAuthEntity author;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 30)
    @ColumnDefault("'SURVEY'")
    @Column(name = "category", length = 30)
    private String category="SURVEY";

    @ColumnDefault("0")
    @Column(name = "views")
    private Integer views=0;

}