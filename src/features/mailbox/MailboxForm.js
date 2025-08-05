import React, { useEffect, useState } from 'react';
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import ComposeMessageForm from './ComposeMessageForm';

const MailboxForm = ({ onClose, messages, setMessages }) => { // 🔹 props로 messages, setMessages 추가 (부모와 상태 공유)
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const username = useSelector(state => state.user.username);
  const [currentView, setCurrentView] = useState('inbox'); // 'inbox' or 'compose'

  // 받은 메시지 가져오기
  const fetchMessages = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await apiClient.get(`/message/receiver`, username);
      setMessages(response.data); // 🔹 부모 상태도 함께 갱신
      console.log('메시지:', response.data);
    } catch (err) {
      console.error('메시지 불러오기 실패:', err);
      setError('메시지를 불러오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // currentView가 inbox일 때 메시지 로드
  useEffect(() => {
    if (currentView === 'inbox' && username) {
      fetchMessages();
    }
  }, [username, currentView]);

  // 메시지 삭제
  const handleMessageDelete = async (id) => {
    try {
      await apiClient.delete(`/message/${id}`);
      // 🔹 삭제 후 로컬 + 부모 상태를 즉시 갱신 (새로 GET 안함)
      const updatedMessages = messages.filter(msg => msg.id !== id);
      setMessages(updatedMessages);
      alert("삭제 완료");
    } catch (err) {
      console.error('메시지 삭제 실패:', err);
      alert("메시지 삭제 실패.");
    }
  };

  // 받은 메시지 목록 UI (함수로 분리) 🔹 가독성 향상
  const renderInbox = () => {
    if (loading) return <p className="notice">메시지를 불러오는 중...</p>;
    if (error) return <p className="notice red">{error}</p>;
    if (messages.length === 0) return <p className="notice">받은 메시지가 없습니다.</p>;

    return (
      <ul className="message-list">
        {messages.map((msg) => (
          <li className="message-item" key={msg.id}> {/* 🔹 key 추가 */}
            <ul>
              <li>
                <em>보낸 사람:</em>{' '}
                <span className="user-grade">{msg.authorNickname || msg.author}</span>
              </li>
              <li><em>제목:</em> {msg.title}</li>
              <li><em>내용:</em> {msg.message}</li>
              <li><span className="time">{new Date(msg.createdAt).toLocaleString()}</span></li>
            </ul>
            <button
              className="btn red small delete"
              onClick={() => handleMessageDelete(msg.id)}
            >
              삭제
            </button>
          </li>
        ))}
      </ul>
    );
  };

  return (
    <div className="message-modal">
      {/* 🔹 탭 버튼 */}
      <div className="modal-tab">
        <button
          onClick={() => setCurrentView('inbox')}
          className={currentView === 'inbox' ? 'btn type2' : 'btn'}
        >
          받은 메시지
        </button>
        <button
          onClick={() => setCurrentView('compose')}
          className={currentView === 'compose' ? 'btn type2' : 'btn'}
        >
          메시지 보내기
        </button>
      </div>

      {/* 🔹 뷰 렌더링 */}
      <div className="tab-container message-box">
        {currentView === 'inbox'
          ? renderInbox()
          : <ComposeMessageForm onMessageSent={() => setCurrentView('inbox')} />}
      </div>
    </div>
  );
};

export default MailboxForm;
