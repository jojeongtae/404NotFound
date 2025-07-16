import React from 'react';
import { useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage'; // NotFoundPage 임포트

const BoardPage = () => {
  const { boardId } = useParams(); // URL 파라미터에서 boardId를 가져옴
  // 유효한 게시판 ID 목록 정의
  const validBoardIds = ['free', 'notice', 'qna'];

  // boardId가 없을 경우 (루트 경로) 404 Not Found 컨셉 메시지 표시
  if (!boardId) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404 Not Found</h1>
        <p>이곳은 404NotFound의 메인 페이지입니다.</p>
        <p>왼쪽 게시판 목록에서 게시판을 선택해주세요.</p>
      </div>
    );
  }

  // boardId가 유효한 목록에 없는 경우 NotFoundPage 렌더링
  if (!validBoardIds.includes(boardId)) {
    return <NotFoundPage />;
  }

  return (
    <div>
      <h3>{boardId} 게시판</h3>
      <p>이곳은 {boardId} 게시판의 내용이 표시될 공간입니다.</p>
      {/* 실제 게시물 목록이나 상세 내용이 여기에 렌더링됩니다. */}
    </div>
  );
};

export default BoardPage;
