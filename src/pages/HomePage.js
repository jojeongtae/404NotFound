import React from 'react';
<<<<<<< HEAD

const HomePage = () => {
  return (
    <div style={{ textAlign: 'center', padding: '50px' }}>
      <h1>환영합니다!</h1>
      <p>이곳은 404NotFound의 메인 페이지입니다.</p>
      <p>왼쪽 게시판 목록을 통해 이동해주세요.</p>
=======
import { Link, Outlet } from 'react-router-dom';

const HomePage = () => {
  return (
    <div style={{ display: 'flex', height: 'calc(100vh - 160px)' }}> {/* 헤더, 푸터 높이 고려 */} 
      {/* 왼쪽 게시판 목록 (사이드바) */}
      <nav style={{ width: '200px', padding: '20px', borderRight: '1px solid #eee', overflowY: 'auto' }}>
        <h3>게시판 목록</h3>
        <ul style={{ listStyle: 'none', padding: 0 }}>
          <li style={{ marginBottom: '10px' }}><Link to="/notice">공지사항</Link></li>
          <li style={{ marginBottom: '10px' }}><Link to="/free">자유 게시판</Link></li>
          <li style={{ marginBottom: '10px' }}><Link to="/qna">Q&A 게시판</Link></li>
          {/* 더 많은 게시판 추가 가능 */}
        </ul>
      </nav>

      {/* 오른쪽 메인 콘텐츠 영역 */}
      <main style={{ flexGrow: 1, padding: '20px', overflowY: 'auto' }}>
        <Outlet /> {/* 게시판 내용이 여기에 렌더링됩니다. */}
      </main>
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
    </div>
  );
};

export default HomePage;