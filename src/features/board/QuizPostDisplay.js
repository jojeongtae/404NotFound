import React, { useState } from 'react';
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { getFullGradeDescription } from '../common/GradeDescriptions';

const QuizPostDisplay = ({ post }) => {
  const [userAnswer, setUserAnswer] = useState('');
  const [isAnswerChecked, setIsAnswerChecked] = useState(false);
  const [isCorrect, setIsCorrect] = useState(false);
  const user = useSelector(state => state.user);


  const handleAnswerSubmit = async(e) => {
    e.preventDefault();
    setIsAnswerChecked(true);
    if (userAnswer.toLowerCase() === post.answer.toLowerCase()) { // 정답은 소문자로 비교
      setIsCorrect(true);
      // 퀴즈 정답을 시도했는지 안했는지 체크
      // const resultResponse = await apiClient.get(`/api/quiz-answers/${}`)

      const res = await apiClient.post("/quiz-answers/new",{
        username: user.username,
        userAnswer:userAnswer,
        quiz_id:post.id,
        result:1
      })
      console.log(res.data);
      if(res.data){
      alert('정답입니다! 포인트를 획득했습니다.'); // 백엔드에서 포인트 지급 로직 필요

      }
    } else {
      const res = await apiClient.post("/quiz-answers/new",{
        username: user.username,
        userAnswer:userAnswer.toLocaleUpperCase(),
        quiz_id:post.id,
        result:0
      })
      console.log(res.data);
      setIsCorrect(false);
      alert('오답입니다.');
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>퀴즈: {post.title}</h2>
      <p><strong>작성자:</strong> {getFullGradeDescription(post.grade)}{post.authorNickname}</p>
      <p><strong>작성일:</strong> {new Date(post.createdAt).toLocaleDateString()}</p>
      <p>조회수: {post.views}</p>
      <hr />
      <div>
        <h3>문제: {post.question}</h3> {/* 퀴즈 문제 표시 */}
        <p>힌트: 아는만큼 보인다</p> {/* 기존 body를 힌트로 활용 */}
      </div>

      <form onSubmit={handleAnswerSubmit}>
        {post.imgsrc &&(
<img
                src={`http://404notfoundpage.duckdns.org/${post.imgsrc}`}
                alt={post.title || '게시글 이미지'}
                style={{ maxWidth: '100%', height: 'auto', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}
              />
        )}
         
        <input
          type="text"
          value={userAnswer}
          onChange={(e) => setUserAnswer(e.target.value)}
          placeholder="정답을 입력하세요"
          disabled={isAnswerChecked && isCorrect} // 정답 맞추면 입력 비활성화
        />
        <button type="submit" disabled={isAnswerChecked}>정답 제출</button>
      </form>

      {isAnswerChecked && !isCorrect && (
        <p style={{ color: 'red' }}>오답입니다. 정답은 "{post.answer}" 입니다.</p> // 오답일 경우 정답 표시
      )}
      {isAnswerChecked && isCorrect && (
        <p style={{ color: 'green' }}>축하합니다! 정답입니다.</p>
      )}

      <hr />
      {/* 추천, 신고 버튼은 PostDetailPage에서 관리 */}
      <hr />
      {/* 댓글 섹션은 퀴즈 게시판에서는 다르게 처리할 수 있음 */}
    </div>
  );
};

export default QuizPostDisplay;