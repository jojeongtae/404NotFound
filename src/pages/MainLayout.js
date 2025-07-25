
import React, { useEffect, useState } from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Modal from '../features/common/Modal';
import LoginForm from '../features/auth/LoginForm';
import SignUpForm from '../features/auth/SignUpForm';
import UserInfoModal from '../features/auth/UserInfoModal'; // UserInfoModal 임포트
import MailboxForm from '../features/mailbox/MailboxForm'; // MailboxForm 임포트
import '../style/MainPage.css'; // 테마 CSS 파일 임포트
import { useDispatch, useSelector } from 'react-redux';
import { getFullGradeDescription } from '../features/common/GradeDescriptions';
import apiClient from '../api/apiClient';
import { setUser } from '../features/auth/userSlice';

// const gradeDescriptions = {
//     "👑 500": "500 Internal Server Error (운영진)",
//     "🐣 404": "404 Not Found (신규)",
//     "👍 200": "200 OK (일반 회원)",
//     "🚀 202": "202 Accepted (활동 회원)",
//     "💎 403": "403 Forbidden (우수 회원)",
//     "👻 401": "401 Unauthorized (손님)"
// };

// export const getFullGradeDescription = (shortGrade) => {
//     return gradeDescriptions[shortGrade] || shortGrade; // 매핑된 값이 없으면 짧은 등급 그대로 반환
// };

const MainLayout = () => {
  const { isLoggedIn, logout } = useAuth();
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(null);
  const dispatch = useDispatch();
  const user = useSelector(state => state.user);
  // const [currentUser,setCurrentUser] = useState(user);
  const openModal = (type) => {
    setModalType(type);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setModalType(null);
  };

  useEffect(() => {
    // 로그인 상태이고, 사용자 정보가 Redux 스토어에 없거나 업데이트가 필요한 경우
    if (isLoggedIn && user.username) { // user.username이 있을 때만 API 호출
      const fetchUserInfo = async () => { // async 함수로 변경
        try {
          const res = await apiClient.get(`/user/user-info?username=${user.username}`); // await 추가
          dispatch(setUser(res.data)); // Redux 스토어 업데이트
        } catch (error) {
          console.error("Failed to fetch user info:", error); // console.log 대신 console.error 사용
        }
      };
      fetchUserInfo();
    }
  }, [isLoggedIn, user.username, dispatch]); // 의존성 배열에 user.username 추가

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
              <button onClick={() => openModal('userInfo')} className='nav-link'>내 정보 수정</button> {/* 버튼으로 변경 */}
              <button onClick={() => openModal('mailbox')} className='nav-link'>우편함</button> {/* 이 줄을 추가합니다. */}
              <button onClick={logout} className="nav-link">Logout</button>
            </>
          ) : (
            <>
              <button onClick={() => openModal('login')} className="nav-link">Login</button>
              <button onClick={() => openModal('signup')} className="nav-link">SignUp</button>
            </>
          )}
        </nav>
      </header>
      {isLoggedIn ?
      <>
          <span style={{textAlign :"left"}}>{getFullGradeDescription(user.grade)} 이름 : {user.nickname}님 안녕하세요!</span> <span>현재 포인트 : {user.point}</span>
      </>
      :
      <>
          <span style={{textAlign :"left"}}>{user?.grade ?? "👻 401"} 이름 : {user?.nickname ??"이름없는 방문자"}님 안녕하세요!</span> <span>현재 포인트 : {user?.point ?? "zero"}</span>
      </>
      }
      <main style={{ display: 'flex', flexGrow: 1 }}>
        <nav style={{ width: '200px', padding: '20px', borderRight: '1px solid #eee', overflowY: 'auto' }}>
          <h3>게시판 목록</h3><br />
          <ul style={{ listStyle: 'none', padding: 0 }}>
            <li style={{ marginBottom: '10px' }}><span>--운영자 게시판 목록--</span></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/notice">공지사항</Link></li>
            {user.role === "ROLE_ADMIN" ?
            <>
            <li style={{ marginBottom: '10px' }}><Link to="/board/admin/list">유저 정보 조회</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/admin/report">신고 목록 조회</Link></li>
            </>:
            ""}
            <li style={{ marginBottom: '10px' }}><span>--오늘의 개추--</span></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/ranking/recommend">Best404추천수</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/ranking/comment">Best404댓글수</Link></li>
            <li style={{ marginBottom: '10px' }}><span>--일반 게시판 목록--</span></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/free">자유 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/qna">Q&A 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/info">정보 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/used">중고거래 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/food">먹거리 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><span>--포인트 게시판 종류--</span></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/quiz">퀴즈 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/voting">OX 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/survey">설문조사 게시판</Link></li>
            <li style={{ marginBottom: '10px' }}><Link to="/board/dice">다이스 도박</Link></li>
            <li style={{ marginBottom: '10px' }}><span>--광고주 링크--</span></li>
            <li style={{ marginBottom: '10px' }}><Link to="http://arirangtrail.duckdns.org/">아리랑 트레일</Link></li>

          </ul>
        </nav>

        <div style={{ flexGrow: 1, padding: '20px', overflowY: 'auto' }}>
          <Outlet />
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
          {modalType === 'userInfo' && <UserInfoModal onClose={closeModal} />} {/* UserInfoModal 렌더링 추가 */}
          {modalType === 'mailbox' && <MailboxForm onClose={closeModal} />} {/* 이 줄을 추가합니다. */}
        </Modal>
      )}
    </div>
  );
};

export default MainLayout;

