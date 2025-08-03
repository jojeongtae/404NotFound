// // LoginCallback.js
// import { useEffect } from "react";
// import { useNavigate, useParams } from "react-router-dom";
// import axios from "axios";

// export default function LoginCallback() {
//   const navigate = useNavigate();
//   const { provider } = useParams(); // kakao or naver

//   useEffect(() => {
//     const code = new URL(window.location.href).searchParams.get("code");
//     if (code) {
//       axios.get(`/api/login/oauth2/code/${provider}?code=${code}`)
//         .then(res => {
//           console.log(`${provider} 유저 정보:`, res.data);
//           // TODO: 여기에 Redux 저장 or JWT 요청 붙이면 됨
//           navigate("/");
//         });
//     }
//   }, [provider]);

//   return <div>{provider} 로그인 처리중...</div>;
// }
