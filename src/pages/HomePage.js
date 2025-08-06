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
        <div className="main-intro">
            <div className="intro-header">
                <p className="main-title">
                    <strong>404 Not Found</strong>
                    {/*<span> – 존재하지 않던 연결을 찾다</span>*/}
                </p>
                <p className="sub-title">지금, 당신의 여정을 함께할 사람들을 만나보세요.</p>
            </div>

            <div className="intro-ranking">
                {loading ? (
                    <p>랭킹 정보를 불러오는 중...</p>
                ) : (
                    <div className="ranking-wrap">
                        <div className="ranking-box user">
                            <h4>🏆 유저 등급 랭킹</h4>
                            <ol className="ranking-list">
                                {userRanking.map((user, index) => (
                                    <li key={index} className="ranking-item">
                                        {/*{`${index + 1}. ${user.nickname} (${getFullGradeDescription(user.grade)})`}*/}
                                        <span className="num">{index + 1}.</span>
                                        <span className="title">{user.nickname}</span>
                                        <span className="user-grade">{getFullGradeDescription(user.grade)}</span>
                                    </li>
                                ))}
                            </ol>
                        </div>
                        <div className="ranking-box recommend">
                            <h4>👍 게시글 추천 랭킹</h4>
                            <ol className="ranking-list">
                                {recommendRanking.map((post, index) => (
                                    <li key={index} className="ranking-item">
                                        {/*{`${index + 1}. ${post.title} (${post.recommend})`}*/}
                                        <span className="num">{index + 1}.</span>
                                        <span className="title">{post.title}</span>
                                        <span className="recommend">({post.recommend}추천)</span>
                                    </li>
                                ))}
                            </ol>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default HomePage;