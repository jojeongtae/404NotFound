import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import apiClient from '../../api/apiClient'; // apiClient 임포트
import { setPostDetails } from './boardSlice';

const UpdateBoardForm = () => {
  const { boardId, postId } = useParams(); // URL 파라미터에서 boardId와 postId 가져오기
  const navigate = useNavigate();
  const loggedInUsername = useSelector(state => state.user.username); // 로그인된 사용자 이름
  const dispatch = useDispatch();
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [originalAuthor, setOriginalAuthor] = useState(null); // 게시글 원본 작성자
  const [selectedImage, setSelectedImage] = useState(null);
  const [imagePreviewUrl, setImagePreviewUrl] = useState('');

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setSelectedImage(file);
      // 이미지 미리보기 URL 생성
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreviewUrl(reader.result);
      };
      reader.readAsDataURL(file);
    } else {
      setSelectedImage(null);
      setImagePreviewUrl('');
    }
  };
  useEffect(() => {

    const fetchPost = async () => {

      try {
        setLoading(true);
        setError(null);
        // API 경로 수정
        const response = await apiClient.get(`/free/${postId}`); // 게시글 상세 정보 불러오기
        const postData = response.data;

        // 작성자 확인
        if (loggedInUsername !== postData.author) {
          alert('게시글 수정 권한이 없습니다.');
          navigate(`/board/free/${postId}`); // 게시글 상세 페이지로 리디렉션
          return;
        }

        setTitle(postData.title);
        setBody(postData.body);
        setOriginalAuthor(postData.author); // 원본 작성자 저장
        setImagePreviewUrl(postData.imgsrc); // 기존 이미지 미리보기 설정
      } catch (err) {
        console.error('게시글 불러오기 실패:', err);
        setError('게시글을 불러오는 데 실패했습니다.');
        alert('게시글을 불러오는 데 실패했습니다.');
        navigate(`/board/free`); // 게시판 목록으로 리디렉션
      } finally {
        setLoading(false);
      }
    };

    if (postId) {
      fetchPost();
    } else {
      setLoading(false);
      setError('잘못된 게시글 경로입니다.');
    }
  }, [postId, loggedInUsername, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!title.trim() || !body.trim()) {
      alert('제목과 내용을 입력해주세요.');
      return;
    }

    setLoading(true);

    try {
      const formData = new FormData();

      // boardDTO를 Blob으로 감싸지 않고, 순수한 JSON 문자열로 추가합니다.
      const boardDTO = {
        title,
        body,
        author: originalAuthor,
      };
      formData.append('boardDTO', JSON.stringify(boardDTO));

      // 이미지가 선택된 경우에만 FormData에 추가
      if (selectedImage) {
        formData.append('file', selectedImage);
      }

      // 백엔드 API 호출
      await apiClient.put(`/free/${postId}`, formData);

      alert('게시글이 성공적으로 수정되었습니다!');
      navigate(`/board/free/${postId}`); // 수정된 게시글 상세 페이지로 이동

    } catch (err) {
      console.error('게시글 수정 실패:', err);
      setError('게시글 수정에 실패했습니다.');
      alert('게시글 수정에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>게시글을 불러오는 중...</div>;
  }

  if (error) {
    return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
  }

  return (
    <div style={{ padding: '20px' }}>
      <h2>게시글 수정</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="title">제목:</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="제목을 입력하세요"
            style={{ width: '100%', padding: '8px', marginBottom: '10px' }}
          />
        </div>
        <div>
          <label htmlFor="body">내용:</label>
          <textarea
            id="body"
            value={body}
            onChange={(e) => setBody(e.target.value)}
            placeholder="내용을 입력하세요"
            rows="10"
            style={{ width: '100%', padding: '8px', marginBottom: '10px' }}
          ></textarea>
        </div>
        <div>
          <input
            type='file'
            accept="image/*" // 이미지 파일만 선택 가능하도록
            onChange={handleImageChange}
          /><br />
          {imagePreviewUrl && ( // 이미지 미리보기
            <div>
              <img src={imagePreviewUrl} alt="Image Preview" style={{ maxWidth: '200px', maxHeight: '200px', marginTop: '10px' }} />
            </div>
          )}
        </div>
        <button type="submit" className="nav-link" disabled={loading}>
          수정 완료
        </button>
        <button type="button" className="nav-link" onClick={() => navigate(-1)} style={{ marginLeft: '10px' }}>
          취소
        </button>
      </form>
    </div>
  );
};

export default UpdateBoardForm;