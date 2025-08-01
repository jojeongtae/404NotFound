import React, { useState } from 'react';
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';

const ComposeMessageForm = ({ onMessageSent }) => {
  const [receiverUsername, setReceiverUsername] = useState('');
  const [title, setTitle] = useState('');
  const [message, setMessage] = useState('');
  const [sending, setSending] = useState(false);
  const [error, setError] = useState(null);
  const [receiverNickname, setReceiverNickname] = useState('');
  const senderUsername = useSelector(state => state.user.username);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!receiverUsername || !title || !message) {
      alert('모든 필드를 입력해주세요.');
      return;
    }

    setSending(true);
    setError(null);

    try {
      const response = await apiClient.post('/message/send', {
        author: senderUsername,
        receiver: receiverUsername,
        title: title,
        message: message,
      });
      console.log('메시지 전송 성공:', response.data);
      alert('메시지가 성공적으로 전송되었습니다!');
      onMessageSent(); // 메시지 전송 후 부모 컴포넌트에 알림
    } catch (err) {
      console.error('메시지 전송 실패:', err);
      setError(err.response?.data?.message || '메시지 전송에 실패했습니다.');
      alert(err.response?.data?.message || '메시지 전송에 실패했습니다.');
    } finally {
      setSending(false);
    }
  };
  const handleNicknameUsername= async(nick)=>{
    try {
      const res = await apiClient.get(`/user/nickname/${nick}`);
      setReceiverUsername(res.data.username);
      alert("확인되었습니다");
      console.log(res.data);
    } catch (err) {
      alert("닉네임을 찾지 못했습니다");
      console.log(err);
    }
  }

  return (
    <div className="tab-container send-message">
      <form onSubmit={handleSubmit}>
        <ul className="send-list">
          <li className="send-item nickname">
            <label htmlFor="receiver">받는 사람 (닉네임):</label>
            <button type="button" onClick={()=>{handleNicknameUsername(receiverNickname)}}>닉네임 체크</button>
            <input
                type="text"
                id="receiver"
                value={receiverNickname}
                onChange={(e) => setReceiverNickname(e.target.value)}
                disabled={sending}
            />
          </li>
          <li className="send-item">
          <label htmlFor="title">제목:</label>
            <input
                type="text"
                id="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                disabled={sending}
            />
          </li>
          <li className="send-item">
          <label htmlFor="message">내용:</label>
            <textarea
                id="message"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                rows="5"
                disabled={sending}
            ></textarea>
          </li>
        </ul>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <div className="btn_wrap">
          <button type="submit" disabled={sending} className="send-btn">{sending ? '전송 중...' : '보내기'}</button>
        </div>
      </form>
    </div>
  );
};

export default ComposeMessageForm;
