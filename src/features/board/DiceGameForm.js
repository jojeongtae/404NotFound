import React, { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useDispatch, useSelector } from "react-redux";
import apiClient from "../../api/apiClient";
import { setUser } from "../../features/auth/userSlice";

const DiceGame = ({ roomId }) => {
  const [dice, setDice] = useState(null);
  const [rolling, setRolling] = useState(false);
  const [result, setResult] = useState(null);
  const [waiting, setWaiting] = useState(false);
  const clientRef = useRef(null);
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);
  const username = user?.username;
  const nickname = user?.nickname;

  // 🔹 유저 정보 업데이트
  const fetchUserInfo = async () => {
    if (!username) return;
    try {
      const res = await apiClient.get(`/user/user-info?username=${username}`);
      dispatch(setUser(res.data));
      console.log("✅ User info updated after dice game:", res.data);
    } catch (error) {
      console.error("❌ Failed to fetch user info:", error);
    }
  };

  // 🔹 WebSocket 연결
  useEffect(() => {
    if (!username) return;

    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_BASE_URL}/ws-game`),
      onConnect: () => {
        console.log("✅ WebSocket connected!");
        client.subscribe(`/topic/game-result/${roomId}`, (message) => {
          const gameResult = JSON.parse(message.body);
          setResult(gameResult);
          setWaiting(false);
          fetchUserInfo(); // 게임 후 유저 정보 갱신
        });
      },
      onStompError: (frame) => {
        console.error("❌ STOMP error:", frame.headers["message"]);
        console.error("Details:", frame.body);
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate();
      }
    };
  }, [roomId, username]);

  // 🔹 주사위 굴리기
  const rollDice = () => {
    if (!clientRef.current || !clientRef.current.connected) {
      console.error("❌ Stomp client is not connected.");
      return;
    }

    setRolling(true);
    setResult(null);

    setTimeout(() => {
      const rolled = Math.floor(Math.random() * 6) + 1;
      setDice(rolled);
      setRolling(false);
      setWaiting(true);

      clientRef.current.publish({
        destination: "/app/game",
        body: JSON.stringify({ username, diceValue: rolled, roomId }),
      });
    }, 1000);
  };


  // 🔹 주사위 이미지 경로
  const diceImage = rolling
    ? "/dice/dice-roll.gif"
    : dice
    ? `/dice/dice${dice}.png`
    : "/dice/dice1.png";

  return (
    <div className="dice-game-container">
      <h4 className="room-title">🎲 주사위 게임 (방 ID: {roomId})</h4>

      <img src={diceImage} alt="주사위" width={120} style={{ margin: "10px 0" }} />

      <p className="play-status">
        {nickname || username}님의 주사위: <strong>{dice || "아직 굴리지 않았습니다."}</strong>
      </p>

      <button
        onClick={rollDice}
        disabled={waiting || rolling}
        className="btn type2 large play-btn"
      >
        {rolling
          ? "굴리는 중..."
          : waiting
          ? "상대방을 기다리는 중..."
          : "주사위 굴리기"}
      </button>

      {result && (
        <div className="result">
          <h4>게임 결과</h4>
          <div className="result-container">
            {result.draw ? (
              <p>결과: <strong>무승부!</strong></p>
            ) : (
              <p>결과: <strong>{result.winnerNickname}</strong>님의 승리!</p>
            )}
            <p>
              점수: {result.winnerNickname}님은 <strong>{result.winnerValue}</strong>점,{" "}
              {result.loserNickname}님은 <strong>{result.loserValue}</strong>점
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default DiceGame;
