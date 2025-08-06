import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { getFullGradeDescription } from "../common/GradeDescriptions";

const SurveyBoardForm = () => {
    const { boardId, postId } = useParams();
    const [surveyData, setSurveyData] = useState(null);
    const [selectedOption, setSelectedOption] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [hasParticipated, setHasParticipated] = useState(false); // 참여 여부 상태
    const [surveyResults, setSurveyResults] = useState(null); // 설문조사 결과 상태 (집계된 득표수)
    const [totalVotes, setTotalVotes] = useState(0); // 총 투표수 상태
    const [resultsLoading, setResultsLoading] = useState(false); // 결과 로딩 상태
    const [resultsError, setResultsError] = useState(null); // 결과 에러 상태
    const user = useSelector(state => state.user);
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

    // 설문조사 데이터 불러오기
    useEffect(() => {
        const fetchSurvey = async () => {
            try {
                setLoading(true);
                const res = await apiClient.get(`/${boardId}/${postId}`);
                setSurveyData(res.data);
            } catch (err) {
                console.error("설문조사 데이터 불러오기 실패:", err);
                setError("설문조사 데이터를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        if (boardId && postId) {
            fetchSurvey();
            // TODO: 사용자가 이미 참여했는지 백엔드에서 확인하는 로직 추가 (예: /survey-responses/check?userId=...&surveyId=...)
            // 만약 이미 참여했다면 setHasParticipated(true) 및 fetchSurveyResults() 호출
        }
    }, [boardId, postId]);

    // 설문조사 결과 불러오기 (프론트엔드에서 집계)
    const fetchSurveyResults = async () => {
        setResultsLoading(true);
        setResultsError(null);
        try {
            const res = await apiClient.get(`/survey-answers/${postId}`); 
            const rawAnswers = res.data; // 백엔드에서 받은 개별 응답 데이터

            // 응답 데이터 집계
            const aggregatedResults = {};
            let currentTotalVotes = 0;
            rawAnswers.forEach(answer => {
                const option = answer.answers; // 'answers' 필드에 선택된 옵션 값이 있다고 가정
                aggregatedResults[option] = (aggregatedResults[option] || 0) + 1;
                currentTotalVotes++;
            });
            setSurveyResults(aggregatedResults);
            setTotalVotes(currentTotalVotes);

        } catch (err) {
            console.error("설문조사 결과 불러오기 실패:", err);
            setResultsError("설문조사 결과를 불러오는데 실패했습니다.");
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
                parentId: postId,
                username: user.username,
                answers: selectedOption,
            };
            const res = await apiClient.post("/survey-answers/new", responseData);
            console.log("설문조사 응답 제출 성공:", res.data);
            alert("설문조사 응답이 제출되었습니다!");
            setHasParticipated(true); // 참여 완료
            fetchSurveyResults(); // 결과 불러오기
        } catch (err) {
            console.error("설문조사 응답 제출 실패:", err);
            alert("설문조사 응답 제출에 실패했습니다.");
        }
    };

    if (loading) {
        return <div style={{ textAlign: 'center', padding: '50px' }}>설문조사를 불러오는 중...</div>;
    }

    if (error) {
        return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
    }

    if (!surveyData) {
        return <div style={{ textAlign: 'center', padding: '50px' }}>설문조사를 찾을 수 없습니다.</div>;
    }

    const columns = [
        surveyData.column1,
        surveyData.column2,
        surveyData.column3,
        surveyData.column4,
        surveyData.column5,
    ].filter(col => col);

    return(
        <div className="survey-detail stats">
            <h3>{surveyData.title}</h3>
            <ul className="detail-info">
                <li className="author">
                    <span>작성자:</span> <span className="user-grade">{getFullGradeDescription(surveyData.grade)}</span>{surveyData.authorNickname}
                </li>
                <li className="date">
                    <span>작성일: </span>{new Date(surveyData.createdAt).toLocaleDateString()}
                </li>
            </ul>
            <p className="question">❓{surveyData.question}</p>

            {hasParticipated ? (
                // 설문조사 결과 표시
                <div className="stats-result">
                    <h4>설문조사 결과</h4>
                    {resultsLoading && <p>결과를 불러오는 중...</p>}
                    {resultsError && <p style={{ color: 'red' }}>{resultsError}</p>}
                    {surveyResults && (
                        <div className="results-container">
                            <p className="result-title">총 투표수 : <span>{totalVotes} 표</span></p>
                            <ul className="result-list">
                                {columns.map((column, index) => {
                                    const voteCount = surveyResults[column] || 0;
                                    const percentage = totalVotes > 0 ? ((voteCount / totalVotes) * 100).toFixed(1) : 0;
                                    return (
                                        <li key={index} className="result-item">
                                            {column} : <strong>{voteCount} 표</strong> ({percentage}%)
                                        </li>
                                    );
                                })}
                            </ul>
                        </div>
                    )}
                </div>
            ) : (
                // 설문조사 폼 표시
                <form onSubmit={handleSubmit}>
                    {surveyData.imgsrc &&(
                        <div style={{ marginBottom: '20px' }}>
                          <img
                              src={`http://404notfoundpage.duckdns.org/${surveyData.imgsrc}`}
                              alt={surveyData.title+" 이미지" || '게시글 이미지'}
                              style={{
                                display: 'block',
                                marginLeft: 'auto',
                                marginRight: 'auto',
                                maxWidth: '800px',
                                width: '100%',
                                height: 'auto',
                                borderRadius: '8px',
                                boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                              }}
                          />
                        </div>
                    )}
                    <ul className="answer-list">
                        {columns.map((column, index) => (
                            <li key={index}>
                                <label>
                                    <input
                                        type="radio"
                                        name="surveyOption"
                                        value={column}
                                        checked={selectedOption === column}
                                        onChange={handleChange}
                                    />
                                    {column}
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

export default SurveyBoardForm;
