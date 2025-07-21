//신고게시판
import {useDispatch, useSelector} from "react-redux";
import apiClient from "../../api/apiClient";

const ReportBoardForm = () => {
    const dispatch = useDispatch();
    const reports = useSelector(state => state.user);
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
           const response = await apiClient.post("/api/board",{
               reason: e.target.reason.value,
               description: e.target.description.value,
               reporter: e,
               target_table: e,
               target_id: e,
           });

        }catch (error){

        }
    }

    const handleChange = () => {

    }

    return(
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                <input type="text" name="author" placeholder="임시 작성란"/>
                <input type="date" name="date"/>
                <label>신고 사유: </label>
                <select name="reason" onChange={handleChange}>
                    <option value="">선택하세요</option>
                    <option value="abuse">욕설, 비방, 인신공격</option>
                    <option value="adult">음란물 및 성인물 게시</option>
                    <option value="illegal">불법 정보 및 저작권 침해</option>
                    <option value="spam">스팸 및 광고성 게시물</option>
                    <option value="repeat">도배 또는 반복 게시</option>
                    <option value="impersonation">타인 사칭</option>
                    <option value="etc">기타 부적절한 내용</option>
                </select>
                <textarea name="description" placeholder="신고 사유를 자세히 입력해주세요" />
                <button type="submit">신고하기</button>
            </form>
            <div>
                <h3>신고 목록</h3>
                {reports.map((report) => (
                    <div>
                        <p>신고 대상: {reports.target_table} #{report.target_id}</p>
                        <p>사유: {report.reason}</p>
                        <p>신고자: {report.reporter}</p>
                    </div>
                ))}
            </div>
        </>
    );
}

export default ReportBoardForm;