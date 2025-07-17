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

      // 1단계: 기본 사용자 정보 (username, role) 저장
      const basicUserData = {
        username: response.data.username || username,
        role: response.data.role,
      };
      dispatch(setUser(basicUserData));
      console.log("기본 사용자 정보 저장 완료:", basicUserData);

      // 2단계: 추가 정보 비동기 요청 및 저장
      const fetchUserDetails = async (token) => {
        try {
          // TODO: 아래 URL을 실제 사용자 상세 정보 API 엔드포인트로 교체하세요.
          // const userDetailsResponse = await apiClient.get('/api/user/details', {
          //   headers: { 'Authorization': token }
          // });

          // --- 아래는 API 연동 전 임시 목(Mock) 데이터입니다. ---
          // --- 실제 API 연동 시 이 부분을 삭제하고 위 주석을 해제하세요. ---
          const userDetailsResponse = {
            data: {
              nickname: "임시 닉네임",
              phone: "010-1234-5678",
              address: "임시 주소"
            }
          };
          // --- 여기까지 임시 목 데이터 ---

          dispatch(setUser(userDetailsResponse.data));
          console.log("추가 사용자 정보 저장 완료:", userDetailsResponse.data);

        } catch (error) {
          console.error("추가 사용자 정보 로딩 중 오류 발생:", error);
          // 추가 정보 로딩 실패 시 사용자에게 알림을 주거나 다른 처리를 할 수 있습니다.
        }
      };

      if (accessToken) {
        fetchUserDetails(accessToken);
      }
      
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
