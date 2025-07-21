import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage';
import { useSelector } from 'react-redux';
import { fetchPostDetailAndComments /*, submitComment */ } from '../features/board/boardService'; // 함수 임포트
import { useDispatch } from 'react-redux';
import apiClient from '../api/apiClient';
import { useAuth } from '../context/AuthContext'; // useAuth 임포트
import QuizPostDisplay from '../features/board/QuizPostDisplay'; // QuizPostDisplay 임포트
import SurveyBoardForm from '../features/board/SurveyBoardForm'; // SurveyBoardForm 임포트

const PostDetailPage = () => {
  const { boardId, postId } = useParams();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isRecommended, setIsRecommended] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const username = useSelector(state => state.user.username);
  const { isLoggedIn } = useAuth(); // isLoggedIn 상태 가져오기

  const handleDeletePost= async()=>{
    try {
      const result = window.confirm("정말 삭제하시겠습니까?");
      if(result){
      const res = await apiClient.delete(`/${boardId}/${postId}`);
      console.log(res.data);
      alert("삭제가 완료되었습니다");
      navigate(-1);
      }else{
        return;
      }
    } catch (error) {
      console.log(error);
    }
  }

  useEffect(() => {
    const loadPostAndComments = async () => {
      try {
        setLoading(true);
        setError(null);
        const { post /*, comments */ } = await fetchPostDetailAndComments(boardId, postId, dispatch);
        setPost(post);
        console.log(post);
        // setComments(comments);
      } catch (err) {
        console.error("데이터 불러오기 실패:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    if (boardId && postId) {
      loadPostAndComments();
    } else {
      setLoading(false);
      setError("잘못된 게시글 경로입니다.");
    }
  }, [boardId, postId, dispatch]);

  const handleRecommend = async() => {
    if (!isLoggedIn) {
      alert("로그인 후 추천할 수 있습니다.");
      return;
    }

    try {
      let res;
      if (isRecommended) { // 이미 추천한 상태라면 추천 취소
        res = await apiClient.patch(`/${boardId}/${postId}/cancel_recommend`);
        alert("추천이 취소되었습니다!");
        setIsRecommended(false);
        setPost(prevPost => ({ ...prevPost, recommend: prevPost.recommend - 1 })); // 추천수 감소
      } else { // 추천하지 않은 상태라면 추천
        res = await apiClient.patch(`/${boardId}/${postId}/recommend`);
        alert("추천되었습니다!");
        setIsRecommended(true);
        setPost(prevPost => ({ ...prevPost, recommend: prevPost.recommend + 1 })); // 추천수 증가
      }
      console.log(res.data);
    } catch (error) {
      console.error("추천/추천 취소 실패:", error);
      alert("추천/추천 취소에 실패했습니다.");
    }
  }

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>게시글을 불러오는 중...</div>;
  }

  if (error) {
    return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
  }

  if (!post) {
    return <NotFoundPage />;
  }
  const handleReport = async() =>{
    try {
      const res = await apiClient.post("/user/report",{
        reason:"간단신고",
        reporter:username,
        reported:post.author,
        targetTable:`board_${boardId}`,
        targetId:postId
      });
      console.log(res.data);
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
      ) : (
        <div style={{ padding: '20px' }}>
          <h2>제목: {post.title}</h2>
          <p><strong>작성자:</strong> {post.author}</p>
          <p><strong>작성일:</strong> {new Date(post.createdAt).toLocaleDateString()}</p>
          <p>조회수: {post.views}</p>
          <hr />
          <div dangerouslySetInnerHTML={{ __html: post.body }}></div>

          <hr />
          <button onClick={handleRecommend}>
            {isRecommended ? "추천 취소" : "추천"}
          </button>
          <span>추천수 : {post.recommend}</span> <button onClick={handleReport}>🏮신고하기</button>
          <hr />

          {/* 댓글 섹션 주석 처리 */}
          {/*
        <hr style={{ marginTop: '30px' }} />
        <h3>댓글</h3>
        {commentLoading && <p>댓글 불러오는 중...</p>}
        {commentError && <p style={{ color: 'red' }}>{commentError}</p>}
        {!commentLoading && comments.length === 0 && <p>아직 댓글이 없습니다.</p>}
        
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {comments.map(comment => (
            <li key={comment.id} style={{ borderBottom: '1px solid #eee', padding: '10px 0' }}>
              <p><strong>{comment.author}:</strong> {comment.text}</p>
              <small>{new Date(comment.createdAt).toLocaleString()}</small>
            </li>
          ))}
        </ul>

        <form onSubmit={handleCommentSubmit} style={{ marginTop: '20px' }}>
          <textarea
            value={newCommentText}
            onChange={(e) => setNewCommentText(e.target.value)}
            placeholder="댓글을 입력하세요..."
            rows="3"
            style={{ width: '100%', padding: '10px', marginBottom: '10px', border: '1px solid #ccc' }}
          ></textarea>
          <button type="submit" className="nav-link" disabled={commentLoading}>
            댓글 작성
          </button>
        </form>
        */}
        </div>
      )}
      <div>
        {username === post.author && (
          <button onClick={() => navigate(`/board/${boardId}/${postId}/edit`)} className='nav-link'>게시글 수정</button>

        )}
        {username === post.author && (

          <button className='nav-link' onClick={handleDeletePost}>게시글 삭제</button>
        )}
      </div>
    </>
  );
};

export default PostDetailPage;

