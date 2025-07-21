
//설문 정답 풀이
const SurveyAnswerForm = () => {
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {

        }catch (error){

        }
    }
    return(
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                <textarea name="text" placeholder="내용을 입력해주세요"></textarea>
                <input type="text" name="author" placeholder="임시 작성란"/>
                <input type="checkbox" />
                <button type="submit">응답 제출</button>
            </form>
        </>
    );
}

export default SurveyAnswerForm;