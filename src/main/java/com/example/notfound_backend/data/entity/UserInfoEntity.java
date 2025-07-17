package com.example.notfound_backend.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "user_info")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    private UserAuthEntity username;

    @Size(max = 20)
    @NotNull
    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @ColumnDefault("0")
    @Column(name = "point")
    private Integer point;

    @ColumnDefault("0")
    @Column(name = "warning")
    private Integer warning;

}