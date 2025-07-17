import React, { useState } from 'react'
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom'; // useNavigate 임포트

const NewBoardForm = () => {
    const [title, setTitle] = useState("");
    const [textBody,setTextBody] = useState("");
    const username = useSelector(state => state.user.username); 
    const navigate = useNavigate(); // useNavigate 훅 사용

    const handleNewBoard = async() =>{
        const boardData  = {
            author: username, 
            title:title,
            body:textBody,
        }
        console.log(boardData);
        try {
            const res = await apiClient.post("/board/new",boardData);
            console.log(res.data);
            alert('글이 성공적으로 작성되었습니다!'); // 성공 알림
            navigate('/board/free'); // 'free' 게시판으로 이동
        } catch (error) {
            console.log(error);
            alert('글 작성에 실패했습니다.'); // 실패 알림
        }
    }
    return (
        <>
        <form onSubmit={(e)=>{
            e.preventDefault();
            handleNewBoard();
        }}>
            <select>
                <option>게시판을 선택</option>
                <option>자유 게시판</option>
                <option>먹거리 게시판</option>
                <option>정보 게시판</option>
                <option>Q&A 게시판</option>
                <option>중고 게시판</option>
            </select><br />
            <input type='text' placeholder='제목' value={title} onChange={(e)=>setTitle(e.target.value)}></input> <br />
            <textarea placeholder='내용' value={textBody} onChange={(e)=>setTextBody(e.target.value)}></textarea>
            <input type="submit" value="전송" />
        </form>
        </>
    )
}

export default NewBoardForm