import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { setToken } from '../features/auth/tokenSlice';
import { useNavigate } from 'react-router-dom';

const OAuthSuccess = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get('accessToken');

    if (accessToken) {
      dispatch(setToken('Bearer ' + accessToken));
      navigate('/'); // 메인 페이지로 이동
    } else {
      navigate('/login?error'); // 실패 시 로그인 화면으로
    }
  }, [dispatch, navigate]);

  return <div>로그인 처리중...</div>;
};

export default OAuthSuccess;
