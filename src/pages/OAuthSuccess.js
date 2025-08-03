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
      dispatch(setToken(accessToken));
      navigate('/'); // 메인으로 이동
    }
  }, [dispatch, navigate]);

  return <div>로그인 처리중...</div>;
};

export default OAuthSuccess;
