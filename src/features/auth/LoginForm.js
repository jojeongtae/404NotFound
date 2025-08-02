
import React, { useState } from 'react'; // useEffect 제거
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

    // 🔹 카카오 로그인 버튼 (서버로 리다이렉트)
    const handleKakaoLogin = () => {
        window.location.href = "/api/kakao"; 
    };

    // 🔹 네이버 로그인 버튼
    const handleNaverLogin = () => {
        console.log("네이버 로그인 클릭");
    };

    return (
        <>
            <form onSubmit={handleSubmit}>
                <h2>로그인</h2>
                <input type="text" placeholder="아이디 입력칸" value={username} onChange={(e) => setUsername(e.target.value)} /><br />
                <input type="password" placeholder="비밀번호 입력칸" value={password} onChange={(e) => setPassword(e.target.value)} /><br />
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