import {useDispatch, useSelector} from "react-redux";
import {useEffect, useState} from "react";
import apiClient from "../../api/apiClient";

//퀴즈 목록 및 생성 게시판
const QuizBoardForm = () => {
    const dispatch = useDispatch;
    const loggedInUser = useSelector(state => state.user);
    const [selectedQuiz, setSelectedQuiz] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const res = await apiClient.get("/api/board");//서버에서 데이터 받아오기
            const data = await res.json();
            setSelectedQuiz(data)
        };
        fetchData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await apiClient.post("/api/board", {
                title: e.target.title.value,
                question: e.target.question.value,
                answer: e.target.answer.value,
                author: e.target.author.value,
                created_at: e.target.date.value,
                level: e.target.level.value,
                category: e.target.category.value,
            });//서버에 데이터 보내기
        }catch (error){

        }
    }
    return(
        <>
            <form onSubmit={handleSubmit}>
                <label>레벨 선택: </label>
                <input type="range" name="level" min="1" max="3" step="1" onChange={}/>
                <label>카테고리 선택: </label>
                <select name="category">
                    <option value="">선택하세요</option>
                    <option value="kpop">k-pop</option>
                    <option value="food">음식</option>
                    <option value="nonsense">넌센스</option>
                    <option value="general">상식</option>
                    <option value="history">역사</option>
                    <option value="etc">기타</option>
                </select>
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                <input type="text" name="question" placeholder="질문을 입력해주세요"/>
                <input type="text" name="answer" placeholder="답을 입력해주세요"/>
                <input type="text" name="author" placeholder="임시 작성란"/>
                <input type="date" name="date" placeholder="임시 작성란"/>
                <button type="submit">퀴즈 생성</button>
            </form>
        </>
    );
}

export default QuizBoardForm;