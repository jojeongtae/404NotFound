import React, { useEffect, useState } from 'react'
import apiClient from '../../api/apiClient';
import { Link } from 'react-router-dom'; // Link 임포트

const BoardPageForm = ({ boardId }) => { // boardId prop 다시 받기
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [pointBoard, setPointBoard] = useState(false);
    // boardId를 한글 이름으로 매핑하는 객체
    const boardNames = {
        free: '자유 게시판',
        notice: '공지사항',
        qna: 'Q&A 게시판',
        info: "정보 게시판",
        food: "먹거리 게시판",
        used: "중고 게시판",
        quiz: "퀴즈 게시판",
        survey: "설문조사 게시판",
        ox: "OX 게시판"
    };

    // boardId에 해당하는 한글 게시판 이름 또는 기본값 설정
    const displayBoardName = boardNames[boardId] || `${boardId} 게시판`;

    useEffect(() => {
        const fetchBoardPosts = async () => {
            if (!boardId) {
                setLoading(false);
                return; // boardId가 없으면 요청하지 않음
            }
            if (boardId === "ox" || boardId === "quiz") {
                setPointBoard(true);
            } else {
                setPointBoard(false);
            }
            try {
                setLoading(true);
                setError(null);
                const res = await apiClient.get(`/${boardId}/list`);
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
    console.log(posts);
    return (
        <div>
            <h3>{displayBoardName}</h3> {/* 한글 게시판 이름 사용 */}
            <div className="post-list-header"> {/* 헤더 추가 */}
                <span className="header-item header-id">번호</span>
                <span className="header-item header-title">제목</span>
                <span className="header-item header-author">글쓴이</span>
                <span className="header-item header-views">조회</span>
                {pointBoard ? "" :
                <span className="header-item header-recommend">추천</span>
                }
            </div>
            {pointBoard ?
                posts.length > 0 ?
                    <ul className="post-list"> {/* 클래스 추가 */}
                        {posts.map(post => (
                            <li key={post.id} className="post-list-item"> {/* 클래스 추가 */}
                                <Link to={`/board/${boardId}/${post.id}`} className="post-link"> {/* 클래스 추가 */}
                                    <span className="post-item post-id">{post.id}</span>
                                    <span className="post-item post-title">{post.title}</span>
                                    <span className="post-item post-author">{post.author}</span>
                                    <span className="post-item post-views">{post.views}</span>
                                </Link>
                            </li>
                        ))}
                    </ul>
                    :
                    (
                        <p>게시글이 없습니다.</p>
                    )
                :
                posts.length > 0 ? (

                    <ul className="post-list"> {/* 클래스 추가 */}
                        {posts.map(post => (
                            <li key={post.id} className="post-list-item"> {/* 클래스 추가 */}
                                <Link to={`/board/${boardId}/${post.id}`} className="post-link"> {/* 클래스 추가 */}
                                    <span className="post-item post-id">{post.id}</span>
                                    <span className="post-item post-title">{post.title}</span>
                                    <span className="post-item post-author">{post.author}</span>
                                    <span className="post-item post-views">{post.views}</span>
                                    <span className="post-item post-recommend">{post.recommend}</span> {/* 임시 추천 수 */}
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