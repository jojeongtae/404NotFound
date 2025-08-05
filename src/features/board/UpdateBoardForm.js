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
        // TODO: 게시글 상세 정보 API 경로 확인
        const response = await apiClient.get(`/${boardId}/${postId}`); // 게시글 상세 정보 불러오기
        const postData = response.data;

        // 작성자 확인
        if (loggedInUsername !== postData.author) {
          alert('게시글 수정 권한이 없습니다.');
          navigate(`/board/${boardId}/${postId}`); // 게시글 상세 페이지로 리디렉션
          return;
        }

        setTitle(postData.title);
        setBody(postData.body);
        setOriginalAuthor(postData.author); // 원본 작성자 저장
      } catch (err) {
        console.error('게시글 불러오기 실패:', err);
        setError('게시글을 불러오는 데 실패했습니다.');
        alert('게시글을 불러오는 데 실패했습니다.');
        navigate(`/board/${boardId}`); // 게시판 목록으로 리디렉션
      } finally {
        setLoading(false);
      }
    };

    if (boardId && postId) {
      fetchPost();
    } else {
      setLoading(false);
      setError('잘못된 게시글 경로입니다.');
    }
  }, [boardId, postId, loggedInUsername, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    let imageUrl = null; // 이미지 URL을 저장할 변수

    if (!title.trim() || !body.trim()) {
      alert('제목과 내용을 입력해주세요.');
      return;
    }


    try {
      if (selectedImage) {
        const imageFormData = new FormData();
        imageFormData.append('file', selectedImage); // 백엔드에서 'file' 필드명으로 받도록 가정

        console.log("이미지 업로드 시작...");
        const imageUploadRes = await apiClient.post('/upload', imageFormData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        console.log("이미지 업로드 응답:", imageUploadRes.data);

        let cleanedPath = imageUploadRes.data.filePath.replace(/\\/g, '/'); // 모든 백슬래시를 슬래시로 변경
        if (cleanedPath.startsWith('uploads/')) {
          cleanedPath = cleanedPath.substring('uploads/'.length);
        }
        imageUrl = '/resources/' + cleanedPath;
        if (!imageUrl) {
          throw new Error("이미지 URL을 받아오지 못했습니다.");
        }
      }
      setLoading(true);
      const updatedPost = {
        title,
        body,
        author: originalAuthor, // 작성자는 변경되지 않음
        imgsrc:imageUrl
      };
      // TODO: 게시글 수정 API 경로 확인 (PUT 또는 PATCH)
      await apiClient.put(`/${boardId}/${postId}`, updatedPost);
      dispatch(setPostDetails({ ...updatedPost, id: postId }))
      alert('게시글이 성공적으로 수정되었습니다!');
      navigate(`/board/${boardId}/${postId}`); // 수정된 게시글 상세 페이지로 이동

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
    <div className="update-board">
      <h3>게시글 수정</h3>
      <form onSubmit={handleSubmit}>
        <div className="update-container">
          <div className="board-title">
            <label htmlFor="title">제목</label>
            <input type="text" id="title" value={title} onChange={(e) => setTitle(e.target.value)} placeholder="제목을 입력하세요" />
          </div>
          <div className="board-body">
            <label htmlFor="body">내용</label>
            <textarea id="body" value={body} onChange={(e) => setBody(e.target.value)} placeholder="내용을 입력하세요" rows="10"></textarea>
          </div>
          <div className="board-image">
            <input type='file' accept="image/*" /> {/*이미지 파일만 선택 가능하도록 onChange={handleImageChange}*/}
            {imagePreviewUrl && ( // 이미지 미리보기
                <div className="image-preview">
                  <img src={imagePreviewUrl} alt="Image Preview" />
                </div>
            )}
          </div>
        </div>
        <div className="btn_wrap">
          <button type="submit" className="btn type2 large" disabled={loading}>수정 완료</button>
          <button type="button" className="btn large" onClick={() => navigate(-1)}>취소</button>
        </div>
      </form>
    </div>
  );
};

export default UpdateBoardForm;