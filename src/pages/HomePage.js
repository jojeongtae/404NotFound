import React from 'react';
import RankingPage from './RankingPage'; // RankingPage 컴포넌트 임포트

const HomePage = () => {
    return (
        <div style={{ textAlign: 'center', padding: '50px' }}>
            <h1>환영합니다!</h1>
            <p>이곳은 404NotFound의 메인 페이지입니다.</p>
            <p>왼쪽 게시판 목록을 통해 이동해주세요.</p>
            <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '20px' }}>
                <div style={{ flex: 1, marginRight: '10px' }}>
                    <RankingPage type="recommend" />
                </div>
                <div style={{ flex: 1, marginLeft: '10px' }}>
                    <RankingPage type="comment" />
                </div>
            </div>
        </div>
    );
};

export default HomePage;