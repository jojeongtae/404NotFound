import React, { useEffect, useState } from 'react';
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import ComposeMessageForm from './ComposeMessageForm'; // ComposeMessageForm 임포트

const MailboxForm = ({ onClose }) => {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const username = useSelector(state => state.user.username);
  const [currentView, setCurrentView] = useState('inbox'); // 'inbox' 또는 'compose'

  useEffect(() => {
    if (currentView === 'inbox') { // 'inbox' 뷰일 때만 메시지 불러오기
      const fetchMessages = async () => {
        try {
          setLoading(true);
          setError(null);
          const response = await apiClient.get(`/message/receiver`, username);
          setMessages(response.data);
          console.log('메시지:', response.data);
        } catch (err) {
          console.error('메시지 불러오기 실패:', err);
          setError('메시지를 불러오는 데 실패했습니다.');
        } finally {
          setLoading(false);
        }
      };

      if (username) {
        fetchMessages();
      }
    }
  }, [username, currentView]); // currentView가 변경될 때도 useEffect 실행

  const handleMessageDelete = async (id) => {
    try {
      const res = await apiClient.delete(`/message/${id}`);
      alert("삭제완료");
      // 메시지 삭제 후 목록 새로고침
      setMessages(prevMessages => prevMessages.filter(msg => msg.id !== id));
    } catch (error) {
      console.log(error);
      alert("메시지 삭제 실패.");
    }
  };

  return (
    <div className="message-modal">
      <div className="modal-tab">
        <button onClick={() => setCurrentView('inbox')} className={currentView === 'inbox' ? 'btn type2' : 'btn'}>받은 메시지</button>
        <button onClick={() => setCurrentView('compose')} className={currentView === 'compose' ? 'btn type2' : 'btn'}>메시지 보내기</button>
      </div>

      {currentView === 'inbox' ? (
        <div className="tab-container message-box">
          {loading ? (
            <div style={{ textAlign: 'center', padding: '50px' }}>메시지를 불러오는 중...</div>
          ) : error ? (
            <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>
          ) : messages.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '50px' }}>받은 메시지가 없습니다.</div>
          ) : (
            <ul className="message-list">
              {messages.map((msg) => (
                <li className="message-item">
                  <ul>
                    <li><strong>보낸 사람:</strong> <span className="user-grade">{msg.authorNickname || msg.author}</span> <br /></li>
                    <li><strong>제목:</strong> {msg.title}<br /></li>
                    <li><strong>내용:</strong> {msg.message}<br /></li>
                    <li><span className="time">{new Date(msg.createdAt).toLocaleString()}</span></li>
                    <li><button className="btn red" onClick={() => handleMessageDelete(msg.id)}>삭제하기</button></li>
                  </ul>
                </li>
              ))}
            </ul>
          )}
        </div>
      ) : (
        <ComposeMessageForm onMessageSent={() => setCurrentView('inbox')} /> // 쪽지 전송 후 우편함으로 돌아가기
      )}
      <button onClick={onClose} style={{ marginTop: '20px' }}>닫기</button>
    </div>
  );
};

export default MailboxForm;