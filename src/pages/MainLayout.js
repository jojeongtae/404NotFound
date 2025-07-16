import React, { useState } from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Modal from '../features/common/Modal'; // Modal 컴포넌트 임포트
import LoginForm from '../features/auth/LoginForm'; // LoginForm 임포트
import SignUpForm from '../features/auth/SignUpForm'; // SignUpForm 임포트

const MainLayout = () => {
  const { isLoggedIn, logout } = useAuth();
  const [showModal, setShowModal] = useState(false); // 모달 표시 여부 상태
  const [modalType, setModalType] = useState(null); // 'login' 또는 'signup'

  const openModal = (type) => {
    setModalType(type);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setModalType(null);
  };

  return (
    <div>
      {/* 1. 헤더 영역: 로고와 네비게이션만 포함 */}
      <header style={{ padding: '20px', borderBottom: '1px solid #ccc', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <Link to="/" style={{ textDecoration: 'none', color: 'black' }}>
          <h1>404NotFound</h1>
        </Link>
        <nav>
          {isLoggedIn ? (
            <button onClick={logout} style={{ marginRight: '10px' }}>Logout</button>
          ) : (
            <>
              <button onClick={() => openModal('login')} style={{ marginRight: '10px' }}>Login</button>
              <button onClick={() => openModal('signup')} style={{ marginRight: '10px' }}>SignUp</button>
            </>
          )}
        </nav>
      </header>

      {/* 2. 메인 콘텐츠를 표시할 영역 */}
      <main style={{ padding: '20px', minHeight: '70vh' }}>
        <Outlet />
      </main>

      {/* 3. 푸터 영역 */}
      <footer style={{ padding: '20px', borderTop: '1px solid #ccc', textAlign: 'center' }}>
        <p>&copy; 2025 404NotFound. All rights reserved.</p>
      </footer>

      {/* 모달 렌더링 */}
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
