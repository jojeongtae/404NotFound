package com.example.notfound_backend.data.entity.pointboard;

import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import com.example.notfound_backend.data.entity.enumlist.Type;
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
@Table(name = "quiz")
public class QuizEntity {
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
    @Column(name = "answer", nullable = false)
    private String answer;

    @Size(max = 255)
    @Column(name = "imgsrc")
    private String imgsrc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private UserAuthEntity author;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("1")
    @Column(name = "level")
    private Integer level=1;

    @Size(max = 30)
    @ColumnDefault("'QUIZ'")
    @Column(name = "category", length = 30)
    private String category="QUIZ";

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "ENUM('MULTI', 'SUBJECTIVE', 'OX')", nullable = false)
    private Type type=Type.MULTI;

    @ColumnDefault("0")
    @Column(name = "views")
    private Integer views=0;

}