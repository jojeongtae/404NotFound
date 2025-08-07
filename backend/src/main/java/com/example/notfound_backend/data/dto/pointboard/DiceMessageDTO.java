package com.example.notfound_backend.data.dto.pointboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiceMessageDTO {
    private String username;
    private int diceValue;
    private String roomId; // 나중에 1:1 구현용
}
