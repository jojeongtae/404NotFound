import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import NotFoundPage from './NotFoundPage';
import { useSelector } from 'react-redux';
import { fetchPostDetailAndComments /*, submitComment */ } from '../features/board/boardService'; // 함수 임포트
import { useDispatch } from 'react-redux';
import apiClient from '../api/apiClient';

const PostDetailPage = () => {
  const { boardId, postId } = useParams();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const username = useSelector(state => state.user.username);

  // const [comments, setComments] = useState([]); // 댓글 목록 상태
  // const [newCommentText, setNewCommentText] = useState(''); // 새 댓글 텍스트 상태
  // const [commentLoading, setCommentLoading] = useState(false); // 댓글 로딩 상태
  // const [commentError, setCommentError] = useState(null); // 댓글 에러 상태
const handleDeletePost= async()=>{
  try {
    const result = window.confirm("정말 삭제하시겠습니까?");
    if(result){
    const res = await apiClient.delete(`/board/${postId}`);
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

  // const handleCommentSubmit = async (e) => {
  //   e.preventDefault();
  //   try {
  //     setCommentLoading(true);
  //     setCommentError(null);
  //     await submitComment(boardId, postId, username, newCommentText);
  //     setNewCommentText('');
  //     await loadPostAndComments(); // 댓글 목록 새로고침
  //     alert("댓글이 성공적으로 작성되었습니다.");
  //   } catch (err) {
  //     console.error("댓글 작성 실패:", err);
  //     setCommentError(err.message);
  //     alert(err.message);
  //   } finally {
  //     setCommentLoading(false);
  //   }
  // };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>게시글을 불러오는 중...</div>;
  }

  if (error) {
    return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
  }

  if (!post) {
    return <NotFoundPage />;
  }

  return (
    <>
      <div style={{ padding: '20px' }}>
        <h2>제목: {post.title}</h2>
        <p><strong>작성자:</strong> {post.author}</p>
        <p><strong>작성일:</strong> {new Date(post.createdAt).toLocaleDateString()}</p>
        <p>조회수: {post.views}</p>
        <hr />
        <div>내용: {post.body}</div>

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

