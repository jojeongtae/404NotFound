import React, { useState } from 'react'
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom'; // useNavigate 임포트

const NewBoardForm = () => {
    const [title, setTitle] = useState("");
    const [textBody, setTextBody] = useState("");
    const user = useSelector(state => state.user);
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [boardId, setBoardId] = useState("");
    const handleNewBoard = async () => {
        const boardData = {
            author: user.username,
            title: title,
            body: textBody,
        }
        console.log(boardData);
        try {
            const res = await apiClient.post(`/${boardId}/new`, boardData);
            console.log(user.role);
            console.log(res.data);
            alert('글이 성공적으로 작성되었습니다!'); // 성공 알림
            navigate(`/board/${boardId}`); // 'free' 게시판으로 이동
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
                </select>
                <br />
                <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                <textarea placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea>
                <input type="submit" value="전송" />
            </form>
        </>
    )
}

export default NewBoardForm