import React, { useState } from 'react';
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

  // 🔹 카카오 로그인
  const handleKakaoLogin = () => {
    // Step1: 카카오 로그인 페이지 이동
    window.location.href = "/api/kakao";
  };

  // 🔹 카카오 로그인 콜백 처리
 React.useEffect(() => {
  const url = new URL(window.location.href);
  const code = url.searchParams.get("code");

  if (code) {
    apiClient.get(`/login/oauth2/code/kakao?code=${code}`, { withCredentials: true })
      .then(res => {
        if (res.data?.username) {
          // Redux 상태 갱신
          dispatch(setUser(res.data));
          login();

          // URL 정리 (code 제거)
          window.history.replaceState({}, document.title, window.location.pathname);

          console.log("카카오 로그인 성공:", res.data);
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
      <button type='button' onClick={handleKakaoLogin}>카카오 로그인</button>
      <button type='button' onClick={handleNaverLogin}>네이버 로그인</button>
    </>
  );
};

export default LoginForm;
