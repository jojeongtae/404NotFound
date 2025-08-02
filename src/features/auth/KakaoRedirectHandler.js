// src/components/KakaoRedirectHandler.js

import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { setToken } from '../features/auth/tokenSlice';
import { setUser } from '../features/auth/userSlice';
import { useAuth } from '../context/AuthContext';

const KakaoRedirectHandler = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { login } = useAuth();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');
        const username = params.get('username');
        const role = params.get('role');
        const nickname = params.get('nickname');

        if (token && username) {
            // Redux 상태 업데이트
            dispatch(setToken(token));
            dispatch(setUser({ username, role, nickname }));
            console.log(token,username,nickname);
            // 로그인 상태 관리 및 페이지 이동
            login();
            navigate('/');
        } else {
            console.error("카카오 로그인 데이터가 URL에 없습니다.");
            alert("로그인 실패");
            navigate('/login');
        }
    }, [dispatch, login, navigate]);

    return <div>카카오 로그인 처리 중...</div>;
};

export default KakaoRedirectHandler;