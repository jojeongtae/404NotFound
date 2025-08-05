import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import DiceGame from '../features/board/DiceGameForm';

const DiceGamePage = () => {
    const user = useSelector(state => state.user);
    const [roomId, setRoomId] = useState('default-room'); // 초기 룸 ID 설정

    // 로그인하지 않은 경우를 대비한 처리
    if (!user || !user.username) {
        return <p>로그인이 필요합니다.</p>;
    }
    //포인트 부족시
    if(user.point < 10){
        return <p>포인트가 모자랍니다</p>
    }

    return (
        <div className="dice-game">
            <h3>🎲 주사위 게임방</h3>
            {/* 방 ID를 선택하거나 입력하는 UI를 여기에 추가할 수 있습니다. */}
            <div className="room-id">
                <label>
                    <strong>방 ID: </strong>
                    <input
                        type="text"
                        value={roomId}
                        onChange={(e) => setRoomId(e.target.value)}
                        placeholder="방 ID를 입력하세요"
                    />
                </label>
            </div>
            <DiceGame username={user} roomId={roomId} />
        </div>
    );
};

export default DiceGamePage;
