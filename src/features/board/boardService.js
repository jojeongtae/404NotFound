import apiClient from '../../api/apiClient';
import { setPostDetails } from './boardSlice';

// 게시글 상세 정보 및 댓글 목록을 불러오는 함수
export const fetchPostDetailAndComments = async (boardId, postId,dispatch) => {
  try {
    // 게시글 상세 정보 불러오기
    const postResponse = await apiClient.get(`/board/${postId}`); // TODO: 실제 백엔드 API 경로 확인
    dispatch(setPostDetails(postResponse.data));
    // 댓글 목록 불러오기
    // TODO: 실제 백엔드 API 경로로 변경하세요. (예: /api/board/{boardId}/posts/{postId}/comments)
    // const commentsResponse = await apiClient.get(`/board/${boardId}/posts/${postId}/comments`); 

    // return { post: postResponse.data, comments: commentsResponse.data };
    return{post:postResponse.data}
  } catch (err) {
    console.error("데이터 불러오기 실패:", err);
    throw new Error("게시글 또는 댓글을 불러오는 데 실패했습니다.");
  }
};

// 새로운 댓글을 작성하는 함수
export const submitComment = async (boardId, postId, username, commentText) => {
  if (!commentText.trim()) {
    throw new Error("댓글 내용을 입력해주세요.");
  }
  if (!username) {
    throw new Error("댓글을 작성하려면 로그인해야 합니다.");
  }

  try {
    const commentData = {
      author: username,
      text: commentText,
      postId: postId, // 백엔드에서 게시글 ID를 필요로 할 경우
    };
    // TODO: 댓글 작성 API 경로 확인
    // await apiClient.post(`/board/${boardId}/posts/${postId}/comments`, commentData);
    return true;
  } catch (err) {
    console.error("댓글 작성 실패:", err);
    throw new Error("댓글 작성에 실패했습니다.");
  }
};
