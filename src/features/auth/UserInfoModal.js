import React, { useState } from 'react';
import UserInfoForm from './UserInfoForm';
import PasswordChangeForm from './PasswordChangeForm';

const UserInfoModal = ({ onClose }) => {
  const [view, setView] = useState('info'); // 'info' or 'password'

  return (
    <>
      <div>
        <button onClick={() => setView('info')} style={view === 'info' ? activeLinkStyle : linkStyle}>
          내 정보 수정
        </button>
        <button onClick={() => setView('password')} style={view === 'password' ? activeLinkStyle : linkStyle}>
          비밀번호 변경
        </button>
      </div>

      {view === 'info' ? (
        <UserInfoForm onClose={onClose} />
      ) : (
        <PasswordChangeForm onClose={onClose} onSwitchToInfo={() => setView('info')} />
      )}
    </>
  );
};

const linkStyle = {
  background: 'none',
  border: 'none',
  padding: '5px 10px',
  cursor: 'pointer',
  textDecoration: 'underline',
  color: 'blue',
  marginRight: '10px'
};

const activeLinkStyle = {
  ...linkStyle,
  textDecoration: 'none',
  fontWeight: 'bold',
  color: 'black'
};

export default UserInfoModal;
