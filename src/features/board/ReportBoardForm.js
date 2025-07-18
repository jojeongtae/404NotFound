
//신고게시판
const ReportBoardForm = () => {
    return(
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                <input type="text" name="author" placeholder="임시 작성란"/>
                <input type="date" name="date" placeholder="임시 작성란"/>
                <label>신고 사유: </label>
                <select>
                    <option>욕설, 비방, 인신공격</option>
                    <option>음란물 및 성인물 게시</option>
                    <option>불법 정보 및 저작권 침해</option>
                    <option>스팸 및 광고성 게시물</option>
                    <option>도배 또는 반복 게시</option>
                    <option>타인 사칭</option>
                    <option>기타 부적절한 내용</option>
                </select>
                <button type="submit">신고하기</button>
            </form>
        </>
    );
}

export default ReportBoardForm;