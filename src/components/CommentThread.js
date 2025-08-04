import React, { useState } from 'react';
import { submitComment } from '../features/board/boardService';
import { useParams } from 'react-router-dom';
import { getFullGradeDescription } from '../features/common/GradeDescriptions';

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
    // <li className="comment-item" style={{marginLeft: comment.parentId ? '25px' : '0' }}>
    <li className="comment-item">
      {/* authorNickname 변경예정 */}
      <div className="comment-header">
        <div>
          <span className="user-grade">{getFullGradeDescription(comment.grade)}</span>
          <span>{comment.authorNickname}</span>
        </div>
        <span className="time">{new Date(comment.createdAt).toLocaleString()}</span>
      </div>
      <div className="comment-body">
        <p>{comment.content}</p>
        <div className="btn_box">
          {username && <button className="small" onClick={() => setReplying(!replying)}>답글 달기</button>}
          {username === comment.author && (<button className="small red" onClick={() => handleDeleteComment(comment.id)}>삭제</button>)}
        </div>
      </div>

      {replying && (
        <form onSubmit={handleReplySubmit}>
          <div className="new-comment">
            <input type="text" onChange={(e) => setReplyText(e.target.value)} placeholder="답글을 입력하세요..." />
            <button type="submit">답글 작성</button>
          </div>
        </form>
      )}
      {comment.children && comment.children.length > 0 && (
        <ul className="reply-comment-list">
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
    <ul className="comment-list">
      {commentTree.map(comment => (
        <Comment key={comment.id} comment={comment} onReply={onCommentUpdate} username={username} handleDeleteComment={handleDeleteComment} />
      ))}
    </ul>
  );
};

export default CommentThread;
