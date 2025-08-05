import { useSelector } from "react-redux";
import apiClient from "../../api/apiClient";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

//신고게시판
const ReportBoardForm = () => {
    const user = useSelector(state => state.user);
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        reason: '',
        description: '',
        reporter: user.username,
        reported: '',
        targetTable: '',
        targetId: '',
    })

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    }

    // 신고
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {

            const res = await apiClient.get(`/user/nickname/${formData.reported}`);
            console.log(res.data);
            if (res.data) {
                const sendData = {
                    ...formData,
                    targetId: parseInt(formData.targetId),
                    reported:res.data.username,
                };
                const response = await apiClient.post("/user/report", sendData);
                console.log("신고 성공: ", response.data);
                alert("신고가 접수 되었습니다.");
                navigate(`/board/user/report/${user.username}`);
            } else {
                alert("작성하신 내용중에 문제가 생겼습니다.");
                return;
            }
        } catch (error) {
            console.error("신고 실패: ", error);
        }
    }

    return (
        <div className="board-report">
            <form onSubmit={handleSubmit}>
                <h3>신고하기</h3>
                <ul className="report-list">
                    <li>
                        <label>
                            <span>신고사유</span>
                            <select name="reason" onChange={handleChange}>
                                <option value="">신고 사유 선택</option>
                                <option value="abuse">욕설, 비방, 인신공격</option>
                                <option value="adult">음란물 및 성인물 게시</option>
                                <option value="illegal">불법 정보 및 저작권 침해</option>
                                <option value="spam">스팸 및 광고성 게시물</option>
                                <option value="repeat">도배 또는 반복 게시</option>
                                <option value="impersonation">타인 사칭</option>
                                <option value="etc">기타 부적절한 내용</option>
                            </select>
                        </label>
                    </li>
                    <li>
                        <label>
                            <span>신고대상</span>
                            <input type="text" name="reported" placeholder="신고자 ID 또는 이름" value={formData.reported} onChange={handleChange} />
                        </label>
                    </li>
                    <li>
                        <label>
                            <span>신고글이 있는 게시판</span>
                            <select name="targetTable" value={formData.targetTable} onChange={handleChange}>
                                <option value="">게시판 선택</option>
                                <option value="board_free">자유 게시판</option>
                                <option value="board_qna">Q&A 게시판</option>
                                <option value="board_info">정보 게시판</option>
                                <option value="board_used">중고거래 게시판</option>
                                <option value="board_food">먹거리 게시판</option>
                                <option value="quiz">퀴즈 게시판</option>
                                <option value="voting">O/X 게시판</option>
                                <option value="survey">설문조사</option>
                            </select>
                        </label>
                    </li>
                    <li>
                        <label>
                            <span>신고 게시물 ID</span>
                            <input type="number" name="targetId" value={formData.targetId} onChange={handleChange} />
                        </label>
                    </li>
                    <li>
                        <label>
                            <span>상세 사유</span>
                            <textarea name="description" placeholder="신고 사유를 자세히 입력해주세요." value={formData.description} onChange={handleChange} rows="6" />
                        </label>
                    </li>
                </ul>
                <div className="btn_wrap">
                    <button type="submit" className="btn type2 large">신고하기</button>
                    <Link to={`/board/user/report/${user.username}`} className="btn large">나의 신고 보기</Link>
                </div>
            </form>
        </div>
    );
}

export default ReportBoardForm;