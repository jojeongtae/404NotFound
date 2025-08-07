import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { setToken } from '../features/auth/tokenSlice';
import { setUser } from '../features/auth/userSlice';
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';

const OAuthSuccess = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get('accessToken');
    const username = params.get('username');
    const nickname = params.get('nickname');
    const role = params.get('role');

    if (accessToken && username) {
      // JWT 저장
      dispatch(setToken('Bearer ' + accessToken));
      const fetchUserInfo = async () => { // async 함수로 변경
        try {
          const res = await apiClient.get(`/user/user-info?username=${username}`); // await 추가
          dispatch(setUser(res.data)); // Redux 스토어 업데이트
        } catch (error) {
          console.error("Failed to fetch user info:", error); // console.log 대신 console.error 사용
        }
      };
      fetchUserInfo();
    
      window.history.replaceState({}, document.title, '/');

      navigate('/'); // 메인 페이지로 이동
    } else {
      navigate('/login?error'); // 실패 시 로그인 화면으로
    }
  }, [dispatch, navigate]);

  return <div>로그인 처리중...</div>;
};

export default OAuthSuccess;
