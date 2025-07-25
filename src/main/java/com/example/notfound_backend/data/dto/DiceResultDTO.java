package com.example.notfound_backend.data.dto;

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
    private String loser;
    private int winnerValue;
    private int loserValue;
    private boolean draw;
}
