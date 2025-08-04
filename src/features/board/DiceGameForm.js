import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useEffect, useState, useRef } from "react";
import { useDispatch } from 'react-redux'; // useDispatch 임포트
import apiClient from '../../api/apiClient'; // apiClient 임포트 (경로 확인)
import { setUser } from '../../features/auth/userSlice'; // setUser 임포트 (경로 확인)

const DiceGame = ({ username, roomId }) => {
  // 컴포넌트의 상태 변수들
  const [dice, setDice] = useState(null); // 사용자가 굴린 주사위 값
  const [result, setResult] = useState(null); // 게임 결과 (승자, 패자, 주사위 값 등)
  const [waiting, setWaiting] = useState(false); // 상대방 플레이어를 기다리는 중인지 여부
  const clientRef = useRef(null); // Stomp 클라이언트 인스턴스를 참조하기 위한 ref
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
  const dispatch = useDispatch(); // useDispatch 초기화

  // 컴포넌트가 마운트될 때 웹소켓 연결을 설정하고, 언마운트될 때 연결을 해제합니다.
  useEffect(() => {
    // SockJS를 사용하여 WebSocket 연결을 생성합니다.
    // 백엔드의 WebSocketConfig에서 설정한 엔드포인트("/ws-game")와 일치해야 합니다.
    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_BASE_URL}/ws-game`),
      
      // Stomp 클라이언트가 성공적으로 연결되었을 때 실행되는 콜백 함수
      onConnect: () => {
        // 게임 결과 메시지를 구독합니다.
        // 백엔드의 DiceGameController에서 메시지를 발행하는 토픽("/topic/game-result/{roomId}")과 일치해야 합니다.
        client.subscribe(`/topic/game-result/${roomId}`, (message) => {
          const gameResult = JSON.parse(message.body); // 수신된 JSON 메시지를 파싱합니다.
          setResult(gameResult); // 게임 결과를 상태에 저장하여 UI를 업데이트합니다.
          setWaiting(false); // 결과를 받았으므로 기다림 상태를 해제합니다.

          // --- 추가된 부분: 사용자 정보 업데이트 ---
          const fetchUserInfo = async () => {
            try {
              const res = await apiClient.get(`/user/user-info?username=${username}`);
              dispatch(setUser(res.data));
              console.log("User info updated after dice game:", res.data);
            } catch (error) {
              console.error("Failed to fetch user info after dice game:", error);
            }
          };
          fetchUserInfo();
          // --- 추가된 부분 끝 ---
        });
      },
      
      // Stomp 오류가 발생했을 때 실행되는 콜백 함수
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    // Stomp 클라이언트를 활성화하여 웹소켓 연결을 시작합니다.
    client.activate();
    clientRef.current = client; // 클라이언트 인스턴스를 ref에 저장합니다.

    // 컴포넌트가 언마운트될 때 실행되는 클린업 함수
    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate(); // Stomp 클라이언트 연결을 해제합니다.
      }
    };
  }, [roomId, username, dispatch]); // roomId, username, dispatch가 변경될 때마다 useEffect를 다시 실행합니다.

  // 주사위를 굴리는 함수
  const rollDice = () => {
    // Stomp 클라이언트가 연결되어 있는지 확인합니다.
    if (clientRef.current && clientRef.current.connected) {
      const rolled = Math.floor(Math.random() * 6) + 1; // 1부터 6까지의 랜덤 숫자 생성
      setDice(rolled); // 굴린 주사위 값을 상태에 저장합니다.
      setResult(null); // 이전 게임 결과를 초기화합니다.
      setWaiting(true); // 상대방 플레이어를 기다리는 상태로 설정합니다.

      // 백엔드로 전송할 메시지 객체를 생성합니다.
      // 백엔드의 DiceMessageDTO와 필드명이 일치해야 합니다.
      const message = {
        username: username,
        diceValue: rolled,
        roomId: roomId,
      };
      // 메시지를 백엔드로 발행합니다.
      // 백엔드의 DiceGameController에서 @MessageMapping("/game")으로 설정된 경로와 일치해야 합니다.
      clientRef.current.publish({ destination: "/app/game", body: JSON.stringify(message) });
    } else {
      console.error("Stomp client is not connected."); // 클라이언트가 연결되지 않았을 경우 에러 로그 출력
    }
  };

  return (
    <div className="dice-game-container">
      <h4 className="room-title">주사위 게임 (방 ID: {roomId})</h4>
      <p className="play-status">{username}님의 주사위: <strong>{dice || "아직 굴리지 않았습니다."}</strong></p>
      {/* 주사위 굴리기 버튼: waiting 상태일 때는 비활성화됩니다. */}
      <button onClick={rollDice} disabled={waiting} className="btn type2 large play-btn">
        {waiting ? "상대방을 기다리는 중..." : "주사위 굴리기"}
      </button>
      {/* 게임 결과가 있을 경우에만 결과를 표시합니다. */}
      {result && (
        <div className="result">
          <h4>게임 결과</h4>
          {/* 무승부 여부에 따라 다른 메시지를 표시합니다. */}
          <div className="result-container">
            {result.draw ? (
                <p>결과: <strong>무승부!</strong></p>
            ) : (
                <p>결과: <strong>{result.winner}</strong>님의 승리!</p>
            )}
            {/* 각 플레이어의 주사위 값을 표시합니다. */}
            <p>점수: {result.winner}님은 <strong>{result.winnerValue}</strong>점, {result.loser}님은 <strong>{result.loserValue}</strong>점 입니다.</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default DiceGame;
