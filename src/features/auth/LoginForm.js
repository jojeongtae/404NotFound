import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import apiClient from '../../api/apiClient';
import { useDispatch } from 'react-redux';
import { setToken } from '../../features/auth/tokenSlice';
import { setUser } from '../../features/auth/userSlice'; // setUser 액션 임포트

const LoginForm = ({ onClose }) => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async(e) => {
    e.preventDefault();
    
    try {
      const params = new URLSearchParams();
      params.append('username', username);
      params.append('password', password);

      const response = await apiClient.post("/login", params);
      
      console.log("로그인 응답", response);

      const accessToken = response.headers.authorization; 
      if (accessToken) {
        dispatch(setToken(accessToken)); 
        console.log("토큰 저장 완료:", accessToken);
      }

      // 백엔드 응답 본문에서 사용자 정보 추출 및 저장
      const userData = {
        username: response.data.username || username, // 응답에 username이 없으면 입력값 사용
        role: response.data.role, // 백엔드에서 role을 제공한다고 가정
      };
      dispatch(setUser(userData)); // Redux 스토어에 사용자 정보 저장
      console.log("사용자 정보 저장 완료:", userData);

      login(); // AuthContext의 로그인 상태 업데이트
      alert('로그인 성공!');
      onClose(); // 모달 닫기
      navigate('/');

    } catch (error) {
      console.error('로그인 중 오류 발생:', error);
      const errorMessage = error.response?.data?.message || error.message || '알 수 없는 오류';
      
      if (error.response?.status === 401) {
        alert(`로그인 실패: ${errorMessage}. 아이디 또는 비밀번호를 확인해주세요.`);
      } else {
        alert(`로그인 실패: ${errorMessage}. 다시 시도해주세요.`);
      }
    }
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <h2>로그인</h2>
        <input 
          type='text' 
          placeholder='아이디 입력칸' 
          value={username} 
          onChange={(e) => setUsername(e.target.value)}
        /> <br />
        <input 
          type="password" 
          placeholder='비밀번호 입력칸' 
          value={password} 
          onChange={(e) => setPassword(e.target.value)}
        /> <br />
        <input type='submit' value="로그인" />
      </form>
    </>
  );
};

export default LoginForm;
