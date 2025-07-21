import {useDispatch} from "react-redux";
import apiClient from "../../api/apiClient";

//OX게시판
const VotingBoardForm = () => {
    const dispatch = useDispatch();
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await apiClient.post("/api/board",{
                title: e.target.title.value,
                question: e.target.question.value,
                answer: e.target.answer.value,
                author: e.target.author.value,
                created_at: e.target.date.value,
                category: e.target.category.value,
            })
        }catch (error){

        }
    }
    return(
        <div>
            <form onSubmit={handleSubmit}>
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
        </div>
    );
}

export default VotingBoardForm;