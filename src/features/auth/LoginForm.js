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
    window.location.href = `${API_BASE_URL}/api/oauth2/authorization/naver`;
  };

  // 🔹 카카오 OAuth 콜백 처리
  useEffect(() => {
    const url = new URL(window.location.href);
    const code = url.searchParams.get("code");

    if (code) {
      console.log("카카오 인가 코드 감지:", code);

      apiClient.get(`/login/oauth2/code/kakao?code=${code}`, { withCredentials: true })
        .then(res => {
          console.log("카카오 로그인 응답:", res.data);

          if (res.data?.username) {
            // Redux 상태 갱신
            dispatch(setToken("Bearer " + res.data.accessToken));
            dispatch(setUser({
              username: res.data.username,
              role: res.data.role,
              nickname: res.data.nickname,
              phone: res.data.phone || "", // KakaoLoginController에서 phone을 반환하지 않으므로 기본값 설정
              address: res.data.address || '', // KakaoLoginController에서 address를 반환하지 않으므로 기본값 설정
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

  return (
    <div className="login-modal">
      <form onSubmit={handleSubmit}>
        <h3>로그인</h3>
        <ul className="login-list">
          <li className="login-item">
            <label>
              <span>아이디: </span>
              <input
                  type="text"
                  placeholder="아이디 입력"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
              />
            </label>
          </li>
          <li className="login-item">
            <label>
              <span>비밀번호: </span>
              <input
                  type="password"
                  placeholder="비밀번호 입력"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
              />
            </label>
          </li>
        </ul>
        <div className="btn_wrap">
          <button type="submit" className="btn type2 large login-btn">로그인</button>
        </div>
      </form>
      <div className="oauth2_wrap">
        <button type='button' onClick={handleKakaoLogin} className="kakao">카카오 로그인</button>
        <button type='button' onClick={handleNaverLogin} className="naver">
          <span className="svg-wrap">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M16.273 12.845 7.376 0H0v24h7.727V11.155L16.624 24H24V0h-7.727v12.845z"></path>
            </svg>
          </span>
          <span>네이버 로그인</span>
        </button>
      </div>
    </div>
  );
};

export default LoginForm;
