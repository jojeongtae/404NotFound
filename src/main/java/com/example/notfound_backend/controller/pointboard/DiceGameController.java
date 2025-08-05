package com.example.notfound_backend.controller.pointboard;

import com.example.notfound_backend.data.dto.pointboard.DiceMessageDTO;
import com.example.notfound_backend.data.dto.pointboard.DiceResultDTO;
import com.example.notfound_backend.service.pointboard.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class DiceGameController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DiceService diceService;

    // 🔹 방마다 두 명의 플레이어를 저장하는 맵
    private final ConcurrentHashMap<String, DiceMessageDTO[]> roomMap = new ConcurrentHashMap<>();

    @MessageMapping("/game")
    public void game(DiceMessageDTO diceMessageDTO) {  // @RequestBody 제거, STOMP 메시지에서 바로 매핑됨
        String roomId = diceMessageDTO.getRoomId();
        roomMap.putIfAbsent(roomId, new DiceMessageDTO[2]);
        DiceMessageDTO[] players = roomMap.get(roomId);

        synchronized (players) {
            // 첫 번째 슬롯 채우기
            if (players[0] == null) {
                players[0] = diceMessageDTO;
            }
            // 두 번째 슬롯 채우기 (중복 유저 방지)
            else if (players[1] == null && !players[0].getUsername().equals(diceMessageDTO.getUsername())) {
                players[1] = diceMessageDTO;
            }
            // 이미 방이 꽉 찼거나 중복이면 무시
            else {
                return;
            }

            // 두 명 모두 주사위를 굴리면 결과 계산
            if (players[0] != null && players[1] != null) {
                DiceResultDTO result = diceService.processDiceRoll(players[0], players[1]);

                // 🔹 결과 브로드캐스트
                simpMessagingTemplate.convertAndSend("/topic/game-result/" + roomId, result);

                // 🔹 방 초기화
                roomMap.remove(roomId);
            }
        }
    }
}
