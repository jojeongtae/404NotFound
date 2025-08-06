import React from 'react';
import { useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage';
import BoardPageForm from '../features/board/BoardPageForm';

const BoardPage = () => {
  const { boardId } = useParams();
  const validBoardIds = ['free', 'notice', 'qna','info','food','used','voting','quiz','survey']; // 유효한 게시판 ID 목록 정의

  // boardId가 없을 경우 (루트 경로) 404 Not Found 컨셉 메시지 표시
  if (!boardId) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404 Not Found</h1>
        <p>이곳은 404NotFound의 메인 페이지입니다.</p>
      </div>
    );
  }

  // 유효하지 않은 boardId일 경우 NotFoundPage 표시
  if (!validBoardIds.includes(boardId)) {
    return <NotFoundPage />;
  }

  return (
    <>
      <div>
        {/* boardId를 BoardPageForm 컴포넌트에 prop으로 전달 */}
        <BoardPageForm boardId={boardId} /> 
      </div>
    </>
  );
};

export default BoardPage;