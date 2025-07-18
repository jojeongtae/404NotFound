import React, { createContext, useState, useContext, useEffect } from 'react'; // useEffect 추가
import { useDispatch, useSelector } from 'react-redux'; // useSelector 추가
import { clearToken } from '../features/auth/tokenSlice';
import { clearUser } from '../features/auth/userSlice';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const dispatch = useDispatch();
  const token = useSelector(state => state.token.token); // Redux 스토어에서 토큰 가져오기
  const [isLoggedIn, setIsLoggedIn] = useState(!!token); // 초기 상태를 토큰 유무에 따라 설정

  useEffect(() => {
    setIsLoggedIn(!!token); // 토큰이 변경될 때마다 isLoggedIn 상태 업데이트
  }, [token]);

  const login = () => {
    // 로그인 성공 시 Redux 스토어에 토큰이 저장되므로, 여기서는 별도로 setIsLoggedIn(true)를 호출할 필요 없음
    console.log("로그인 성공: 상태 업데이트");
  };

  const logout = () => {
    setIsLoggedIn(false);
    dispatch(clearToken());
    dispatch(clearUser());
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
