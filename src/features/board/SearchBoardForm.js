import React from 'react'; // useState는 이제 부모에서 관리하므로 제거

const SearchBoardForm = ({ searchText, setSearchText, searchOption, setSearchOption, onSearchSubmit }) => {
    const handleSearch = (e) => {
        e.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
        if (onSearchSubmit) {
            onSearchSubmit(); // 부모 컴포넌트의 검색 함수 호출
        }
    };

    return (
        <form onSubmit={handleSearch} style={{ marginBottom: '20px', display: 'flex', gap: '10px' }}>
            <select value={searchOption} onChange={(e) => setSearchOption(e.target.value)}
                    style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}>
                <option value="">제목 또는 닉네임</option>
                <option value="title">제목</option>
                <option value="author">닉네임</option>
            </select>
            <input
                type="text"
                placeholder="검색어를 입력하세요"
                value={searchText}
                onChange={(e) => setSearchText(e.target.value)}
                style={{ flexGrow: 1, padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
            />
            <button type="submit" style={{ padding: '8px 15px', borderRadius: '4px', border: 'none', backgroundColor: '#007bff', color: 'white', cursor: 'pointer' }}>
                검색
            </button>
        </form>
    );
};

export default SearchBoardForm;