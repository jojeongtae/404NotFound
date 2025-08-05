import React, { useState } from 'react';
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { getFullGradeDescription } from '../common/GradeDescriptions';
import {Link} from "react-router-dom";

const QuizPostDisplay = ({ post }) => {
  const [userAnswer, setUserAnswer] = useState('');
  const [isAnswerChecked, setIsAnswerChecked] = useState(false);
  const [isCorrect, setIsCorrect] = useState(false);
  const user = useSelector(state => state.user);
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

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
        result: 1
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
        <div className="quiz-detail">
            <h3>{post.title}</h3>
            <ul className="detail-info">
                <li><span>작성자:</span> <span className="user-grade">{getFullGradeDescription(post.grade)}</span>{post.authorNickname}</li>
                <li><span>작성일:</span> {new Date(post.createdAt).toLocaleDateString()}</li>
                <li><span>조회수:</span> {post.views}</li>
            </ul>
            <p className="question">❓{post.question}</p> {/* 퀴즈 문제 표시 */}
            {post.imgsrc &&(
                <img src={`http://404notfoundpage.duckdns.org/${post.imgsrc}`} alt={post.title || '게시글 이미지'}/>
            )}
            <form onSubmit={handleAnswerSubmit}>
                <div className="answer-box">
                    <p className="hint">💡힌트: 아는만큼 보인다</p> {/* 기존 body를 힌트로 활용 */}
                    <input
                        type="text"
                        value={userAnswer}
                        onChange={(e) => setUserAnswer(e.target.value)}
                        placeholder="정답을 입력하세요."
                        className="answer-input"
                        disabled={isAnswerChecked && isCorrect} // 정답 맞추면 입력 비활성화
                    />
                    <button type="submit" disabled={isAnswerChecked} className="btn type2">{isAnswerChecked ? "제출 완료" : "정답 제출"}</button>
                </div>
            </form>

            {isAnswerChecked && !isCorrect && (
                <p className="result wrong">❌ 오답입니다. 정답은 "{post.answer}" 입니다.</p> // 오답일 경우 정답 표시
            )}
            {isAnswerChecked && isCorrect && (
                <p className="result correct">⭕ 축하합니다! 정답입니다.</p>
            )}
            <div className="btn_wrap">
                <Link to="/board/quiz" className="btn large more">목록으로</Link>
                <Link to={`/board/quiz/${post.id + 1}`} className="btn type2 large more">다음퀴즈 풀기</Link>
            </div>
            {/* 추천, 신고 버튼은 PostDetailPage에서 관리 */}
            {/* 댓글 섹션은 퀴즈 게시판에서는 다르게 처리할 수 있음 */}
        </div>
    );
};

export default QuizPostDisplay;