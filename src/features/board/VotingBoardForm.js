import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { getFullGradeDescription } from "../common/GradeDescriptions";

const VotingBoardForm = () => {
    const { boardId, postId } = useParams();
    const [votingData, setVotingData] = useState(null);
    const [selectedOption, setSelectedOption] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [hasParticipated, setHasParticipated] = useState(false); // 참여 여부 상태
    const [votingResults, setVotingResults] = useState(null); // 투표 결과 상태 (집계된 득표수)
    const [totalVotes, setTotalVotes] = useState(0); // 총 투표수 상태
    const [resultsLoading, setResultsLoading] = useState(false); // 결과 로딩 상태
    const [resultsError, setResultsError] = useState(null); // 결과 에러 상태
    const user = useSelector(state => state.user);
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

    // 투표 데이터 불러오기
    useEffect(() => {
        const fetchVoting = async () => {
            try {
                setLoading(true);
                const res = await apiClient.get(`/${boardId}/${postId}`);
                setVotingData(res.data);
            } catch (err) {
                console.error("투표 데이터 불러오기 실패:", err);
                setError("투표 데이터를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        if (boardId && postId) {
            fetchVoting();
            // TODO: 사용자가 이미 참여했는지 백엔드에서 확인하는 로직 추가 (예: /voting-responses/check?userId=...&votingId=...)
            // 만약 이미 참여했다면 setHasParticipated(true) 및 fetchVotingResults() 호출
        }
    }, [boardId, postId]);

    // 투표 결과 불러오기 (프론트엔드에서 집계)
    const fetchVotingResults = async () => {
        setResultsLoading(true);
        setResultsError(null);
        try {
            const res = await apiClient.get(`/voting-answers/${postId}`); 
            const rawAnswers = res.data; // 백엔드에서 받은 개별 응답 데이터
            // 응답 데이터 집계
            const aggregatedResults = {};
            let currentTotalVotes = 0;
            rawAnswers.forEach(answer => {
                const option = answer.answers.toUpperCase(); // 'answers' 필드에 선택된 옵션 값이 있다고 가정
                aggregatedResults[option] = (aggregatedResults[option] || 0) + 1;
                currentTotalVotes++;
            });
            setVotingResults(aggregatedResults);
            setTotalVotes(currentTotalVotes);

        } catch (err) {
            console.error("투표 결과 불러오기 실패:", err);
            setResultsError("투표 결과를 불러오는데 실패했습니다.");
        } finally {
            setResultsLoading(false);
        }
    };

    const handleChange = (event) => {
        setSelectedOption(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!selectedOption) {
            alert("옵션을 선택해주세요.");
            return;
        }
        try {
            const responseData = {
                parent: postId,
                username: user.username,
                answers: selectedOption.toUpperCase(),
            };
            const res = await apiClient.post("/voting-answers/new", responseData);
            console.log("투표 응답 제출 성공:", res.data);
            alert("투표 응답이 제출되었습니다!");
            setHasParticipated(true); // 참여 완료
            fetchVotingResults(); // 결과 불러오기
        } catch (err) {
            console.error("투표 응답 제출 실패:", err);
            alert("투표 응답 제출에 실패했습니다.");
        }
    };

    if (loading) {
        return <div style={{ textAlign: 'center', padding: '50px' }}>투표를 불러오는 중...</div>;
    }

    if (error) {
        return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
    }

    if (!votingData) {
        return <div style={{ textAlign: 'center', padding: '50px' }}>투표를 찾을 수 없습니다.</div>;
    }

    const options = [
        { value: 'O', label: '⭕' },
        { value: 'X', label: '❌' },
    ];

    return(
        <div className="voting-detail stats">
            <h3>{votingData.title}</h3>
            <ul className="detail-info">
                <li className="author">
                    <span>작성자: </span><span className="user-grade">{getFullGradeDescription(votingData.grade)}</span>{votingData.authorNickname}
                </li>
                <li className="date">
                    <span>작성일: </span>{new Date(votingData.createdAt).toLocaleDateString()}
                </li>
            </ul>

            {votingData &&(
            <img
                src={`http://404notfoundpage.duckdns.org/api/${votingData.imgsrc}`}
                alt={votingData.title+' 이미지' || '게시글 이미지'}
                className="detail-img"
              />
            )}
            
            <p className="question">❓{votingData.question}</p>

            {hasParticipated ? (
                // 투표 결과 표시
                <div className="stats-result">
                    <h4>투표 결과</h4>
                    {resultsLoading && <p>결과를 불러오는 중...</p>}
                    {resultsError && <p style={{ color: 'red' }}>{resultsError}</p>}
                    {votingResults && (
                        <div className="results-container">
                        <p className="result-title">총 투표수: <span>{totalVotes} 표</span></p>
                            <ul className="result-list">
                                {options.map((option, index) => {
                                    const voteCount = votingResults[option.value] || 0;
                                    const percentage = totalVotes > 0 ? ((voteCount / totalVotes) * 100).toFixed(1) : 0;
                                    return (
                                        <li key={index} className="result-item">
                                            {option.label} : <strong>{voteCount} 표</strong> ({percentage}%)
                                        </li>
                                    );
                                })}
                            </ul>
                        </div>
                    )}
                </div>
            ) : (
                // 투표 폼 표시
                <form onSubmit={handleSubmit}>
                    <ul className="answer-list">
                    {options.map((option, index) => (
                        <li key={index}>
                            <label>
                                <input
                                    type="radio"
                                    name="votingOption"
                                    value={option.value}
                                    checked={selectedOption === option.value}
                                    onChange={handleChange}
                                />
                                {option.label}
                            </label>
                        </li>
                    ))}
                    </ul>
                    <button type="submit" className="btn type2 large submit">제출</button>
                </form>
            )}
        </div>
    );
}

export default VotingBoardForm;
