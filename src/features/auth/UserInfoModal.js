import React, { useState } from 'react';
import UserInfoForm from './UserInfoForm';
import PasswordChangeForm from './PasswordChangeForm';

const UserInfoModal = ({ onClose }) => {
  const [view, setView] = useState('info'); // 'info' or 'password'

  return (
    <div className="userinfo-modal">
      <div className="modal-tab">
        <button onClick={() => setView('info')} className={view === 'info' ? 'btn type2' : "btn"}>내 정보 수정</button>
        <button onClick={() => setView('password')} className={view === 'password' ? 'btn type2' : "btn"}>비밀번호 변경</button>
      </div>

      {view === 'info' ? (
        <UserInfoForm onClose={onClose} />
      ) : (
        <PasswordChangeForm onClose={onClose} onSwitchToInfo={() => setView('info')} />
      )}
    </div>
  );
};

export default UserInfoModal;
