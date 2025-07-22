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
                setReports(res.data);
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
        } catch (err) {
            console.log(err);
        }
    }
    const handleAddReportUser = async (id) =>{
        const body = {
            reportId:id,
            status:"ACCEPTED"
        }
        try {
            const res = await apiClient.patch(`/admin/report`,body);
            console.log(res.data);
            alert("처리 완료")
        } catch (error) {
            console.log(error);
        }
    }
    return (
        <>
            <h2>신고 관리</h2>
            <ul className="report-list">
                {reports.map(report => (
                    <li key={report.id} className="report-list-item" style={{ border: '1px solid #ccc', padding: '15px', marginBottom: '10px', borderRadius: '5px' }}>
                        <h3>신고 ID: {report.id}</h3>
                        <div style={{ marginBottom: '5px' }}><strong>사유:</strong> {report.reason || '사유 없음'}</div>
                        <div style={{ marginBottom: '5px' }}><strong>상세:</strong> {report.description || '-'}</div>
                        {/* authorNickname 변경예정 */}
                        <div style={{ marginBottom: '5px' }}><strong>신고자:</strong> {report.reporter || '알 수 없음'}</div>
                        {/* authorNickname 변경예정 */}
                        <div style={{ marginBottom: '5px' }}><strong>피신고자:</strong> {report.reported || '알 수 없음'}</div>
                        <div style={{ marginBottom: '5px' }}>
                            <strong>대상:</strong>
                            <Link to={`/board/${report.targetTable.replace('board_', '')}/${report.targetId}`} style={{ marginLeft: '5px', color: 'blue', textDecoration: 'underline' }}>
                                {report.targetTable.replace('board_', '')} (ID: {report.targetId})
                            </Link>
                        </div>
                        <div style={{ marginBottom: '5px' }}><strong>상태:</strong> {report.status || '알 수 없음'}</div>
                        <div style={{ marginBottom: '5px' }}><strong>신고 시간:</strong> {new Date(report.createdAt).toLocaleString()}</div>
                        {report.updatedAt && <div style={{ marginBottom: '5px' }}><strong>처리 시간:</strong> {new Date(report.updatedAt).toLocaleString()}</div>}
                        <button onClick={() => handleCancle(report.id)}>탈락 땅땅땅</button> &nbsp;
                        <button onClick={()=> handleAddReportUser(report.id)}>처리완료 땅땅땅</button>
                        {/* 여기에 신고 처리 버튼 (예: 게시글 숨기기, 사용자 경고 등) 추가 가능 */}
                    </li>
                ))}
            </ul>
        </>
    )
}

export default AdminReportForm;
