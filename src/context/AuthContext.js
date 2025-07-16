import React, { createContext, useState, useContext } from 'react';
import { useDispatch } from 'react-redux';
import { clearToken } from '../features/auth/tokenSlice';
import { clearUser } from '../features/auth/userSlice'; // clearUser 액션 임포트

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const dispatch = useDispatch();

  const login = () => {
    setIsLoggedIn(true);
    console.log("로그인 성공: 상태 업데이트");
  };

  const logout = () => {
    setIsLoggedIn(false);
    dispatch(clearToken()); // 토큰 클리어
    dispatch(clearUser()); // 사용자 정보 클리어
    alert("로그아웃이 완료되었습니다.");
    console.log("로그아웃 성공: 상태 업데이트 및 토큰/사용자 정보 클리어");
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
