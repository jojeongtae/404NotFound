import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage';
import { useSelector, useDispatch } from 'react-redux'; // useDispatch 추가
import { fetchPostDetailAndComments, submitComment } from '../features/board/boardService';
import apiClient from '../api/apiClient';
import { useAuth } from '../context/AuthContext';
import QuizPostDisplay from '../features/board/QuizPostDisplay';
import SurveyBoardForm from '../features/board/SurveyBoardForm';
import CommentThread from '../components/CommentThread';
import VotingBoardForm from '../features/board/VotingBoardForm';
import { getFullGradeDescription } from '../features/common/GradeDescriptions';
import { setUser } from '../features/auth/userSlice'; // setUser 임포트

const PostDetailPage = () => {
  const { boardId, postId } = useParams();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isRecommended, setIsRecommended] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const username = useSelector(state => state.user.username);
  const { isLoggedIn } = useAuth();
  const [comments, setComments] = useState([]);
  const [newCommentText, setNewCommentText] = useState('');
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

  const handleDeletePost = async () => {
    try {
      const result = window.confirm("정말 삭제하시겠습니까?");
      if (result) {
        const res = await apiClient.delete(`/${boardId}/${postId}`);
        console.log(res.data);
        alert("삭제가 완료되었습니다");
        navigate(-1);
      } else {
        return;
      }
    } catch (error) {
      console.log(error);
    }
  }

  const loadPostAndComments = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      if (boardId === 'quiz' || boardId === 'survey' || boardId === 'voting') {
        setPost(null);
        const postResponse = await apiClient.get(`/${boardId}/${postId}`);
        setPost(postResponse.data);
        setComments([]);
      } else {
        const { post, comments } = await fetchPostDetailAndComments(boardId, postId, dispatch);
        setPost(post);
        console.log(post);
        const sortedComments = comments.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
        setComments(sortedComments);
      }
    } catch (err) {
      console.error("데이터 불러오기 실패:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [boardId, postId, dispatch]);

  useEffect(() => {
    if (boardId && postId) {
      loadPostAndComments();
    } else {
      setLoading(false);
      setError("잘못된 게시글 경로입니다.");
    }
  }, [boardId, postId, dispatch, loadPostAndComments]);

  const handleRecommend = async () => {
    if (!isLoggedIn) {
      alert("로그인 후 추천할 수 있습니다.");
      return;
    }

    try {
      const res = await apiClient.post(`/${boardId}/${postId}/recommend`);
      alert("추천되었습니다!");
      setIsRecommended(true);
      setPost(prevPost => ({ ...prevPost, recommend: prevPost.recommend + 1 }));
      console.log(res.data);

    } catch (error) {
      if (error.status === 500) {
        alert("추천은 중복이 불가능합니다.");
        setIsRecommended(true);
      }
      alert("추천/추천 취소에 실패했습니다.");
    }
  }

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!isLoggedIn) {
      alert("로그인 후 댓글을 작성할 수 있습니다.");
      return;
    }
    if (!newCommentText.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    try {
      await submitComment(boardId, postId, username, newCommentText, null);
      setNewCommentText('');
      const { comments: updatedComments } = await fetchPostDetailAndComments(boardId, postId, dispatch);
      setComments(updatedComments);
      alert("댓글이 작성되었습니다!");

      // --- 추가된 부분: 사용자 정보 업데이트 ---
      const fetchUserInfo = async () => {
        try {
          const userInfoRes = await apiClient.get(`/user/user-info?username=${username}`);
          dispatch(setUser(userInfoRes.data));
          console.log("User info updated after comment submission:", userInfoRes.data);
        } catch (userInfoError) {
          console.error("Failed to fetch user info after comment submission:", userInfoError);
        }
      };
      fetchUserInfo();
      // --- 추가된 부분 끝 ---

    } catch (err) {
      console.error("댓글 작성 실패:", err);
      alert(err.message || "댓글 작성에 실패했습니다.");
    }
  };

  const handleDeleteComment = async (commentId) => {
    try {
      const confirmDelete = window.confirm("정말 이 댓글을 삭제하시겠습니까?");
      if (!confirmDelete) {
        return;
      }

      await apiClient.delete(`${boardId}/comments/${commentId}`);
      alert("댓글이 삭제되었습니다.");
      const { comments: updatedComments } = await fetchPostDetailAndComments(boardId, postId, dispatch);
      setComments(updatedComments);
    } catch (err) {
      console.error("댓글 삭제 실패:", err);
      alert("댓글 삭제에 실패했습니다.");
    }
  };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>게시글을 불러오는 중...</div>;
  }

  if (error) {
    return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
  }

  if (!post) {
    return <NotFoundPage />;
  }
  const handleReport = async () => {
    try {
      const res = await apiClient.post("/user/report", {
        reason: "간편신고",
        reporter: username,
        reported: post.author,
        targetTable: `board_${boardId}`,
        targetId: postId
      });
      console.log(res.data);
      alert("신고 완료");
    } catch (err) {
      console.log(err);
    }
  }


  return (
    <>
      {boardId === 'quiz' ? (
        <QuizPostDisplay post={post} />
      ) : boardId === 'survey' ? (
        <SurveyBoardForm />
      ) : boardId === 'voting' ? (
        <VotingBoardForm />
      ) : (
        <div className="board-detail">
          <h3>{post.title}</h3>
          <div className="board-detail-header">
            <ul>
              <li>작성자: <span className="user-grade">{getFullGradeDescription(post.grade)}</span>{post.authorNickname}</li>
              <li>작성일: {new Date(post.createdAt).toLocaleDateString()}</li>
              <li>조회수: {post.views}</li>
            </ul>
          </div>
          <div className="board-detail-body">
            {post.imgsrc && (
                <div style={{ textAlign: 'center', marginBottom: '20px' }}>
                  <img
                      src={`${API_BASE_URL}/${post.imgsrc}`}
                      alt={post.title || '게시글 이미지'}
                      style={{ maxWidth: '100%', height: 'auto', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}
                  />
                </div>
            )}
            {boardId === 'used' && post.price && (
                <p><strong>가격:</strong> {post.price.toLocaleString()}원</p>
            )}
            <div dangerouslySetInnerHTML={{ __html: post.body }}></div>
          </div>
          <div className="board-detail-footer">
            <div className="recommend">
              <button onClick={handleRecommend} disabled={isRecommended}>
                {isRecommended ? `추천함👍${post.recommend}` : `추천👍${post.recommend}`}
              </button>
            </div>
            <div className="report">
              <button className="report-btn" onClick={handleReport}>신고🏮</button>
            </div>
          </div>

          {/* 댓글 섹션 시작 */}
          {boardId !== 'quiz' && boardId !== 'survey' && boardId !== 'voting' && (
            <div className="comment">
              <h4 className="comment-title">💬댓글</h4>
              {comments.length === 0 && <p>댓글이 없습니다. 첫번째 댓글을 입력해 보세요.</p>}
                <CommentThread comments={comments} onCommentUpdate={loadPostAndComments} username={username} handleDeleteComment={handleDeleteComment} />
              <form onSubmit={handleCommentSubmit}>
                <div className="new-comment">
                  <input type="text" value={newCommentText} onChange={(e) => setNewCommentText(e.target.value)} placeholder="댓글을 입력하세요..."/>
                  <button type="submit" className="btn type2">댓글 작성</button>
                </div>
              </form>
            </div>
          )}
          {/* 댓글 섹션 끝 */}
        </div>
      )}
      <div className="btn_wrap">
        {username === post.author && (
          <button className='btn large' onClick={() => navigate(`/board/${boardId}/${postId}/edit`)}>게시글 수정</button>
        )}
        {username === post.author && (
          <button className='btn red large' onClick={handleDeletePost}>게시글 삭제</button>
        )}
      </div>
    </>
  );
};

export default PostDetailPage;

