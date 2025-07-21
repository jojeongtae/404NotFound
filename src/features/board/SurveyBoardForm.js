import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';

const SurveyBoardForm = () => {
    const { boardId, postId } = useParams();
    const [surveyData, setSurveyData] = useState(null);
    const [selectedOption, setSelectedOption] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const user = useSelector(state => state.user);

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
        }
    }, [boardId, postId]);

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
            // 제출 후 폼 비활성화 또는 다른 페이지로 이동 등 추가 로직
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
        <div style={{ padding: '20px' }}>
            <h2>{surveyData.title}</h2>
            <h3>{surveyData.question}</h3>
            <form onSubmit={handleSubmit}>
                {columns.map((column, index) => (
                    <div key={index}>
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
                    </div>
                ))}
                <br />
                <button type="submit">제출</button>
            </form>
        </div>
    );
}

export default SurveyBoardForm;