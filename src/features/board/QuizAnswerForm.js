import apiClient from "../../api/apiClient";
import {useState} from "react";

//퀴즈 풀이
const QuizAnswerForm = () => {
    const [formData, setFormData] = useState();
    const handleSubmit = async (e) => {
        e.preventDefault();
        const quizAnswer = {
            quiz_id: e.target.value,
            username:e.target.value,
            userAnswer:e.target.userAnswer.value,
            result:e.target.value,
            solvedAt:e.target.value,
        };

        try{
            const response = await apiClient.post("/api/quiz-answers/new", quizAnswer);
            console.log("성공", response.data);
        }catch (error){
            console.error("실패", error);
        }
    }

    return(
        <>
            <form onSubmit={handleSubmit}>
                <textarea name="text" placeholder="내용을 입력해주세요"></textarea>
                <input type="text" name="userAnswer" placeholder="답을 입력해주세요"/>
                <button type="submit">다음</button>
            </form>
        </>
    );
}

export default QuizAnswerForm;