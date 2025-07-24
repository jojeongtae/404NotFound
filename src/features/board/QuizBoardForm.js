import {useSelector} from "react-redux";
import {useState} from "react";
import apiClient from "../../api/apiClient";

//퀴즈 목록 및 생성 게시판
const QuizBoardForm = () => {
    const loggedInUser = useSelector(state => state.user);
    const [quizList, setQuizList] = useState([]);
    const [level, setLevel] = useState(1);

    const fetchQuizList = async () => {
        try {
            const res = await apiClient.get("/api/quiz/list");
            setQuizList(res.data);
        } catch (err) {
            console.error("퀴즈 불러오기 실패", err);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newQuiz = {
            title: e.target.title.value,
            question: e.target.question.value,
            answer: e.target.answer.value,
            author: e.target.author.value,
            created_at: e.target.created_at.value,
            level: e.target.level.value,
            category: e.target.category.value,
        };

        try {
            const response = await apiClient.post("/api/quiz/new", newQuiz);//서버에 데이터 보내기
            console.log("등록 성공: ", response.data);
            fetchQuizList();
            e.target.reset();
            setLevel(1);
        }catch (error){
            console.error("등록 실패: ", error);
        }
    }
    return(
        <>
            <form onSubmit={handleSubmit}>
                <label>레벨 선택: </label>
                <input type="range" name="level" min="1" max="3" step="1"
                       value={level} onChange={(e) => setLevel(e.target.value)}/>

                <span>{level}</span>

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
                <input type="date" name="created_at" placeholder="임시 작성란"/>
                <button type="submit">퀴즈 생성</button>
            </form>


        </>
    );
}

export default QuizBoardForm;