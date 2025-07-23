// import SockJS from "sockjs-client";
// import Stomp from "stompjs";
// import { useEffect, useState } from "react";

// const DiceGame = ({ username, roomId }) => {
//   const [stompClient, setStompClient] = useState(null);
//   const [dice, setDice] = useState(null);
//   const [result, setResult] = useState(null);

//   useEffect(() => {
//     const socket = new SockJS("http://localhost:8080/ws");
//     const client = Stomp.over(socket);
//     client.connect({}, () => {
//       client.subscribe(`/topic/dice/${roomId}`, (message) => {
//         const gameResult = JSON.parse(message.body);
//         setResult(gameResult);
//       });
//     });
//     setStompClient(client);

//     return () => client.disconnect();
//   }, []);

//   const rollDice = () => {
//     const rolled = Math.floor(Math.random() * 6) + 1;
//     setDice(rolled);
//     stompClient.send(`/app/dice/${roomId}`, {}, JSON.stringify({ username, dice: rolled }));
//   };

//   return (
//     <div>
//       <h2>주사위 게임</h2>
//       <p>당신의 주사위: {dice}</p>
//       <button onClick={rollDice}>주사위 굴리기</button>
//       {result && (
//         <div>
//           <p>결과: {result.winner === "DRAW" ? "무승부" : `${result.winner} 승리!`}</p>
//           <p>상대 주사위: {result.userADice} vs {result.userBDice}</p>
//         </div>
//       )}
//     </div>
//   );
// };

// export default DiceGame;
