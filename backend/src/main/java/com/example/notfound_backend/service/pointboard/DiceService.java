package com.example.notfound_backend.service.pointboard;

import com.example.notfound_backend.data.dao.admin.UserInfoDAO;
import com.example.notfound_backend.data.dto.pointboard.DiceMessageDTO;
import com.example.notfound_backend.data.dto.pointboard.DiceResultDTO;
import com.example.notfound_backend.data.entity.admin.UserInfoEntity;
import com.example.notfound_backend.data.repository.admin.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiceService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoDAO userInfoDAO;
    public void addPoints(String username, int point) {
        UserInfoEntity userInfo = userInfoDAO.getUserInfo(username);
        userInfo.setPoint(userInfo.getPoint() + point);
        userInfoRepository.save(userInfo);
    }

    public DiceResultDTO processDiceRoll(DiceMessageDTO player1, DiceMessageDTO player2) {
        int diceValue1 = player1.getDiceValue();
        int diceValue2 = player2.getDiceValue();

        // 무승부 처리
        if (diceValue1 == diceValue2) {
            return DiceResultDTO.builder()
                    .draw(true)
                    .winnerValue(diceValue1)
                    .loserValue(diceValue2)
                    .build();
        }

        // 승자/패자 구분
        boolean player1Wins = diceValue1 > diceValue2;
        String winnerUsername = player1Wins ? player1.getUsername() : player2.getUsername();
        String loserUsername  = player1Wins ? player2.getUsername() : player1.getUsername();
        int winnerValue = Math.max(diceValue1, diceValue2);
        int loserValue  = Math.min(diceValue1, diceValue2);

        // 🔹 유저 정보 가져와서 닉네임 조회
        UserInfoEntity winnerEntity = userInfoDAO.getUserInfo(winnerUsername);
        UserInfoEntity loserEntity = userInfoDAO.getUserInfo(loserUsername);

        // 포인트 갱신
        addPoints(winnerUsername, 10);
        addPoints(loserUsername, -10);

        // DTO 반환
        return DiceResultDTO.builder()
                .winner(winnerUsername)
                .winnerNickname(winnerEntity.getNickname())
                .winnerValue(winnerValue)
                .loser(loserUsername)
                .loserNickname(loserEntity.getNickname())
                .loserValue(loserValue)
                .draw(false)
                .build();
    }



}
