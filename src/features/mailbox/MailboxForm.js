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
          console.log('우편함 메시지:', response.data);
        } catch (err) {
          console.error('우편함 메시지 불러오기 실패:', err);
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
    <div style={{ padding: '20px', minWidth: '400px' }}> {/* 모달 크기 조절 */}
      <div style={{ marginBottom: '15px', borderBottom: '1px solid #ccc', paddingBottom: '10px' }}>
        <button 
          onClick={() => setCurrentView('inbox')} 
          style={{ 
            padding: '10px 15px', 
            marginRight: '10px', 
            border: '1px solid #ccc', 
            backgroundColor: currentView === 'inbox' ? '#e0e0e0' : '#f0f0f0',
            cursor: 'pointer'
          }}
        >
          우편함
        </button>
        <button 
          onClick={() => setCurrentView('compose')} 
          style={{ 
            padding: '10px 15px', 
            border: '1px solid #ccc', 
            backgroundColor: currentView === 'compose' ? '#e0e0e0' : '#f0f0f0',
            cursor: 'pointer'
          }}
        >
          쪽지 쓰기
        </button>
      </div>

      {currentView === 'inbox' ? (
        <>
          <h3>우편함</h3>
          {loading ? (
            <div style={{ textAlign: 'center', padding: '50px' }}>메시지를 불러오는 중...</div>
          ) : error ? (
            <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>
          ) : messages.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '50px' }}>받은 메시지가 없습니다.</div>
          ) : (
            <ul style={{ listStyle: 'none', padding: 0, maxHeight: '300px', overflowY: 'auto' }}>
              {messages.map((msg) => (
                <li key={msg.id} style={{ borderBottom: '1px solid #999', padding: '10px 0' }}>
                  <strong>보낸 사람:</strong> {msg.authorNickname || msg.author}<br />
                  <strong>제목:</strong> {msg.title}<br />
                  <strong>내용:</strong> {msg.message}<br />
                  <span style={{ fontSize: '0.8em', color: '#666' }}>{new Date(msg.createdAt).toLocaleString()}</span>
                  <button onClick={() => handleMessageDelete(msg.id)} style={{ marginLeft: '10px', background: 'red', color: 'white', border: 'none', padding: '5px 10px', cursor: 'pointer' }}>삭제하기</button>
                </li>
              ))}
            </ul>
          )}
        </>
      ) : (
        <ComposeMessageForm onMessageSent={() => setCurrentView('inbox')} /> // 쪽지 전송 후 우편함으로 돌아가기
      )}
      <button onClick={onClose} style={{ marginTop: '20px' }}>닫기</button>
    </div>
  );
};

export default MailboxForm;