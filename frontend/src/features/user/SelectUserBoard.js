import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import apiClient from '../../api/apiClient';
import { getFullGradeDescription } from '../common/GradeDescriptions'; // 등급 정보 가져오기

const SelectUserBoard = () => {
    const { username } = useParams(); // URL 파라미터에서 username 가져오기
    const [userPosts, setUserPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 모든 게시판 ID를 여기에 정의합니다.
    // 실제 프로젝트에 맞게 추가/수정해주세요.
    const boardIds = ['free', 'notice', 'qna', 'info', 'food', 'used', 'quiz', 'survey', 'voting'];

    useEffect(() => {
        const fetchUserPosts = async () => {
            if (!username) {
                setLoading(false);
                return;
            }

            setLoading(true);
            setError(null);
            let allPosts = [];

            try {
                // 각 게시판의 게시글을 비동기적으로 가져옵니다.
                const fetchPromises = boardIds.map(async (boardId) => {
                    try {
                        const res = await apiClient.get(`/${boardId}/list`);
                        // 현재 유저가 작성한 게시글만 필터링
                        const filteredPosts = res.data.filter(post => post.author === username);
                        // 게시판 ID를 게시글 객체에 추가하여 나중에 어떤 게시판 글인지 알 수 있도록 합니다.
                        return filteredPosts.map(post => ({ ...post, boardId: boardId }));
                    } catch (err) {
                        console.warn(`게시판 ${boardId} 게시글 불러오기 실패:`, err);
                        return []; // 실패해도 다른 게시판은 계속 진행
                    }
                });

                // 모든 Promise가 완료될 때까지 기다립니다.
                const results = await Promise.all(fetchPromises);
                // 모든 게시판의 게시글을 하나의 배열로 합칩니다.
                allPosts = results.flat();
                setUserPosts(allPosts);

            } catch (err) {
                console.error("유저 게시글 불러오기 실패:", err);
                setError("유저 게시글을 불러오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchUserPosts();
    }, [username]); // username이 변경될 때마다 다시 불러옴

    if (loading) {
        return <div style={{ padding: '20px', textAlign: 'center' }}>{username} 님의 게시글을 불러오는 중...</div>;
    }
    if (error) {
        return <div style={{ padding: '20px', textAlign: 'center', color: 'red' }}>{error}</div>;
    }

    return (
        <div className="user-detail">
            <h3>{username} 님의 작성글</h3>
            {userPosts.length === 0 ? (
                <p style={{ textAlign: 'center', color: '#666' }}>작성된 게시글이 없습니다.</p>
            ) : (
                <ul className="detail-list">
                    {userPosts.map(post => (
                        <li key={post.id} className="detail-item">
                            <Link to={`/board/${post.boardId}/${post.id}`}>
                                <div>
                                    <div className="title">{post.title}</div>
                                    <p className="info">게시판: {post.boardId} | 조회수: {post.views} | 추천: {post.recommend} | 등급: <span className="user-grade">{getFullGradeDescription(post.grade)}</span></p>
                                </div>
                                <span className="date">{new Date(post.createdAt).toLocaleDateString()}</span>
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default SelectUserBoard;