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

  const handleSubmit = async (e) => {
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
      const fetchUserDetails = async (token, username) => {
        try {
          const userDetailsResponse = await apiClient.get(`/user/user-info?username=${username}`,
            // { headers: { 'Authorization': token }}

          );
          // const gradeResponse = await apiClient.get(`/user/user-grade?username=${username}`);

          // let finalNickname = userDetailsResponse.data.nickname; // userDetailsResponse에서 nickname 가져오기
          console.log(userDetailsResponse.data);
          // console.log(gradeResponse.data.split(" ")[0]);


          // if (gradeResponse.data.split(" ")[0] === "500") {
          //   finalNickname = "✨" + finalNickname; // nickname에 이모티콘 추가
          // }
          // userDetailsResponse.data와 grade 정보를 합쳐서 setUser에 전달
          dispatch(setUser({
            ...userDetailsResponse.data, // 기존 사용자 상세 정보
            // nickname: finalNickname, // 이모티콘이 추가된 nickname
            // grade: gradeResponse.data, // grade는 원본 그대로 유지
          }));

          console.log("추가 사용자 정보 저장 완료:", userDetailsResponse.data);

        } catch (error) {
          console.error("추가 사용자 정보 로딩 중 오류 발생:", error);
        }
      };

      if (accessToken) {
        fetchUserDetails(accessToken, basicUserData.username);
      }

      login(); // AuthContext의 로그인 상태 업데이트
      if (response.data.role === "ROLE_ADMIN") {
        alert("i'm admin");
      } else {
        alert('로그인 성공!');
      }
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
