import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import apiClient from '../api/apiClient';

const RankingPage = () => {
  const { type } = useParams(); // URL 파라미터에서 랭킹 타입 (recommend 또는 comment)을 가져옴
  const [rankingData, setRankingData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRankingData = async () => {
      setLoading(true);
      setError(null);
      try {
        if(type === "comments"){
        const response = await apiClient.get(`/ranking/comments`);
        setRankingData(response.data);
        console.log(response.data);

        }else{
        const response = await apiClient.get(`/ranking/recommend`);
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
    <div>
      <h2>{type === 'recommend' ? '추천수 랭킹' : '댓글수 랭킹'}</h2>
      {rankingData.length === 0 ? (
        <p>랭킹 데이터가 없습니다.</p>
      ) : (
        <ol>
          {rankingData.map((item, index) => (
            <li key={item.id} style={{ marginBottom: '10px' }}>
              <strong>{index + 1}.</strong> {item.title} (작성자: {/* authorNickname 변경예정 */} {item.author}, {type === 'recommend' ? `추천수 : ${item.recommend}` : `댓글수${item.commentCount}`})
            </li>
          ))}
        </ol>
      )}
    </div>
  );
};

export default RankingPage;
