import React, { createContext, useState, useContext, useEffect } from 'react'; // useEffect 추가
import { useDispatch, useSelector } from 'react-redux'; // useSelector 추가
import { clearToken, setToken } from '../features/auth/tokenSlice';
import { clearUser } from '../features/auth/userSlice';
import apiClient from '../api/apiClient';


const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const dispatch = useDispatch();
  const token = useSelector(state => state.token.token);
  const [isLoggedIn, setIsLoggedIn] = useState(!!token);

  useEffect(() => {
    setIsLoggedIn(!!token);
  }, [token]);

  const login = () => {
    setIsLoggedIn(true);
    console.log("로그인 성공: 상태 업데이트");
  };

  const logout = async () => { // async 키워드 추가
    try {

      const response = await apiClient.delete('/reissue'); 
      console.log("백엔드 로그아웃 응답:", response.data); // 응답 데이터 확인
    } catch (error) {
      console.error("백엔드 로그아웃 실패:", error);
      // 백엔드 로그아웃 실패 시에도 프론트엔드 상태는 초기화
    } finally {
      setIsLoggedIn(false);
      dispatch(clearToken());
      dispatch(clearUser());
      alert("로그아웃이 완료되었습니다.");
      console.log("로그아웃 성공: 상태 업데이트 및 토큰/사용자 정보 클리어");
    }
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};
