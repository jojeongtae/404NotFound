import  { useState } from 'react'
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom'; // useNavigate 임포트

const NewBoardForm = () => {
    const [title, setTitle] = useState("");
    const [textBody, setTextBody] = useState("");
    const [quizQuestion, setQuizQuestion] = useState(""); // 퀴즈 문제 상태 추가
    const [quizAnswer, setQuizAnswer] = useState("");     // 퀴즈 정답 상태 추가
    const user = useSelector(state => state.user);
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [boardId, setBoardId] = useState("");
    const [quizId,setQuizId] = useState("");
    const handleNewBoard = async () => {
        let boardData = {
            author: user.username,
            title: title,
            body: textBody,
        }

        // 퀴즈 게시판일 경우 문제와 정답 추가
        if (boardId === 'quiz') {
            boardData = {
                ...boardData,
                author:user.username,
                question: quizQuestion,
                answer: quizAnswer,
                title,
                type:quizId
            };
        }

        console.log(boardData);
        try {
            const res = await apiClient.post(`/${boardId}/new`, boardData);
            console.log(user.role);
            console.log(res.data);
            alert('글이 성공적으로 작성되었습니다!'); // 성공 알림
            
            navigate(`/board/${boardId}`); // 예)'free' 게시판으로 이동
        } catch (error) {
            console.log(error);
            alert('글 작성에 실패했습니다.'); // 실패 알림
        }
    }
    return (
        <>
            <form onSubmit={(e) => {
                e.preventDefault();
                handleNewBoard();
            }}>
                {/* 게시글 작성 */}
                <select id="boardSelect" onChange={(e) => setBoardId(e.target.value)}>
                    <option value="">게시판을 선택</option>
                    {user.role === "ROLE_ADMIN" && (
                        <option value="notice">공지 사항</option>
                    )}
                    <option value="free">자유 게시판</option>
                    <option value="food">먹거리 게시판</option>
                    <option value="info">정보 게시판</option>
                    <option value="qna">Q&A 게시판</option>
                    <option value="used">중고 게시판</option>
                    <option value="quiz">퀴즈 게시판</option>
                </select>
                <br />
                <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                {boardId !== "quiz" && (
                <textarea placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea>
                )
                }
                {/* 퀴즈 게시판일 경우 추가 필드 */} 
                {boardId === 'quiz' && (
                    <>
                        <br />
                        <select id='quizSelect' onChange={(e)=> setQuizId(e.target.value)}>
                            <option value="MULTI">객관식</option>
                            <option value="SUBJECTIVE">주관식</option>
                            <option value="OX">OX</option>
                        </select>
                        <input type='text' placeholder='퀴즈 문제' value={quizQuestion} onChange={(e) => setQuizQuestion(e.target.value)}></input> <br />
                        <input type='text' placeholder='퀴즈 정답' value={quizAnswer} onChange={(e) => setQuizAnswer(e.target.value)}></input>
                    </>
                )}

                <input type="submit" value="전송" />
            </form>
        </>
    )
}

export default NewBoardForm