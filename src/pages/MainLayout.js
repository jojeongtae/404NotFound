import React, { useEffect, useState } from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Modal from '../features/common/Modal';
import LoginForm from '../features/auth/LoginForm';
import SignUpForm from '../features/auth/SignUpForm';
import UserInfoModal from '../features/auth/UserInfoModal'; // UserInfoModal 임포트
import MailboxForm from '../features/mailbox/MailboxForm'; // MailboxForm 임포트
import { useDispatch, useSelector } from 'react-redux';
import { getFullGradeDescription } from '../features/common/GradeDescriptions';
import apiClient from '../api/apiClient';
import { setUser } from '../features/auth/userSlice';
import SignUpWithCaptcha from '../features/auth/SignUpWithCaptcha.js';

// import '../style/MainPage.css'; // 테마 CSS 파일 임포트
import '../style/layout.css';
import '../style/template.css';
import '../style/common.css';

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
          <div className="wrap">
            <div className="main-header">
              <Link to="/" className="header-title-container" style={{ textDecoration: 'none' }}>
                <h1 className="glitch-title" data-text="404NotFound">404NotFound</h1>
              </Link>
            </div>

            <nav className="nav-links">
              {isLoggedIn ? (
                  <>
                    <Link to="/board/new" className="btn type2 nav-link">글쓰기</Link>
                    <button onClick={() => openModal('userInfo')} className='nav-link'>내 정보 수정</button> {/* 버튼으로 변경 */}
                    <button onClick={() => openModal('mailbox')} className='nav-link'>메시지</button> {/* 이 줄을 추가합니다. */}
                    <button onClick={logout} className="nav-link">Logout</button>
                  </>
              ) : (
                  <>
                    <button onClick={() => openModal('login')} className="btn type2">Login</button>
                    <button onClick={() => openModal('signup')} className="btn">SignUp</button>
                  </>
              )}
            </nav>
          </div>
        </header>
        <div className="user-intro">
          <div className="wrap">
            {isLoggedIn ?
                <>
                  <span className="user-grade">{getFullGradeDescription(user.grade)}</span>
                  <span className="user-message">{user.nickname}님 안녕하세요!</span>
                  <span>현재 포인트 : {user.point}</span>
                </>
                :
                <>
                  <span className="user-grade">{user?.grade ?? "👻 401"}</span>
                  <span className="user-message">{user?.nickname ?? "이름없는 방문자"}님 안녕하세요!</span>
                  <span>현재 포인트 : {user?.point ?? "zero"}</span>
                </>
            }
          </div>
        </div>

        <div id="container">
          <div className="wrap">
            <aside>
              <div className="aside-title">
                <h2>메뉴</h2>
              </div>
              <nav>
                <h3>운영자 게시판</h3>
                <ul className="nav-list">
                  <li><Link to="/board/notice">공지사항</Link></li>
                  {user.role === "ROLE_ADMIN" ?
                      <>
                        <li><Link to="/board/admin/list">유저 정보 조회</Link></li>
                        <li><Link to="/board/admin/report">신고 목록 조회</Link></li>
                      </>:
                      ""}
                </ul>
                <h3>오늘의</h3>
                <ul className="nav-list">
                  <li><Link to="/board/ranking/recommend">Best404추천수</Link></li>
                  <li><Link to="/board/ranking/comments">Best404댓글수</Link></li>
                </ul>
                <h3>일반 게시판</h3>
                <ul className="nav-list">
                  <li><Link to="/board/free">자유 게시판</Link></li>
                  <li><Link to="/board/qna">Q&A 게시판</Link></li>
                  <li><Link to="/board/info">정보 게시판</Link></li>
                  <li><Link to="/board/used">중고거래 게시판</Link></li>
                  <li><Link to="/board/food">먹거리 게시판</Link></li>
                  <li><Link to="/board/user/report">신고 게시판</Link></li>
                </ul>
                <h3>포인트 게시판</h3>
                <ul className="nav-list">
                  <li><Link to="/board/quiz">퀴즈 게시판</Link></li>
                  <li><Link to="/board/voting">OX 게시판</Link></li>
                  <li><Link to="/board/survey">설문조사 게시판</Link></li>
                  <li><Link to="/board/dice">주사위 게임</Link></li>
                </ul>
                <h3>자매 사이트</h3>
                <ul className="nav-list">
                  <li><Link to="http://arirangtrail.duckdns.org/" target="_blank">아리랑 트레일</Link></li>
                </ul>
              </nav>
            </aside>
            <main>
              <Outlet />
            </main>
          </div>
        </div>

        <footer>
          <div className="wrap">
            <p className="subtitle">A digital space for the lost and found.<span className="blinking-cursor"></span></p>
            <p>404Project JJT | CSA | JHY | JSL</p>
            <p>&copy; 2025 404 Not Found. All rights reserved.</p>
          </div>
        </footer>

      {showModal && (
        <Modal onClose={closeModal}>
          {modalType === 'login' && <LoginForm onClose={closeModal} />}
          {modalType === 'signup' && <SignUpWithCaptcha onClose={closeModal} />}
          {modalType === 'userInfo' && <UserInfoModal onClose={closeModal} />} {/* UserInfoModal 렌더링 추가 */}
          {modalType === 'mailbox' && <MailboxForm onClose={closeModal} />} {/* 이 줄을 추가합니다. */}
        </Modal>
      )}
    </div>
  );
};

export default MainLayout;

