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

    const handleUpdate = async (e) => {
        e.preventDefault();
        try{
            const response = await apiClient.put("/api/user/report/{reportId}");
            console.log("수정 성공: ", response.data);
        }catch (error){
            console.error("", error);
        }
    }

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const response = await apiClient.delete("/api/user/report/{reportId}");
        }catch (error){
            console.error("", error);
        }
    }

    return(
        <>
            <form onSubmit={handleSubmit}>
                
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
                {/* title 부분을 위에 select option으로 들어가게하기 예) 욕설, 비방, 인신공격 */}
                <input type="text" name="title" placeholder="제목을 입력해주세요"/>
                {/* reporter는 현재 신고글을 작성하는 작성자의 username 보내주면됨 */}
                <input type="text" name="reporter" placeholder="신고자 ID 또는 이름"
                       value={formData.reporter} onChange={handleChange}/>
                {/* 여기는 원래  신고 대상 콘텐츠 유형을 입력하세요 인데 이거를 신고하는 게시판 종류를 적으면됨 예)자유게시판 을 유저가 작성하면   targetTable: `board_${boardId}` 이런식으로 값을 넣어주면됨 앞에 board_붙여서*/}
                <input type="text" name="reporter" placeholder="신고 대상 콘텐츠 유형을 입력하세요"
                       value={formData.target_table} onChange={handleChange}/>
                {/* 여기는 신고하는 게시글의 아이디번호를 줘야됨 게시글 들어가면 맨앞에있는 번호 */}
                <input type="text" name="target_id" placeholder="신고 대상 ID"
                       value={formData.target_id} onChange={handleChange}/>
                {/* 여기는  신고내용 적어주면됨 값이 수정될 건 없음 */}
                <label>신고 사유: </label>
                <textarea name="description" placeholder="신고 사유를 자세히 입력해주세요"
                          value={formData.description} onChange={handleChange}/>

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