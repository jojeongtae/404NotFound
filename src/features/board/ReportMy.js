import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import apiClient from "../../api/apiClient";

export default function ReportMy() {
    const {username} = useParams();
    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!username) {return}
        const fetchData = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await apiClient.get(`/user/report/${username}`);
                console.log(response.data);
                setReports(response.data);
            } catch (e) {
                console.error("목록 불러오기 실패: ",e)
                setError("신고 내역을 불러오는 중 오류가 발생하였습니다.");
            } finally {
                setLoading(false);
            }
        }
        fetchData();
    }, [username]);

    const convertStatus = (status) => {
        switch (status) {
            case 'PENDING':
                return '대기중';
            case "ACCEPTED":
                return '처리완료';
            case 'REJECTED':
                return '반려';
            default:
                return status;
        }
    }
    const convertBoard = (board) => {
        switch (board) {
            case 'board_free':
                return '자유 게시판';
            case "board_qna":
                return 'Q&A 게시판';
            case 'board_info':
                return '정보 게시판';
            case 'board_used':
                return '중고거래 게시판';
            case 'board_food':
                return '먹거리 게시판';
            case 'quiz':
                return '퀴즈 게시판';
            case 'voting':
                return 'O/X 게시판';
            case 'survey':
                return '설문조사';
            default:
                return board;
        }
    }
    const convertReason = (reason) => {
        switch (reason) {
            case 'abuse':
                return '욕설, 비방, 인신공격';
            case 'adult':
                return '음란물 및 성인물 게시';
            case 'illegal':
                return '불법 정보 및 저작권 침해';
            case 'spam':
                return '스팸 및 광고성 게시물';
            case 'repeat':
                return '도배 또는 반복 게시';
            case 'impersonation':
                return '타인 사칭';
            case 'etc':
                return '기타 부적절한 내용';
            default:
                return reason;
        }
    }

    if (loading) {
        return <p>신고 내역을 불러오는 중...</p>;
    }
    if (error) {
        return <p>{error}</p>;
    }
    return (
        <div className="report-my">
            <h3>{username}님의 신고내역</h3>
            <table className="admin-table">
                <thead>
                <tr>
                    <th>번호</th>
                    <th>신고사유</th>
                    <th>신고대상 ID</th>
                    <th>게시판</th>
                    <th>게시물 ID</th>
                    <th>상세사유</th>
                    <th>상태</th>
                    <th>신고시간</th>
                    {/*<th>수정/삭제</th>*/}
                </tr>
                </thead>
                <tbody>
                {reports.length === 0 ? (
                    <tr>
                        <td colSpan={9} className="empty">신고내역이 없습니다.</td>
                    </tr>
                ) : (reports.map((report) => (
                        <tr key={report.id}>
                            <td>{report.id}</td>
                            <td>{convertReason(report.reason)}</td>
                            <td>
                                <Link to={`/user/userinfo/${report.reported}`}>
                                {report.reported}
                                </Link></td>
                            <td>{convertBoard(report.targetTable)}</td>
                            <td>{report.targetId}</td>
                            <td>{report.description}</td>
                            <td>{convertStatus(report.status)}</td>
                            <td className="date">{new Date(report.createdAt).toLocaleString()}</td>
                            {/*<td>*/}
                            {/*    <button className="btn">수정</button>*/}
                            {/*    <button className="btn red">삭제</button>*/}
                            {/*</td>*/}
                        </tr>
                    ))
                )}
                </tbody>
            </table>
            {/*<div className="btn_wrap">*/}
            {/*    <Link to="/board/user/report" className="btn type2 large">신고하러 가기</Link>*/}
            {/*</div>*/}
        </div>
    );
}