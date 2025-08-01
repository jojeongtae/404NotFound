import React, { useEffect, useState, useRef, useCallback } from 'react'
import apiClient from '../../api/apiClient';
import { Link, useNavigate } from 'react-router-dom'; // useNavigate 임포트
import { getFullGradeDescription } from '../common/GradeDescriptions';
import SearchBoardForm from './SearchBoardForm'; // SearchBoardForm 임포트

const BoardPageForm = ({ boardId }) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [pointBoard, setPointBoard] = useState(false);
    const [showDropdown, setShowDropdown] = useState(false);
    const [dropdownPosition, setDropdownPosition] = useState({ x: 0, y: 0 });
    const [selectedUser, setSelectedUser] = useState(null);
    const [searchText, setSearchText] = useState(""); // 검색어 상태 (입력 필드와 직접 연결)
    const [searchOption, setSearchOption] = useState(""); // 검색 옵션 상태
    const [debouncedSearchText, setDebouncedSearchText] = useState(searchText); // 디바운싱된 검색어 상태
    const [topPosts, setTopPosts] = useState([]);
    const dropdownRef = useRef(null);
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [currentPage, setCurrentPage] = useState(1);
    const pageSize = 10; // 페이지당 게시글 수

    const boardNames = {
        free: '자유 게시판',
        notice: '공지사항',
        qna: 'Q&A 게시판',
        info: "정보 게시판",
        food: "먹거리 게시판",
        used: "중고 게시판",
        quiz: "퀴즈 게시판",
        survey: "설문조사 게시판",
        voting: "OX 게시판"
    };

    const displayBoardName = boardNames[boardId] || `${boardId} 게시판`;

    useEffect(() => {
        if (!boardId || pointBoard) return;

        const fetchTopPost = async () => {
            try {
                const response = await apiClient.get(`${boardId}/ranking/recommend`);
                setTopPosts(response.data.slice(0, 3));
            } catch (e) {
                console.log(e);
            }
        }
        fetchTopPost();
    }, [boardId, pointBoard]);
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setShowDropdown(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleNicknameClick = (event, user) => {
        setSelectedUser(user);
        // 클릭된 요소의 위치를 기준으로 드롭다운 위치 설정
        const rect = event.currentTarget.getBoundingClientRect();
        setDropdownPosition({
            x: rect.left + window.scrollX,
            y: rect.top + window.scrollY
        });
        setShowDropdown(prev => !prev); // 토글
    };

    // searchText가 변경될 때마다 디바운싱된 검색어 업데이트
    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedSearchText(searchText);
        }, 500); // 500ms 디바운스 지연

        return () => {
            clearTimeout(handler); // 이전 타이머 정리
        };
    }, [searchText]); // searchText가 변경될 때만 실행

    // 검색 버튼 클릭 시 호출될 함수
    const handleSearchSubmit = useCallback(() => {
        if (!searchText || !searchOption) {
            alert("검색 기준과 내용을 입력해주세요.");
            return;
        }
        // 버튼 클릭 시에는 디바운싱 지연 없이 즉시 검색 실행
        setDebouncedSearchText(searchText);
    }, [searchText, searchOption]);

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
                let url = `/${boardId}/list`;
                // API 호출 시 디바운싱된 검색어 사용
                if (debouncedSearchText && searchOption) {
                    url = `/${boardId}/search/${searchOption}?${searchOption}=${searchText}`;
                }
                const res = await apiClient.get(url);
                setPosts([...res.data].reverse());
                console.log(`게시판 ${boardId}의 게시글:`, res.data);
            } catch (err) {
                console.error(`게시판 ${boardId} 게시글 불러오기 실패:`, err);
                setError("게시글을 불러오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        // boardId, 디바운싱된 검색어, 검색 옵션이 변경될 때마다 다시 불러옴
        fetchBoardPosts();
    }, [boardId, debouncedSearchText, searchOption]); // boardId, debouncedSearchText, searchOption이 변경될 때마다 다시 불러옴

    // 현재 페이지에 표시할 게시글
    const startIndex = (currentPage - 1) * pageSize;
    const currentPosts = posts.slice(startIndex, startIndex + pageSize);
    const totalPages = Math.ceil(posts.length / pageSize);



    return (
        <div>
            <h3>{displayBoardName}</h3>
            {/* SearchBoardForm 컴포넌트 사용 */}
            <SearchBoardForm
                searchText={searchText}
                setSearchText={setSearchText}
                searchOption={searchOption}
                setSearchOption={setSearchOption}
                onSearchSubmit={handleSearchSubmit}
            />

            <table className="table post-list">
                <colgroup>
                    <col width="10%" />
                    <col width="auto" />
                    <col width="20%" />
                    <col width="10%" />
                    <col width="10%" />
                </colgroup>
                <thead>
                    <tr>
                        <th className="header-item header-id">번호</th>
                        <th className="header-item header-title">제목</th>
                        <th className="header-item header-author">글쓴이</th>
                        <th className="header-item header-views">조회</th>
                        {!pointBoard && <th className="header-item header-recommend">추천</th>}
                    </tr>
                </thead>
                <tbody>
                    {!pointBoard && topPosts.map(post => (
                        <tr key={`top-${post.id}`} className="post-list-item top-recommend">
                            <td className="post-item post-id" onClick={() => navigate(`/board/${boardId}/${post.id}`)}>{post.id}</td>
                            <td className="post-item post-title" onClick={() => navigate(`/board/${boardId}/${post.id}`)}>⭐ {post.title}</td>
                            <td className="post-item post-author" onClick={(e) => handleNicknameClick(e, { nickname: post.authorNickname, id: post.author, grade: post.grade })}>
                                <span className="user-grade">{getFullGradeDescription(post.grade)}</span>
                                {post.authorNickname}
                            </td>
                            <td className="post-item post-views">{post.views}</td>
                            <td className="post-item post-recommend">{post.recommend}</td>
                        </tr>

                    ))}

                    {currentPosts.length > 0 ? (
                        currentPosts.map(post => (
                            <tr key={post.id} className="post-list-item">
                                <td className="post-item post-id" onClick={() => navigate(`/board/${boardId}/${post.id}`)} style={{ cursor: 'pointer' }}>{post.id}</td>
                                <td className="post-item post-title" onClick={() => navigate(`/board/${boardId}/${post.id}`)} style={{ cursor: 'pointer' }}>{post.title}</td>
                                <td
                                    className="post-item post-author"
                                    style={{ textDecoration: 'underline', cursor: 'pointer' }}
                                    onClick={(e) => handleNicknameClick(e, { nickname: post.authorNickname, id: post.author, grade: post.grade })}
                                >
                                    <span className="user-grade">{getFullGradeDescription(post.grade)}</span>
                                    {post.authorNickname}
                                </td>
                                <td className="post-item post-views">{post.views}</td>
                                {!pointBoard && <td className="post-item post-recommend">{post.recommend}</td>}
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={!pointBoard ? 5 : 4} className="no-posts" style={{ textAlign: 'center' }}>
                                게시글이 없습니다.
                            </td>
                        </tr>
                    )}
                </tbody>
            </table>
            {/* 페이지 네이션 */}
            <div>
                <button onClick={() => setCurrentPage(e => Math.max(e - 1, 1))}
                    disabled={currentPage === 1}
                >
                    이전
                </button>
                <span>{currentPage} / {totalPages}</span>
                <button
                    onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                    disabled={currentPage === totalPages}
                >
                    다음
                </button>
            </div>

            {/* 드롭다운 메뉴 */}
            {showDropdown && selectedUser && (
                <div
                    ref={dropdownRef}
                    className="user-info-dropdown"
                    style={{
                        position: 'absolute',
                        top: dropdownPosition.y - 180,
                        left: dropdownPosition.x + 30
                    }}
                >
                    <div className="title">{selectedUser.nickname}</div>
                    <Link className="btn small" to={`/user/board/${selectedUser.id}`} onClick={() => setShowDropdown(false)}>작성글 보기</Link>
                    <Link className="btn small" to={`/user/userinfo/${selectedUser.id}`} onClick={() => setShowDropdown(false)}>유저 정보 보기</Link>
                </div>
            )}
        </div>
    )
}
export default BoardPageForm;
