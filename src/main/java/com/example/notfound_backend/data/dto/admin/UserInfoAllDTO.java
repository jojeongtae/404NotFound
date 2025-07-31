package com.example.notfound_backend.data.dto.admin;

import com.example.notfound_backend.data.entity.enumlist.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoAllDTO {
    private String username;
    private String nickname;
    private String phone;
    private String address;
    private Integer point;
    private Integer warning;
    private UserStatus status;
    private String grade;
}
