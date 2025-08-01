import React, { useState, useEffect } from 'react'; // --수정된부분--
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import apiClient from '../../api/apiClient';
import { useDispatch } from 'react-redux';
import { setToken } from '../../features/auth/tokenSlice';
import { setUser } from '../../features/auth/userSlice';

const LoginForm = ({ onClose }) => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  // 🔹 일반 로그인 처리
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const params = new URLSearchParams();
      params.append('username', username);
      params.append('password', password);

      const response = await apiClient.post("/login", params);

      const accessToken = response.headers.authorization;
      if (accessToken) {
        dispatch(setToken(accessToken));
      }

      const basicUserData = {
        username: response.data.username || username,
        role: response.data.role,
      };
      dispatch(setUser(basicUserData));

      // 유저 상세 정보 로딩
      const userDetailsResponse = await apiClient.get(`/user/user-info?username=${basicUserData.username}`);
      dispatch(setUser({
        ...basicUserData,
        ...userDetailsResponse.data,
      }));

      login();
      onClose();
      navigate('/');

    } catch (error) {
      const errorMessage = error.response?.data?.message || '로그인 실패';
      alert(errorMessage);
    }
  };

  // 🔹 카카오 로그인 버튼
  const handleKakaoLogin = () => {
    window.location.href = "/api/kakao"; // --추가된부분-- (navigate → 직접 이동)
  };

  // 🔹 카카오 OAuth 콜백 처리
  useEffect(() => {
    const url = new URL(window.location.href);
    const code = url.searchParams.get("code");

    if (code) {
      console.log("카카오 인가 코드 감지:", code); // --추가된부분--

      apiClient.get(`/login/oauth2/code/kakao?code=${code}`, { withCredentials: true }) // --수정된부분--
        .then(res => {
          console.log("카카오 로그인 응답:", res.data); // --추가된부분--

          if (res.data?.username) {
            // Redux 상태 갱신
            dispatch(setToken(res.data.accessToken)); // --추가된부분--
            dispatch(setUser({
              username: res.data.username,
              role: res.data.role,
              nickname: res.data.nickname,
            }));

            login();

            // URL 정리 (code 제거)
            window.history.replaceState({}, document.title, window.location.pathname);

            onClose();
            navigate('/');
          }
        })
        .catch(err => {
          console.error("카카오 로그인 실패:", err);
          alert("카카오 로그인 실패");
        });
    }
  }, [dispatch, login, navigate, onClose]);

  const handleNaverLogin = () => {
    console.log("Naver Login Clicked");
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <h2>로그인</h2>
        <input
          type="text"
          placeholder="아이디 입력칸"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        /><br />
        <input
          type="password"
          placeholder="비밀번호 입력칸"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        /><br />
        <input type="submit" value="로그인" />
      </form>
      <div>
        <button type='button' onClick={handleKakaoLogin}>카카오 로그인</button>
        <button type='button' onClick={handleNaverLogin}>네이버 로그인</button>
      </div>
    </>
  );
};

export default LoginForm;
