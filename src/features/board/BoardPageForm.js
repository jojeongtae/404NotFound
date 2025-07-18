import React, { useEffect, useState } from 'react'
import apiClient from '../../api/apiClient';
import { Link } from 'react-router-dom'; // Link 임포트

const BoardPageForm = ({ boardId }) => { // boardId prop 다시 받기
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBoardPosts = async () => { 
            if (!boardId) {
                setLoading(false);
                return; // boardId가 없으면 요청하지 않음
            }
            try {
                setLoading(true);
                setError(null);
                const res = await apiClient.get(`/board/${boardId}/list`); 
                setPosts(res.data);
                console.log(`게시판 ${boardId}의 게시글:`, res.data);
            } catch (err) {
                console.error(`게시판 ${boardId} 게시글 불러오기 실패:`, err);
                setError("게시글을 불러오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchBoardPosts();
    }, [boardId]); // boardId가 변경될 때마다 다시 불러옴

    if (loading) {
        return <div>게시글을 불러오는 중...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }

    return (
        <div>
            <h3>{boardId} 게시판</h3>
            {posts.length > 0 ? (
                <ul>
                    {posts.map(post => (
                        <li key={post.id}>
                            <Link to={`/board/${boardId}/${post.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                               {post.id} | 제목 : {post.title} | 작성자 :{post.author}
                            </Link>
                        </li> 
                    ))}
                </ul>
            ) : (
                <p>게시글이 없습니다.</p>
            )}
        </div>
    )
}

export default BoardPageForm