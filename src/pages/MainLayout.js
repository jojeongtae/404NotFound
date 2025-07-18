
import React, { useEffect, useState } from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Modal from '../features/common/Modal';
import LoginForm from '../features/auth/LoginForm';
import SignUpForm from '../features/auth/SignUpForm';
import '../style/MainPage.css'; // 테마 CSS 파일 임포트
import { clearToken } from '../features/auth/tokenSlice';
import { clearUser } from '../features/auth/userSlice';
import { useDispatch } from 'react-redux'; // useDispatch는 계속 필요
import { clearPost } from '../features/board/boardSlice';

const MainLayout = () => {
  const { isLoggedIn, logout } = useAuth();
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(null);
  const dispatch = useDispatch(); // useDispatch는 계속 필요
  const openModal = (type) => {
    setModalType(type);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setModalType(null);
  };


  // useEffect(() => {
  //   dispatch(clearToken());
  //   dispatch(clearUser());
  //   dispatch(clearPost());
  // }, [dispatch]);

  return (
    <div className="main-container">
      <header>
        <Link to="/" className="header-title-container" style={{ textDecoration: 'none' }}>
          <h1 className="glitch-title" data-text="404NotFound">404NotFound</h1>
        </Link>
        <nav className="nav-links">

          {isLoggedIn ? (
            <>
              <Link to="/board/new" className="nav-link">글쓰기</Link>
              <Link to={"/user/userinfo"} className='nav-link'>내 정보 확인</Link>
              <button onClick={logout} className="nav-link">Logout</button>
            </>
              )
            : (<>
              <button onClick={() => openModal('login')} className="nav-link">Login</button>
              <button onClick={() => openModal('signup')} className="nav-link">SignUp</button>
            </>
          )}
        </nav>
      </header>


      <main style={{ display: 'flex', flexGrow: 1 }}>
        {/* 왼쪽 게시판 목록 (사이드바) */}
        <nav style={{ width: '200px', padding: '20px', borderRight: '1px solid #eee', overflowY: 'auto' }}>
          <h3>게시판 목록</h3><br />
          <ul style={{ listStyle: 'none', padding: 0 }}>
            <li style={{ marginBottom: '10px' }}><span>--운영자 게시판 목록--</span></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/notice">공지사항</Link></li>
            <li style={{ marginBottom: '10px' }}><span>--일반 게시판 목록--</span></li>

            <li style={{ marginBottom: '10px' }}><Link to="/board/free">자유 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/qna">Q&A 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/info">정보 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/used">중고거래 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/food">먹거리 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><span>--포인트 게시판 종류--</span></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/quiz">퀴즈 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/ox">OX 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/servy">설문조사 게시판</Link></li>
            {/* 더 많은 게시판 추가 가능 */}
          </ul>
        </nav>

        {/* 오른쪽 메인 콘텐츠 영역 */}
        <div style={{ flexGrow: 1, padding: '20px', overflowY: 'auto' }}>
          <Outlet /> {/* 게시판 내용이 여기에 렌더링. */}
        </div>
      </main>


      <footer>
        <p className="subtitle">
          A digital space for the lost and found<span className="blinking-cursor"></span>
        </p>
      </footer>

      {showModal && (
        <Modal onClose={closeModal}>
          {modalType === 'login' && <LoginForm onClose={closeModal} />}
          {modalType === 'signup' && <SignUpForm onClose={closeModal} />}
        </Modal>
      )}
    </div>
  );
};

export default MainLayout;
