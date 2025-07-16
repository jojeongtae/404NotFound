import React from 'react';
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
    </div>
  );
};

export default HomePage;