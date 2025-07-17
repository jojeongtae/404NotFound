import React from 'react';
import { useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage';
import BoardPageForm from '../features/board/BoardPageForm'; // BoardPageForm 임포트

const BoardPage = () => {
  const { boardId } = useParams();
  const validBoardIds = ['free', 'notice', 'qna'];

  if (!boardId) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404 Not Found</h1>
        <p>이곳은 404NotFound의 메인 페이지입니다.</p>
        <p>왼쪽 게시판 목록에서 게시판을 선택해주세요.</p>
      </div>
    );
  }

  if (!validBoardIds.includes(boardId)) {
    return <NotFoundPage />;
  }

  return (
    <>
      <div>
        <BoardPageForm boardId={boardId} /> 
      </div>
    </>
  );
};

export default BoardPage;
