<<<<<<< HEAD
import React, { useState } from 'react'; // useEffect 제거
=======
import React, { useState } from 'react';
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Modal from '../features/common/Modal';
import LoginForm from '../features/auth/LoginForm';
import SignUpForm from '../features/auth/SignUpForm';
import '../style/MainPage.css'; // 테마 CSS 파일 임포트
<<<<<<< HEAD
import { clearToken } from '../features/auth/tokenSlice';
import { clearUser } from '../features/auth/userSlice';
import { useDispatch } from 'react-redux'; // useDispatch는 계속 필요
=======
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7

const MainLayout = () => {
  const { isLoggedIn, logout } = useAuth();
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(null);
<<<<<<< HEAD
  const dispatch = useDispatch(); // useDispatch는 계속 필요
=======
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7

  const openModal = (type) => {
    setModalType(type);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setModalType(null);
  };

<<<<<<< HEAD
  // useEffect 블록을 제거합니다.
  // useEffect(() => {
  //   dispatch(clearToken());
  //   dispatch(clearUser());
  // }, []);
=======
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
  return (
    <div className="main-container">
      <header>
        <Link to="/" className="header-title-container" style={{ textDecoration: 'none' }}>
          <h1 className="glitch-title" data-text="404NotFound">404NotFound</h1>
        </Link>
        <nav className="nav-links">
          {isLoggedIn ? (
<<<<<<< HEAD
            <>
              <Link to="/board/new" className="nav-link">글쓰기</Link>
              <button onClick={logout} className="nav-link">Logout</button>
            </>
          ) : (
=======
              <>
            <button onClick={logout} className="nav-link">Logout</button>
              <Link to={"/user/userinfo"}>내 정보 확인</Link>
              </>
            ) : (
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
            <>
              <button onClick={() => openModal('login')} className="nav-link">Login</button>
              <button onClick={() => openModal('signup')} className="nav-link">SignUp</button>
            </>
          )}
        </nav>
      </header>

<<<<<<< HEAD
      <main style={{ display: 'flex', flexGrow: 1 }}>
        {/* 왼쪽 게시판 목록 (사이드바) */}
        <nav style={{ width: '200px', padding: '20px', borderRight: '1px solid #eee', overflowY: 'auto' }}>
          <h3>게시판 목록</h3>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            <li style={{ marginBottom: '10px' }}><Link to="/board/notice">공지사항</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/free">자유 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/qna">Q&A 게시판</Link></li>
            {/* 더 많은 게시판 추가 가능 */}
          </ul>
        </nav>

        {/* 오른쪽 메인 콘텐츠 영역 */}
        <div style={{ flexGrow: 1, padding: '20px', overflowY: 'auto' }}>
          <Outlet /> {/* 게시판 내용이 여기에 렌더링. */}
        </div>
=======
      <main>
        {/* Outlet을 통해 현재 경로에 맞는 페이지 컴포넌트가 렌더링됩니다. */}
        {/* 홈페이지의 경우, 특별한 콘텐츠가 없을 수 있습니다. */}
        <Outlet />
>>>>>>> 48d3c4166b89da4f911e02ca51d272361f9243d7
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
