package com.example.notfound_backend.controller;

import com.example.notfound_backend.data.dto.DiceMessageDTO;
import com.example.notfound_backend.data.dto.DiceResultDTO;
import com.example.notfound_backend.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dice")
public class DiceGameController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DiceService diceService;

    private final ConcurrentHashMap<String, DiceMessageDTO[]> roomMap  = new ConcurrentHashMap<>();


    @MessageMapping("/game")
    public void game(@RequestBody DiceMessageDTO diceMessageDTO) {
        String roomId = diceMessageDTO.getRoomId();
        roomMap.putIfAbsent(roomId, new DiceMessageDTO[2]);
        DiceMessageDTO[] players = roomMap.get(roomId);

        synchronized (players) {
            if (players[0] == null) {
                players[0] = diceMessageDTO;
            } else if (players[1] == null && !players[0].getUsername().equals(diceMessageDTO.getUsername())) {
                players[1] = diceMessageDTO;
            } else {
                // 같은 유저가 중복으로 보냈거나 이미 다 찬 경우 무시
                return;
            }
            if (players[0] != null && players[1] != null) {
                DiceResultDTO result = diceService.processDiceRoll(players[0], players[1]);

                // 브로드캐스트: /topic/game-result/{roomId}
                simpMessagingTemplate.convertAndSend("/topic/game-result/" + roomId, result);

                // 방 초기화
                roomMap.remove(roomId);
            }
        }
    }
}

