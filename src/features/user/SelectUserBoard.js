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
    const boardIds = [
        'free', 'notice', 'qna', 'info', 'food', 'used', 'quiz', 'survey', 'voting'
    ];

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
        <div style={{ maxWidth: '800px', margin: '50px auto', padding: '20px', border: '1px solid #eee', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '20px', color: '#333' }}>{username} 님의 작성글</h2>
            {userPosts.length === 0 ? (
                <p style={{ textAlign: 'center', color: '#666' }}>작성된 게시글이 없습니다.</p>
            ) : (
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {userPosts.map(post => (
                        <li key={post.id} style={{
                            borderBottom: '1px solid #f0f0f0',
                            padding: '15px 0',
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center'
                        }}>
                            <div style={{ flexGrow: 1 }}>
                                <Link to={`/board/${post.boardId}/${post.id}`} style={{ textDecoration: 'none', color: '#007bff', fontSize: '1.1em', fontWeight: 'bold' }}>
                                    {post.title}
                                </Link>
                                <p style={{ fontSize: '0.9em', color: '#888', margin: '5px 0 0 0' }}>
                                    게시판: {post.boardId} | 조회수: {post.views} | 추천: {post.recommend} | 등급: {getFullGradeDescription(post.grade)}
                                </p>
                            </div>
                            <span style={{ fontSize: '0.85em', color: '#999' }}>
                                {new Date(post.createdAt).toLocaleDateString()}
                            </span>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default SelectUserBoard;