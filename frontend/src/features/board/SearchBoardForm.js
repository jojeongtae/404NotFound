import React from 'react'; // useState는 이제 부모에서 관리하므로 제거

const SearchBoardForm = ({ searchText, setSearchText, searchOption, setSearchOption, onSearchSubmit }) => {
    const handleSearch = (e) => {
        e.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
        if (onSearchSubmit) {
            onSearchSubmit(); // 부모 컴포넌트의 검색 함수 호출
        }
    };

    return (
        <div className="search-board-form">
            <form onSubmit={handleSearch}>
                <select value={searchOption} onChange={(e) => setSearchOption(e.target.value)}>
                    <option value="">제목 또는 닉네임</option>
                    <option value="title">제목</option>
                    <option value="author">닉네임</option>
                </select>
                <input
                    type="text"
                    placeholder="검색어를 입력하세요"
                    className="search-input"
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                />
                <button type="submit" className="btn type2 submit">검색</button>
            </form>
        </div>
    );
};

export default SearchBoardForm;