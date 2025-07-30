import {useSelector} from "react-redux";
import apiClient from "../../api/apiClient";
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";

//신고게시판
const ReportBoardForm = () => {
    const user = useSelector((state) => state.user);
    const { boardId } = useParams();

    const [reportedNickname, setReportedNickname] = useState("");
    const [reportedUserId, setReportedUserId] = useState(null); // 사용자 id 저장용
    const [formData, setFormData] = useState({
        reason: "",
        description: "",
        reporter: user?.username || "",
        reported: "", // userId
        targetTable: `board_${boardId}`,
        targetId: "",
    });

    // 닉네임 변경 시 사용자 id 조회
    useEffect(() => {
        if (!reportedNickname) {
            setReportedUserId(null);
            setFormData((prev) => ({ ...prev, reported: "" }));
            return;
        }

        // API 호출 (닉네임으로 id 조회)
        const fetchUserId = async () => {
            try {
                const res = await apiClient.get(`/api/user/nickname/${reportedNickname}`);
                // 백엔드가 반환하는 데이터 구조에 따라 맞춰서 수정
                const userId = res.data.id;
                setReportedUserId(userId);
                setFormData((prev) => ({ ...prev, reported: userId }));
            } catch (err) {
                console.error("사용자 조회 실패", err);
                setReportedUserId(null);
                setFormData((prev) => ({ ...prev, reported: "" }));
            }
        };

        fetchUserId();
    }, [reportedNickname]);

    const handleReportedChange = (e) => {
        setReportedNickname(e.target.value);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!formData.reported) {
            alert("신고 대상 닉네임을 올바르게 입력해주세요.");
            return;
        }
        if (!formData.reason) {
            alert("신고 사유를 입력하세요.");
            return;
        }
        try {
            const response = await apiClient.post("/api/user/report", formData);
            alert("신고가 접수 되었습니다.");
            console.log("신고 성공: ", response.data);
        } catch (error) {
            console.error("신고 실패: ", error);
            alert("신고 처리 중 오류가 발생하였습니다.");
        }
    };

    return(
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" name="nickname" placeholder="신고 대상"
                       value={reportedNickname} onChange={handleReportedChange}/>
                {reportedNickname && !reportedUserId && (
                    <div style={{ color: "red" }}>존재하지 않는 닉네임입니다.</div>
                )}
                <br/>
                {/* reporter는 현재 신고글을 작성하는 작성자의 username 보내주면됨 */}
                <input type="text" name="reporter" placeholder="신고자"
                       value={formData.reporter} readOnly/>
                <br/>
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
                {/* 여기는  신고내용 적어주면됨 값이 수정될 건 없음 */}
                <br/>
                <label>신고 사유: </label>
                <textarea name="description" placeholder="신고 사유를 자세히 입력해주세요"
                          value={formData.description} onChange={handleChange}/>
                <br/>
                {/* title 부분을 위에 select option으로 들어가게하기 예) 욕설, 비방, 인신공격 */}
                {/* 여기는 원래  신고 대상 콘텐츠 유형을 입력하세요 인데 이거를 신고하는 게시판 종류를 적으면됨 예)자유게시판 을 유저가 작성하면   targetTable: board_${boardId} 이런식으로 값을 넣어주면됨 앞에 board_붙여서*/}
                <input type="text" name="targetTable" placeholder="신고 대상 게시판 종류"
                       value={formData.targetTable} onChange={handleChange}/>
                {/* 여기는 신고하는 게시글의 아이디번호를 줘야됨 게시글 들어가면 맨앞에있는 번호 */}
                <br/>
                <input type="text" name="targetId" placeholder="신고 대상 ID"
                       value={formData.targetId} onChange={handleChange}/>
                <br/>
                {/* 추가적으로 신고당하는 닉네임을 가져와서 아이디로 변환하는 백엔드 요청을 써서  그걸 reported로 값을 넣어줘야됨 그러니 input 태그를 하나 더 만들어서 신고자 닉네임을 만들어야됨 */}

                {/* 하다가 잘 모르면 Page/PostDetail 가서 handleReport 함수 보고 참고 */}

                <button type="submit">신고하기</button>

            </form>
            {/* <div>
                <h3>신고 목록</h3>
                {reports.map((report) => (
                    <div>
                        <p>신고 대상: {reports.target_table} #{report.target_id}</p>
                        <p>사유: {report.reason}</p>
                        <p>신고자: {report.reporter}</p>
                    </div>
                ))}
            </div> */}
        </>
    );
}

export default ReportBoardForm;