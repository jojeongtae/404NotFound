
//설문조사 게시판
const SurveyBoardForm = () => {
    return(
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                <textarea name="text" placeholder="내용을 입력해주세요"></textarea>
                <input type="text" name="author" placeholder="임시 작성란"/>
                <label>
                    <input type="radio" name="anonymous" value="false" checked />
                    실명으로 작성
                </label>
                <label>
                    <input type="radio" name="anonymous" value="true" />
                    익명으로 작성
                </label>
                <input type="checkbox" />

                <button type="submit">설문 등록</button>
            </form>
        </>
    );
}

export default SurveyBoardForm;