import apiClient from "../../api/apiClient";

//퀴즈 풀이
const QuizAnswerForm = () => {
    const handleSubmit = async (e) => {
        e.preventDefault();
        try{
            const response = await apiClient.post("/api/board", {
                answer: e.target.answer.value,
            })
        }catch (error){

        }
    }
    return(
        <>
            <form onSubmit={handleSubmit}>
                <textarea name="text" placeholder="내용을 입력해주세요"></textarea>
                <input type="text" name="answer" placeholder="답을 입력해주세요"/>
                <button type="submit">다음</button>
            </form>
        </>
    );
}

export default QuizAnswerForm;