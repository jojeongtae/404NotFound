import { BrowserRouter, Route,  Routes } from 'react-router-dom';
// import './App.css';
import './style/default.css';
import MainLayout from "./pages/MainLayout";
import HomePage from './pages/HomePage';
import BoardPage from './pages/BoardPage';
import PostDetailPage from './pages/PostDetailPage';
import { AuthProvider } from './context/AuthContext'; 
import NewBoardPage from './pages/NewBoardPage';
import PostUpdatePage from './pages/PostUpdatePage';

import RankingPage from './pages/RankingPage';
import AdminReportForm from './features/admin/AdminReportForm';
import AdminUserListForm from './features/admin/AdminUserListForm';
import DiceGamePage from './pages/DiceGamePage';
import SelectUserBoard from './features/user/SelectUserBoard';
import SelectUserInfo from './features/user/SelectUserInfo';
import ReportBoardForm from './features/board/ReportBoardForm';
import ReportMy from "./features/board/ReportMy";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path='/' element={<div><MainLayout /></div>}>
            <Route index element={<HomePage />} /> {/* 기본 경로를 HomePage로 설정 */}
            <Route path='board/:boardId' element={<BoardPage />} /> {/* 게시판 목록 페이지 */}
            <Route path='board/:boardId/:postId' element={<PostDetailPage />} /> {/* 게시글 상세 페이지 */}
            <Route path='board/new' element={<NewBoardPage />} /> {/* 새 게시글 작성 페이지 */}
            <Route path='board/:boardId/:postId/edit' element={<PostUpdatePage />}></Route>
            <Route path='board/user/report' element={<ReportBoardForm />}></Route>{/* 신고하기 */}
            <Route path="/board/user/report/:username" element={<ReportMy/>}></Route>{/* 나의 신고 내역 */}
            <Route path='board/admin/list' element={<AdminUserListForm />}></Route>
            <Route path='board/admin/report' element={<AdminReportForm />}></Route>
            <Route path='board/ranking/:type' element={<RankingPage />} />
            <Route path='board/dice' element={<DiceGamePage />} />
            <Route path='user/userinfo/:username' element={<SelectUserInfo />}></Route>
            <Route path='user/board/:username' element={<SelectUserBoard />}></Route>
            
            {/* 다른 페이지 라우트들 */}
            {/* ✅ 카카오 OAuth 콜백 처리용 */}
            <Route path='oauth/kakao' element={<HomePage />} /> 
          
          </Route>
        </Routes>  
    </AuthProvider>
      </BrowserRouter>
    
  );
}

export default App;



