package com.example.notfound_backend.data.entity.admin;

import com.example.notfound_backend.data.entity.enumlist.ReportStatus;
import com.example.notfound_backend.data.entity.login.UserAuthEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "report")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason; // 신고사유 (간단유형)

    @Lob
    @Column(name = "description")
    private String description; // 상세설명(선택)

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reporter", nullable = false)
    private UserAuthEntity reporter;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported", nullable = false)
    private UserAuthEntity reported;

    @Size(max = 30)
    @Column(name = "target_table", length = 30)
    private String targetTable;

    @NotNull
    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}