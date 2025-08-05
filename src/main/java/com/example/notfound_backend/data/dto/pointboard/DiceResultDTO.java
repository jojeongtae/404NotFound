package com.example.notfound_backend.data.dto.pointboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiceResultDTO {

    private String winner;
    private String winnerNickname;
    private String loser;
    private int winnerValue;
    private String loserNickname;
    private int loserValue;
    private boolean draw;
}
