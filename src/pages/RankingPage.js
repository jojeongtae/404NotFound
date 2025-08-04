import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import apiClient from '../api/apiClient';
import { getFullGradeDescription } from '../features/common/GradeDescriptions';
import { GetBoardName } from '../features/board/GetBoardName';

const RankingPage = ({ props }) => {
  const { type } = useParams(); // URL 파라미터에서 랭킹 타입 (recommend 또는 comment)을 가져옴
  console.log("RankingPage - type:", type);
  const [rankingData, setRankingData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRankingData = async () => {
      setLoading(true);
      setError(null);
      try {
        if(type === "comments"){
        const response = await apiClient.get(`/ranking/comments/week`);
        console.log("Original Data (before sort):", response.data);
        // 댓글 수 기준으로 내림차순 정렬
        const sortedData = [...response.data].sort((a, b) => Number(b.commentCount) - Number(a.commentCount));
        console.log("Sorted Data:", sortedData);
        setRankingData(sortedData);

        }else if(type === "recommend"){
        const response = await apiClient.get(`/ranking/recommend/week`);
        setRankingData(response.data);
        console.log(response.data);
        }else if(type === "view"){
          const response = await apiClient.get('/ranking/views/week')
          setRankingData(response.data);
          console.log(response.data);
        }

        else{
          const response = await apiClient.get(`/ranking/recommend/all`);
        setRankingData(response.data);
        console.log(response.data);
        }

      } catch (err) {
        console.error("랭킹 데이터 불러오기 실패:", err);
        setError("랭킹 데이터를 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchRankingData();
  }, [type]); // type이 변경될 때마다 데이터를 다시 불러옴

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>랭킹 데이터를 불러오는 중...</div>;
  }

  if (error) {
    return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
  }

  return (
      <div className="ranking-page">
        <h3>
          {type === 'recommend'
              ? '주간 추천수 랭킹'
              : type === 'comments'
                  ? '주간 댓글수 랭킹'
                  : type === 'view'
                      ? '주간 조회수 랭킹'
                      : '전체 추천수 랭킹'}
        </h3>

        {rankingData.length === 0 ? (
            <p>랭킹 데이터가 없습니다.</p>
        ) : (
            <ol className="ranking-list">
              {rankingData.map((item, index) => (
                  <li key={index} className="ranking-item">
                    <strong>{index + 1}.</strong>
                    {item.title} | {GetBoardName((item.category || '').toLowerCase())} | (작성자: <span className="user-grade">{getFullGradeDescription(item.grade)}</span> {item.authorNickname},
                    {type === 'comments' ? `댓글수: ${item.commentCount}` : type === 'recommend' ? `추천수${item.recommend}` : type === 'view' ? `조회수${item.views}` : `전체 추천수: ${item.recommend}`})
                  </li>
              ))}
            </ol>
        )}
      </div>
  );
};

export default RankingPage;