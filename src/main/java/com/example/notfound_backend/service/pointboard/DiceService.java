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

    public DiceResultDTO processDiceRoll(DiceMessageDTO player1,DiceMessageDTO player2) {
        int diceValue1 = player1.getDiceValue();
        int diceValue2 = player2.getDiceValue();

        if (diceValue1 == diceValue2){
            return DiceResultDTO.builder()
                    .draw(true)
                    .winnerValue(diceValue1)
                    .loserValue(diceValue2)
                    .build();
        }
        String winner = diceValue1 > diceValue2 ? player1.getUsername() : player2.getUsername();
        String loser = diceValue1 < diceValue2 ? player1.getUsername() : player2.getUsername();
        int winnerValue = Math.max(diceValue1, diceValue2);
        int loserValue =  Math.min(diceValue1, diceValue2);

        addPoints(winner,10);
        addPoints(loser,-10);

        return DiceResultDTO.builder()
                .winnerValue(winnerValue)
                .loserValue(loserValue)
                .winner(winner)
                .loser(loser)
                .draw(false)
                .build();
    }


}
