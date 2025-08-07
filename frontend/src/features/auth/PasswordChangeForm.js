import React, { useState } from 'react';
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';

const PasswordChangeForm = ({ onSwitchToInfo, onClose }) => {
  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const username = useSelector(state => state.user.username);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPasswordData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setError('새 비밀번호가 일치하지 않습니다.');
      return;
    }

    try {
      // API 엔드포인트는 실제 프로젝트에 맞게 수정해야 합니다.
      await apiClient.put('/user/password', {
        username,
        oldPassword: passwordData.currentPassword,
        newPassword: passwordData.newPassword,
      });
      setSuccess('비밀번호가 성공적으로 변경되었습니다.');
      // 성공 후 폼 초기화 또는 모달 닫기
      setPasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' });
      setTimeout(onClose, 1500); // 1.5초 후 모달 닫기
    } catch (err) {
      setError(err.response?.data?.message || '비밀번호 변경 중 오류가 발생했습니다.');
    }
  };

  return (
      <div className="tab-container password">
        <form onSubmit={handleSubmit}>
          <ul>
            <li>
              <input
                  type="password"
                  name="currentPassword"
                  value={passwordData.currentPassword}
                  onChange={handleChange}
                  placeholder="현재 비밀번호"
                  required
              />
            </li>
            <li>
              <input
                  type="password"
                  name="newPassword"
                  value={passwordData.newPassword}
                  onChange={handleChange}
                  placeholder="새 비밀번호"
                  required
              />
            </li>
            <li>
              <input
                  type="password"
                  name="confirmPassword"
                  value={passwordData.confirmPassword}
                  onChange={handleChange}
                  placeholder="새 비밀번호 확인"
                  required
              />
            </li>
          </ul>
          {error && <p style={{ color: 'red' }}>{error}</p>}
          {success && <p style={{ color: 'green' }}>{success}</p>}
          <div className="btn_wrap">
            <button type="submit" className="btn type2 large">비밀번호 변경</button>
          </div>
        </form>
      </div>
  );
};

export default PasswordChangeForm;
