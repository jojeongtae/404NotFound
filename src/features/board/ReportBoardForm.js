import {useSelector} from "react-redux";
import apiClient from "../../api/apiClient";
import {useState} from "react";

//신고게시판
const ReportBoardForm = () => {
    const reports = useSelector(state => state.user);

    const [formData, setFormData] = useState({
        reason: '',
        description: '',
        reporter: '',
        target_table: '',
        target_id: '',
    })

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData(prev => ({ ...prev, [name]: value}));
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await apiClient.post("/api/user/report", formData);
            console.log("신고 성공: ", response.data);
        }catch (error){
            console.error("신고 실패: ", error);
        }
    }

    return(
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                <input type="text" name="reporter" placeholder="신고자 ID 또는 이름"
                       value={formData.reporter} onChange={handleChange}/>

                <input type="text" name="reporter" placeholder="신고 대상 콘텐츠 유형을 입력하세요"
                       value={formData.target_table} onChange={handleChange}/>

                <input type="text" name="target_id" placeholder="신고 대상 ID"
                       value={formData.target_id} onChange={handleChange}/>

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

                <textarea name="description" placeholder="신고 사유를 자세히 입력해주세요"
                          value={formData.description} onChange={handleChange}/>

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