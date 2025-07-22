import React, { useState } from 'react';
import { submitComment } from '../features/board/boardService';
import { useParams } from 'react-router-dom';

const Comment = ({ comment, onReply, username, handleDeleteComment }) => {
  const [replying, setReplying] = useState(false);
  const [replyText, setReplyText] = useState('');
  const { boardId, postId } = useParams();

  const handleReplySubmit = async (e) => {
    e.preventDefault();
    try {
      await submitComment(boardId, postId, username, replyText, comment.id);
      setReplyText('');
      setReplying(false);
      onReply(); // 댓글 목록 새로고침
    } catch (error) {
      console.error('Failed to submit reply:', error);
      alert('Failed to submit reply.');
    }
  };

  return (
    <li style={{ borderBottom: '1px solid #eee', padding: '10px 0', marginLeft: comment.parentId ? '20px' : '0' }}>
      {/* authorNickname 변경예정 */}
      <div><strong>작성자 :</strong> {comment.authorNickname}</div>
      <div><strong>내용 :</strong> {comment.content}</div>
      <div style={{ textAlign: 'right', fontSize: '0.8em', color: '#666' }}>작성시간 : {new Date(comment.createdAt).toLocaleString()}</div>
      {username && <button onClick={() => setReplying(!replying)}>답글 달기</button>}
      {username === comment.author && (
        <button onClick={() => handleDeleteComment(comment.id)} style={{ marginLeft: '10px', background: 'red', color: 'white', border: 'none', padding: '5px 10px', cursor: 'pointer' }}>삭제</button>
      )}
      {replying && (
        <form onSubmit={handleReplySubmit} style={{ marginTop: '10px' }}>
          <textarea
            value={replyText}
            onChange={(e) => setReplyText(e.target.value)}
            placeholder="답글을 입력하세요..."
            rows="2"
            style={{ width: '100%', padding: '5px', marginBottom: '5px', border: '1px solid #ccc' }}
          />
          <button type="submit">답글 작성</button>
        </form>
      )}
      {comment.children && comment.children.length > 0 && (
        <ul>
          {comment.children.map(child => (
            <Comment key={child.id} comment={child} onReply={onReply} username={username} handleDeleteComment={handleDeleteComment} />
          ))}
        </ul>
      )}
    </li>
  );
};

const CommentThread = ({ comments, onCommentUpdate, username, handleDeleteComment }) => {
  const buildCommentTree = (commentList) => {
    const commentMap = {};
    const tree = [];

    commentList.forEach(comment => {
      commentMap[comment.id] = { ...comment, children: [] };
    });

    commentList.forEach(comment => {
      if (comment.parentId) {
        if (commentMap[comment.parentId]) {
          commentMap[comment.parentId].children.push(commentMap[comment.id]);
        } else {
          console.warn(`Parent comment with ID ${comment.parentId} not found for comment ID ${comment.id}. Adding to top level.`);
          tree.push(commentMap[comment.id]); // 부모를 찾지 못하면 최상위로 추가
        }
      } else {
        tree.push(commentMap[comment.id]);
      }
    });

    return tree;
  };

  const commentTree = buildCommentTree(comments);

  return (
    <ul>
      {commentTree.map(comment => (
        <Comment key={comment.id} comment={comment} onReply={onCommentUpdate} username={username} handleDeleteComment={handleDeleteComment} />
      ))}
    </ul>
  );
};

export default CommentThread;
