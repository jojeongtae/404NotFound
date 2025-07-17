import React, { useState } from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Modal from '../features/common/Modal';
import LoginForm from '../features/auth/LoginForm';
import SignUpForm from '../features/auth/SignUpForm';
import '../style/MainPage.css'; // 테마 CSS 파일 임포트

const MainLayout = () => {
  const { isLoggedIn, logout } = useAuth();
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(null);

  const openModal = (type) => {
    setModalType(type);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setModalType(null);
  };

  return (
    <div className="main-container">
      <header>
        <Link to="/" className="header-title-container" style={{ textDecoration: 'none' }}>
          <h1 className="glitch-title" data-text="404NotFound">404NotFound</h1>
        </Link>
        <nav className="nav-links">
          {isLoggedIn ? (
            <button onClick={logout} className="nav-link">Logout</button>
          ) : (
            <>
              <button onClick={() => openModal('login')} className="nav-link">Login</button>
              <button onClick={() => openModal('signup')} className="nav-link">SignUp</button>
            </>
          )}
        </nav>
      </header>

      <main>
        {/* Outlet을 통해 현재 경로에 맞는 페이지 컴포넌트가 렌더링됩니다. */}
        {/* 홈페이지의 경우, 특별한 콘텐츠가 없을 수 있습니다. */}
        <Outlet />
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
