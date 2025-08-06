import { useState } from 'react'
import apiClient from '../../api/apiClient';
import { useSelector, useDispatch } from 'react-redux'; // useDispatch 추가
import { useNavigate } from 'react-router-dom';
import SurveyCreationForm from './SurveyCreationForm';
import { setUser } from '../../features/auth/userSlice'; // setUser 임포트 (경로 확인)

const NewBoardForm = () => {
    const [title, setTitle] = useState("");
    const [textBody, setTextBody] = useState("");
    const [price, setPrice] = useState("");
    const [quizQuestion, setQuizQuestion] = useState("");
    const [quizAnswer, setQuizAnswer] = useState("");
    const [surveyQuestion, setSurveyQuestion] = useState('');
    const [surveyColumn1, setSurveyColumn1] = useState('');
    const [surveyColumn2, setSurveyColumn2] = useState('');
    const [surveyColumn3, setSurveyColumn3] = useState('');
    const [surveyColumn4, setSurveyColumn4] = useState('');
    const [surveyColumn5, setSurveyColumn5] = useState('');
    const [oxVotingQuestion, setOxVotingQuestion] = useState('');
    const [oxVotingCorrectAnswer, setOxVotingCorrectAnswer] = useState('');

    const [selectedImage, setSelectedImage] = useState(null);
    const [imagePreviewUrl, setImagePreviewUrl] = useState('');

    const user = useSelector(state => state.user);
    const navigate = useNavigate();
    const dispatch = useDispatch(); // useDispatch 초기화
    const [boardId, setBoardId] = useState("");
    const [quizId, setQuizId] = useState("");

    const handleImageChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            setSelectedImage(file);
            const reader = new FileReader();
            reader.onloadend = () => {
                setImagePreviewUrl(reader.result);
            };
            reader.readAsDataURL(file);
        } else {
            setSelectedImage(null);
            setImagePreviewUrl('');
        }
    };

      const handleNewBoard = async () => {
        try {
            let res;
            // 1. 모든 게시판 유형에 공통적인 기본 데이터 객체를 먼저 생성합니다.
            const commonData = {
                author: user.username,
                title: title,
                body: textBody,
            };

            if(commonData.title === ""){
                alert('제목을 입력해 주세요.');
                return;
            }
            // if(commonData.body === "") {
            //     alert('내용을 입력해 주세요.');
            //     return;
            // }
            
                // 2. JSON 전용 게시판의 경우, 공통 데이터에 특정 필드를 추가합니다.
                const requestData = { ...commonData };

                if (boardId === 'quiz') {
                    requestData.question = quizQuestion;
                    requestData.answer = quizAnswer.toLocaleUpperCase();
                    requestData.type = quizId.toUpperCase();
                } else if (boardId === 'survey') {
                    requestData.question = surveyQuestion;
                    requestData.column1 = surveyColumn1;
                    requestData.column2 = surveyColumn2;
                    requestData.column3 = surveyColumn3;
                    requestData.column4 = surveyColumn4;
                    requestData.column5 = surveyColumn5;
                    requestData.category = boardId;
                } else if (boardId === 'voting') {
                    requestData.question = oxVotingQuestion;
                    requestData.answer = oxVotingCorrectAnswer;
                    requestData.category = boardId;
                }
                
                // 3. Multipart/form-data를 사용하는 게시판의 경우, 공통 데이터를 DTO로 사용합니다.
                const boardDTO = { ...requestData };
                const boardFormData = new FormData();
                
                if (boardId === 'used') {
                    if (price === "") {
                        alert("가격을 입력해 주세요");
                        return;
                    }
                    boardDTO.price = Number(price);
                }

                boardFormData.append(
                    "boardDTO",
                    new Blob([JSON.stringify(boardDTO)], { type: "application/json" })
                );

                if (selectedImage) {
                    boardFormData.append("file", selectedImage);
                }
                // console.log("boardDTO JSON:", JSON.stringify(boardDTO));

                // console.log("FormData (with boardDTO) 전송 시작...");
               res = await apiClient.post(`/${boardId}/new`, boardFormData, {
  headers: {
    'Content-Type': undefined, // <- Axios가 multipart로 자동 설정
  },
});

            // console.log("게시글 작성 응답:", res.data);
            alert('글이 성공적으로 작성되었습니다!');
            navigate(`/board/${boardId}`);

            const fetchUserInfo = async () => {
                try {
                    const userInfoRes = await apiClient.get(`/user/user-info?username=${user.username}`);
                    dispatch(setUser(userInfoRes.data));
                    // console.log("User info updated after new board post:", userInfoRes.data);
                } catch (userInfoError) {
                    console.error("Failed to fetch user info after new board post:", userInfoError);
                }
            };
            fetchUserInfo();

        } catch (error) {
            console.error('글 작성 실패:', error);
            alert('글 작성에 실패했습니다. ' + (error.response?.data?.message || error.message));
        }
    }

    return (
        <div className="new-board">
            <form onSubmit={(e) => {
                e.preventDefault();
                handleNewBoard();
            }}>
                <h3>글쓰기</h3>
                {/* 게시판 선택 드롭다운 */}
                <select id="boardSelect" onChange={(e) => setBoardId(e.target.value)}>
                    <option value="">게시판 선택</option>
                    {user.role === "ROLE_ADMIN" && (<option value="notice">공지 사항</option>)}
                    <option value="free">자유 게시판</option>
                    <option value="food">먹거리 게시판</option>
                    <option value="info">정보 게시판</option>
                    <option value="qna">Q&A 게시판</option>
                    <option value="used">중고 게시판</option>
                    <option value="quiz">퀴즈 게시판</option>
                    <option value="survey">설문조사 게시판</option>
                    <option value="voting">OX게시판</option>
                </select>

                {boardId === 'survey' ? (
                    <SurveyCreationForm
                        title={title} setTitle={setTitle}
                        question={surveyQuestion} setQuestion={setSurveyQuestion}
                        column1={surveyColumn1} setColumn1={setSurveyColumn1}
                        column2={surveyColumn2} setColumn2={setSurveyColumn2}
                        column3={surveyColumn3} setColumn3={setSurveyColumn3}
                        column4={surveyColumn4} setColumn4={setSurveyColumn4}
                        column5={surveyColumn5} setColumn5={setSurveyColumn5}
                    />
                ) : boardId === 'quiz' ? (
                    // 퀴즈
                    <div className="new-board-quiz">
                        <input type='text' className="board-title" placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input>
                        <div className="row">
                            <select id='quizSelect' onChange={(e) => setQuizId(e.target.value)}>
                                <option value="MULTI">객관식</option>
                                <option value="SUBJECTIVE">주관식</option>
                                <option value="OX">OX</option>
                            </select>
                            <input type='text' className="quiz-question" placeholder='퀴즈 문제' value={quizQuestion} onChange={(e) => setQuizQuestion(e.target.value)}></input>
                        </div>
                        <input type='text' className="quiz-answer" placeholder='퀴즈 정답' value={quizAnswer} onChange={(e) => setQuizAnswer(e.target.value)}></input>
                    </div>
                ) : boardId === "voting" ? (
                    <div className="new-board-voting">
                        <input type='text' className="board-title" placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                        <input type='text' className="voting-question" placeholder='OX 문제' value={oxVotingQuestion} onChange={(e) => setOxVotingQuestion(e.target.value)}></input> <br />
                        <div className="voting-answer">
                            <strong>정답: </strong>
                            <label>
                                <input
                                    type="radio"
                                    name="oxAnswer"
                                    value="O"
                                    checked={oxVotingCorrectAnswer === 'O'}
                                    onChange={(e) => setOxVotingCorrectAnswer(e.target.value)}
                                /> ⭕
                            </label>
                            <label style={{ marginLeft: '14px' }}>
                                <input
                                    type="radio"
                                    name="oxAnswer"
                                    value="X"
                                    checked={oxVotingCorrectAnswer === 'X'}
                                    onChange={(e) => setOxVotingCorrectAnswer(e.target.value)}
                                /> ❌
                            </label>
                        </div>
                    </div>
                ) : boardId === 'used' ? (
                        <>
                            <input type='text' className="board-title" placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input>
                            <textarea className="board-body" placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea>
                            <div className="price-wrap">
                                <input type='number' className="board-price" placeholder='가격' value={price} onChange={(e) => setPrice(e.target.value)} /> <span className="unit">원</span>
                            </div>
                        </>
                    )

                    : (
                        <>
                            <input type='text' className="board-title" placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input>
                            <textarea className="board-body" placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea>
                        </>
                    )
                }
                <div className="new-board-image">
                    <input
                        type='file'
                        accept="image/*"
                        onChange={handleImageChange}
                    />
                    {imagePreviewUrl && (
                        <div className="img-wrapper">
                            <img src={imagePreviewUrl} alt="Image Preview" style={{ maxWidth: '200px', maxHeight: '200px'}} />
                        </div>
                    )}
                </div>
                <button type="submit" className="btn type2 large">글쓰기</button>
            </form>
        </div>
    )
}

export default NewBoardForm;
