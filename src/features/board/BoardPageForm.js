import React, { useEffect, useState } from 'react'
import apiClient from '../../api/apiClient';
import { Link } from 'react-router-dom';

const BoardPageForm = () => { // boardId prop은 일단 제거하거나 사용하지 않음
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchAllPosts = async () => {
            try {
                setLoading(true);
                setError(null);
                // TODO: 모든 게시글을 불러오는 실제 백엔드 API 경로로 변경하세요.
                const res = await apiClient.get("/board/list"); 
                setPosts(res.data);
                console.log("모든 게시글:", res.data);
            } catch (err) {
                console.error("모든 게시글 불러오기 실패:", err);
                setError("게시글을 불러오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchAllPosts();
    }, []); // 의존성 배열을 비워 컴포넌트 마운트 시 한 번만 실행

    if (loading) {
        return <div>게시글을 불러오는 중...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }



    return (
        <div>
            <h3>모든 게시글</h3>
            {posts.length > 0 ? (
                <ul>
                    {posts.map(post => (
                        <li key={post.id}>{post.title}</li> // TODO: 실제 게시글 데이터 구조에 맞게 수정
                    ))}
                </ul>
            
            ) : (
                <p>게시글이 없습니다.</p>
            )}
            <Link to={"/board/new"}>글쓰기</Link>
        </div>
    )
}

export default BoardPageForm