import React, { useEffect, useState } from 'react';
import apiClient from '../api/apiClient'; // apiClient 임포트
import { getFullGradeDescription } from '../features/common/GradeDescriptions';

const HomePage = () => {
    const [userRanking, setUserRanking] = useState([]);
    const [recommendRanking, setRecommendRanking] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchRankings = async () => {
            try {
                const [userRes, recommendRes] = await Promise.all([
                    apiClient.get('/users-grade/top5'),
                    apiClient.get('/ranking/recommend/all')
                ]);
                setUserRanking(userRes.data || []);
                setRecommendRanking(recommendRes.data || []);
            } catch (error) {
                console.error("랭킹 정보를 불러오는 데 실패했습니다:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchRankings();
    }, []);

    return (
        <div style={{ textAlign: 'center', padding: '50px' }}>
            <h1>환영합니다!</h1>
            <p>이곳은 404NotFound의 메인 페이지입니다.</p>
            
            {loading ? (
                <p>랭킹 정보를 불러오는 중...</p>
            ) : (
                <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '40px', textAlign: 'left' }}>
                    <div style={{ flex: 1, marginRight: '20px' }}>
                        <h3>🏆 유저 등급 랭킹</h3>
                        <ol>
                            {userRanking.map((user, index) => (
                                <li key={index}>{`${index + 1}. ${user.nickname} (${getFullGradeDescription(user.grade)})`}</li>
                            ))}
                        </ol>
                    </div>
                    <div style={{ flex: 1, marginLeft: '20px' }}>
                        <h3>👍 게시글 추천 랭킹</h3>
                        <ol>
                            {recommendRanking.map((post, index) => (
                                <li key={index}>{`${index + 1}. ${post.title} (${post.recommend})`}</li>
                            ))}
                        </ol>
                    </div>
                </div>
            )}
        </div>
    );
};

export default HomePage;