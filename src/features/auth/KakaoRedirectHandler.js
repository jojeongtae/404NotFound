// src/components/KakaoRedirectHandler.js

import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';
import { setToken } from '../features/auth/tokenSlice';
import { setUser } from '../features/auth/userSlice';
import { useAuth } from '../context/AuthContext';

const KakaoRedirectHandler = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const location = useLocation();
    const { login } = useAuth();

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const token = params.get('token');
        const username = params.get('username');
        const role = params.get('role');
        const nickname = params.get('nickname');

        if (token && username) {
            console.log("Token:", token);
            console.log("Username:", username);
            console.log("Role:", role);
            console.log("Nickname:", nickname);

            // URL 파라미터에서 받은 데이터를 Redux에 저장
            dispatch(setToken(token));
            dispatch(setUser({ username, role, nickname }));
             console.log("로그인 함수 호출 전");
            // 전역 로그인 상태를 업데이트하고 홈으로 이동
            login();
            console.log("로그인 함수 호출 후")
            // navigate('/', { replace: true });
        } else {
            console.error("카카오 로그인 데이터가 URL에 없습니다.");
            alert("카카오 로그인 실패");
            navigate('/login');
        }
    }, [dispatch, navigate, location.search, login]);

    return <div>카카오 로그인 처리 중...</div>;
};

export default KakaoRedirectHandler;