import React, { useEffect, useState } from 'react';
import apiClient from '../../api/apiClient';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';

const AdminReportForm = () => {
    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const user = useSelector(state => state.user);
    useEffect(() => {
        const fetchReports = async () => {
            try {
                setLoading(true);
                setError(null);
                const res = await apiClient.get("/admin/report-list");
                console.log(res.data);
                setReports(res.data.filter(user => user.status === "PENDING"));
            } catch (err) {
                console.error("신고 목록 불러오기 실패:", err);
                setError("신고 목록을 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchReports();
    }, []);

    if (loading) {
        return <div style={{ textAlign: 'center', padding: '50px' }}>신고 목록을 불러오는 중...</div>;
    }

    if (error) {
        return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
    }

    if (reports.length === 0) {
        return <div style={{ textAlign: 'center', padding: '50px' }}>신고된 게시글이 없습니다.</div>;
    }
    const handleCancle = async (reportId) => {

        try {
            const res = await apiClient.delete(`/user/report/${reportId}?username=${user.username}`);
            console.log(res.data);
            alert("취소 완료");
            setReports(prevReports => prevReports.filter(report => report.id !== reportId));
        } catch (err) {
            console.log(err);
        }
    }
    const handleAddReportUser = async (id,reporter,reported) =>{
        const body = {
            reportId:id,
            status:"ACCEPTED"
        }
        try {
            const res = await apiClient.patch(`/admin/report`,body);
            console.log(res.data);
            if(res.status === 200){
                const messageBody = {
                    author:user.username,
                    receiver:reporter,
                    title:`신고접수안내`,
                    message:`신고하신 내용이 접수되었습니다.`
                }
                const messageRes = await apiClient.post(`/message/send`,messageBody);
                console.log(messageRes);
            }
            alert("처리 완료")
            setReports(prevReports => prevReports.filter(report => report.id !== id));
        } catch (error) {
            console.log(error);
        }
    }
    return (
        <div className="admin-report">
            <h3>신고 관리</h3>
            <ul className="report-list">
                {reports.map(report => (
                    <li key={report.id} className="report-item">
                        <h4>신고 ID: {report.id}</h4>
                        <ul className="detail-list">
                            <li className="detail-item"><em>사유:</em> {report.reason || '사유 없음'}</li>
                            <li className="detail-item"><em>상세:</em> {report.description || '-'}</li>
                            {/* authorNickname 변경예정 */}
                            <li className="detail-item"><em>신고자:</em> {report.reporter || '알 수 없음'}</li>
                            {/* authorNickname 변경예정 */}
                            <li className="detail-item"><em>피신고자:</em> {report.reported || '알 수 없음'}</li>
                            {console.log(report)}
                            <li className="detail-item">
                                <em>대상:</em>
                                <Link to={`/board/${report.targetTable.replace('board_', '')}/${report.targetId}`}
                                      style={{marginLeft: '5px', color: 'blue', textDecoration: 'underline'}}>
                                    {report.targetTable.replace('board_', '')} (ID: {report.targetId})
                                </Link>
                            </li>
                            <li className="detail-item"><em>상태:</em> {report.status || '알 수 없음'}</li>
                            <li className="detail-item"><em>신고 시간:</em> {new Date(report.createdAt).toLocaleString()}</li>
                            {report.updatedAt && <li className="detail-item"><em>처리 시간:</em> {new Date(report.updatedAt).toLocaleString()}</li>}
                        </ul>
                        <div className="btn_wrap">
                            <button onClick={()=> handleAddReportUser(report.id,report.reporter,report.reported)}>신고 인용</button>
                            <button onClick={() => handleCancle(report.id)}>신고 기각</button>
                        </div>
                        {/* 여기에 신고 처리 버튼 (예: 게시글 숨기기, 사용자 경고 등) 추가 가능 */}
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default AdminReportForm;
