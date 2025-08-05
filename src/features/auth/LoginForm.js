import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import apiClient from '../../api/apiClient';
import { useDispatch } from 'react-redux';
import { setToken } from '../../features/auth/tokenSlice';
import { setUser } from '../../features/auth/userSlice';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

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
    window.location.href = `${API_BASE_URL}/api/kakao`;
  };

  // 🔹 네이버 로그인 버튼
  const handleNaverLogin = () => {
    window.location.href = `${API_BASE_URL}/api/naver`;
  };

  // 🔹 OAuth 콜백 처리 (카카오, 네이버)
  useEffect(() => {
    const url = new URL(window.location.href);
    const code = url.searchParams.get("code");
    const state = url.searchParams.get("state"); // 네이버는 state를 포함합니다.

    // URL에 인가 코드가 있는 경우에만 실행
    if (code) {
      // state 파라미터 유무로 카카오와 네이버를 구분합니다.
      const isNaver = !!state;
      const provider = isNaver ? 'naver' : 'kakao';
      
      console.log(`${provider} 인가 코드 감지:`, code);

      const callbackUrl = isNaver
        ? `/login/oauth2/code/naver?code=${code}&state=${state}`
        : `/login/oauth2/code/kakao?code=${code}`;

      apiClient.get(callbackUrl, { withCredentials: true })
        .then(res => {
          console.log(`${provider} 로그인 응답:`, res.data);

          if (res.data?.username) {
            // Redux 상태 갱신
            dispatch(setToken("Bearer " + res.data.accessToken));
            dispatch(setUser({
              username: res.data.username,
              role: res.data.role,
              nickname: res.data.nickname,
              phone: res.data.phone || "",
              address: res.data.address || '',
            }));

            login();

            // URL에서 인가 코드 관련 파라미터 제거
            window.history.replaceState({}, document.title, window.location.pathname);

            onClose();
            navigate('/');
          }
        })
        .catch(err => {
          console.error(`${provider} 로그인 실패:`, err);
          alert(`${provider} 로그인에 실패했습니다. 자세한 내용은 콘솔을 확인해주세요.`);
        });
    }
  }, [dispatch, login, navigate, onClose]);

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
