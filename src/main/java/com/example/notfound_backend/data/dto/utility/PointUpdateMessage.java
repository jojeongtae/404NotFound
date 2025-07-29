package com.example.notfound_backend.data.dto.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getter, Setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
public class PointUpdateMessage {
    private String userId;
    private int points;
    // 필요하다면 다른 필드 추가 (예: String message)
}
