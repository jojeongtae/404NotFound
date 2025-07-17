import { BrowserRouter, Route,  Routes } from 'react-router-dom';
import './App.css';
import MainLayout from "./pages/MainLayout";
import HomePage from './pages/HomePage';
import BoardPage from './pages/BoardPage';
import PostDetailPage from './pages/PostDetailPage';
import { AuthProvider } from './context/AuthContext'; 
import NewBoardPage from './pages/NewBoardPage';
import PostUpdatePage from './pages/PostUpdatePage';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<MainLayout />}>
            <Route index element={<HomePage />} /> {/* 기본 경로를 HomePage로 설정 */}
            <Route path='board/:boardId' element={<BoardPage />} /> {/* 게시판 목록 페이지 */}
            <Route path='board/:boardId/:postId' element={<PostDetailPage />} /> {/* 게시글 상세 페이지 */}
            <Route path='board/new' element={<NewBoardPage />} /> {/* 새 게시글 작성 페이지 */}
            <Route path='board/:boardId/:postId/edit' element={<PostUpdatePage />}></Route>
            {/* 다른 페이지 라우트들 */}
          </Route>
        </Routes>  
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;