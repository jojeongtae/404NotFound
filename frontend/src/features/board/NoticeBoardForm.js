
//공지사항 게시판
const NoticeBoardForm = () => {
    const handleSubmit = async (e) => {
        e.preventDefault();

    }
    return(
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                <input type="text" name="author" placeholder="임시 작성란"/>
                <input type="date" name="date" placeholder="임시 작성란"/>
                <textarea name="text" placeholder="내용을 입력해주세요"></textarea>
                <button type="submit">등록</button>
            </form>
        </>
    );
}
export default NoticeBoardForm;