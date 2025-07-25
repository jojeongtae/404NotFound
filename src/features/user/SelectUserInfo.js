import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import '../../style/SelectUserInfo.css'; // CSS 파일 임포트
import { getFullGradeDescription } from '../common/GradeDescriptions';

const SelectUserInfo = () => {
    const { username } = useParams();
    const [selectUser, setSelectedUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const currentUser = useSelector(state => state.user);

    useEffect(() => {
        const fetchUserInfo = async () => {
            if (!username) {
                setLoading(false);
                return;
            }
            try {
                setLoading(true);
                setError(null);
                const res = await apiClient.get(`/user/user-info?username=${username}`);
                setSelectedUser(res.data);
            } catch (err) {
                console.error("유저 정보 불러오기 실패:", err);
                setError("유저 정보를 불러오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        fetchUserInfo();
    }, [username]);

    if (loading) {
        return <div className="user-info-loading">유저 정보를 불러오는 중...</div>;
    }

    if (error) {
        return <div className="user-info-error">{error}</div>;
    }

    if (!selectUser) {
        return <div className="user-info-not-found">유저 정보를 찾을 수 없습니다.</div>;
    }

    return (
        <div className="user-info-container">
            <h2 className="user-info-title">{getFullGradeDescription(selectUser.grade)} {selectUser.nickname} 님의 정보</h2>
            <div>
                <p className="user-info-item">
                    <strong>유저 아이디:</strong> {selectUser.username}
                </p>
                <p className="user-info-item">
                    <strong>유저 닉네임:</strong> {selectUser.nickname}
                </p>
                <p className="user-info-item">
                    <strong>유저 등급:</strong>{selectUser.grade ? selectUser.grade : '정보 없음'}
                </p>
                <p className="user-info-item">
                    <strong>유저 전화번호:</strong> {selectUser.phone ? selectUser.phone.slice(0, 6) : '정보 없음'}
                </p>
                <p className="user-info-item">
                    <strong>유저 주소지:</strong> {selectUser.address ? selectUser.address.split(" ")[0] : '정보 없음'}
                </p>
            </div>
        </div>
    )
}

export default SelectUserInfo;