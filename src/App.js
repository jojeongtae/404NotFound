import { BrowserRouter, Route,  Routes } from 'react-router-dom';
import './App.css';
import MainLayout from "./pages/MainLayout";
import HomePage from './pages/HomePage';
import BoardPage from './pages/BoardPage';
import { AuthProvider } from './context/AuthContext'; 
import NewBoardPage from './pages/NewBoardPage';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<MainLayout />}>
            {/* HomePage를 게시판 섹션의 부모 라우트로 설정 */}
            <Route path='/' element={<HomePage />}>
              {/* '/' 경로로 접속했을 때 HomePage의 Outlet에 BoardPage가 기본으로 렌더링됨 */}
              <Route index element={<BoardPage />} /> 
              {/* '/:boardId' 경로로 접속했을 때 HomePage의 Outlet에 해당 BoardPage가 렌더링됨 */}
              <Route path=':boardId' element={<BoardPage />} />
              <Route path='/board/new' element={<NewBoardPage />}></Route>
            </Route>
          </Route>
        </Routes>  
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;