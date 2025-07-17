package com.example.notfound_backend.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJoinDTO {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(max = 30, message = "아이디는 최대 30자까지 가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 3, max = 100, message = "비밀번호는 3자 이상 100자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임 필수입니다.")
    @Size(max = 20, message = "닉네임은 최대 20자까지 가능합니다.")
    private String nickname;

    @Size(max = 20, message = "폰번호 최대 20자까지 가능합니다.")
    private String phone;

    @Size(max = 100, message = "주소 최대 100자까지 가능합니다.")
    private String address;

}
