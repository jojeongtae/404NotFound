import React, { useState } from 'react';
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

    /** 🔹 일반 로그인 처리 */
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const params = new URLSearchParams();
            params.append('username', username);
            params.append('password', password);

            // JWT 로그인 요청
            const response = await apiClient.post('/login', params);

            // AccessToken Redux 저장
            const accessToken = response.headers.authorization;
            if (accessToken) {
                dispatch(setToken(accessToken));
            }

            // 기본 유저 데이터 저장
            const basicUserData = {
                username: response.data.username || username,
                role: response.data.role,
            };
            dispatch(setUser(basicUserData));

            // 상세 유저 정보 요청
            const userDetailsResponse = await apiClient.get(`/user/user-info?username=${basicUserData.username}`);
            dispatch(setUser({
                ...basicUserData,
                ...userDetailsResponse.data,
            }));

            login();  // Context 로그인 처리
            onClose();
            navigate('/');
        } catch (error) {
            const errorMessage = error.response?.data?.message || '로그인 실패';
            alert(errorMessage);
        }
    };

    /** 🔹 소셜 로그인 버튼 */
    const handleKakaoLogin = () => {
        window.location.href = `${API_BASE_URL}/api/oauth2/authorization/kakao`;
    };

    const handleNaverLogin = () => {
        window.location.href = `${API_BASE_URL}/api/oauth2/authorization/naver`;
    };

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
