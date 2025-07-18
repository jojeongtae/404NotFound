<<<<<<< HEAD
import React from 'react';
import { useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage';
import BoardPageForm from '../features/board/BoardPageForm'; // BoardPageForm 임포트

const BoardPage = () => {
  const { boardId } = useParams();
  const validBoardIds = ['free', 'notice', 'qna'];

=======
import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage'; // NotFoundPage 임포트
import apiClient from '../api/apiClient';
import BoardPageForm from '../features/board/BoardPageForm';

const BoardPage = () => {
  const { boardId } = useParams(); // URL 파라미터에서 boardId를 가져옴
  // 유효한 게시판 ID 목록 정의
  const validBoardIds = ['free', 'notice', 'qna'];

  // boardId가 없을 경우 (루트 경로) 404 Not Found 컨셉 메시지 표시
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
  if (!boardId) {
    return (
      <div style={{ textAlign: 'center', padding: '50px' }}>
        <h1>404 Not Found</h1>
        <p>이곳은 404NotFound의 메인 페이지입니다.</p>
        <p>왼쪽 게시판 목록에서 게시판을 선택해주세요.</p>
      </div>
    );
  }

<<<<<<< HEAD
=======
  // boardId가 유효한 목록에 없는 경우 NotFoundPage 렌더링
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
  if (!validBoardIds.includes(boardId)) {
    return <NotFoundPage />;
  }

<<<<<<< HEAD
  return (
    <>
      <div>
        <BoardPageForm boardId={boardId} /> 
=======


  return (
    <>
      <div>
        <h3>{boardId} 게시판</h3>
        <BoardPageForm></BoardPageForm>

>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
      </div>
    </>
  );
};

export default BoardPage;
