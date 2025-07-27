import { useState } from 'react'
import apiClient from '../../api/apiClient';
<<<<<<< HEAD
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom'; // useNavigate 임포트
import SurveyCreationForm from './SurveyCreationForm'; // SurveyCreationForm 임포트
=======
import { useSelector, useDispatch } from 'react-redux'; // useDispatch 추가
import { useNavigate } from 'react-router-dom';
import SurveyCreationForm from './SurveyCreationForm';
import { setUser } from '../../features/auth/userSlice'; // setUser 임포트 (경로 확인)
>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780

const NewBoardForm = () => {
    const [title, setTitle] = useState("");
    const [textBody, setTextBody] = useState("");
<<<<<<< HEAD
    const [quizQuestion, setQuizQuestion] = useState(""); // 퀴즈 문제 상태 추가
    const [quizAnswer, setQuizAnswer] = useState("");     // 퀴즈 정답 상태 추가
    // 설문조사 관련 상태 추가
=======
    const [price, setPrice] = useState("");
    const [quizQuestion, setQuizQuestion] = useState("");
    const [quizAnswer, setQuizAnswer] = useState("");
>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780
    const [surveyQuestion, setSurveyQuestion] = useState('');
    const [surveyColumn1, setSurveyColumn1] = useState('');
    const [surveyColumn2, setSurveyColumn2] = useState('');
    const [surveyColumn3, setSurveyColumn3] = useState('');
    const [surveyColumn4, setSurveyColumn4] = useState('');
    const [surveyColumn5, setSurveyColumn5] = useState('');
<<<<<<< HEAD
    // OX 게시판 관련 상태 추가
    const [oxVotingQuestion, setOxVotingQuestion] = useState('');
    const [oxVotingCorrectAnswer, setOxVotingCorrectAnswer] = useState('');

    const user = useSelector(state => state.user);
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [boardId, setBoardId] = useState("");
    const [quizId, setQuizId] = useState("");
    const handleNewBoard = async () => {
        let boardData = {
            author: user.username,
            title: title,
            body: textBody,
        }

        // 퀴즈 게시판일 경우 문제와 정답 추가
        if (boardId === 'quiz') {
            boardData = {
                ...boardData,
                author: user.username,
                question: quizQuestion,
                answer: quizAnswer.toLocaleUpperCase,
                title,
                type: quizId
            };
        } else if (boardId === 'survey') { // 설문조사 게시판일 경우 데이터 추가
            boardData = {
                author: user.username,
                title: title,
                question: surveyQuestion,
                column1: surveyColumn1,
                column2: surveyColumn2,
                column3: surveyColumn3,
                column4: surveyColumn4,
                column5: surveyColumn5,
                category: boardId,
            };
        } else if (boardId === 'voting') { // OX 게시판일 경우 데이터 추가
            boardData = {
                author: user.username,
                title: title,
                question: oxVotingQuestion,
                answer: oxVotingCorrectAnswer,
                category: boardId,
            };
        }

        console.log(boardData);
        try {
            const res = await apiClient.post(`/${boardId}/new`, boardData);
            console.log(user.role);
            console.log(res.data);
            alert('글이 성공적으로 작성되었습니다!'); // 성공 알림

            navigate(`/board/${boardId}`); // 예)'free' 게시판으로 이동
        } catch (error) {
            console.log(error);
            alert('글 작성에 실패했습니다.'); // 실패 알림
        }
    }



=======
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
        let imageUrl = null;

        try {
            const boardFormData = new FormData();
            const boardDTO = {
                'author': user.username,
                'title': title,
                'body': textBody,
            }
            if (boardId === 'quiz') {
                boardFormData.append('question', quizQuestion);
                boardFormData.append('answer', quizAnswer.toLocaleUpperCase());
                boardFormData.append('type', quizId);
            } else if (boardId === 'survey') {
                boardFormData.append('question', surveyQuestion);
                boardFormData.append('column1', surveyColumn1);
                boardFormData.append('column2', surveyColumn2);
                boardFormData.append('column3', surveyColumn3);
                boardFormData.append('column4', surveyColumn4);
                boardFormData.append('column5', surveyColumn5);
                boardFormData.append('category', boardId);
            } else if (boardId === 'voting') {
                boardFormData.append('question', oxVotingQuestion);
                boardFormData.append('answer', oxVotingCorrectAnswer);
                boardFormData.append('category', boardId);
            } else if (boardId === 'used') {
                boardDTO.price = Number(price);


            }
            if (selectedImage) {
                boardFormData.append("file", selectedImage);
            }
            boardFormData.append(
                "boardDTO",
                new Blob([JSON.stringify(boardDTO)], { type: "application/json" })
            );
            console.log(boardFormData)
            console.log("게시글 데이터 전송 시작...");
            const res = await apiClient.post(`/${boardId}/new`, boardFormData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log("게시글 작성 응답:", res.data);

            alert('글이 성공적으로 작성되었습니다!');
            navigate(`/board/${boardId}`);

            // --- 추가된 부분: 사용자 정보 업데이트 ---
            const fetchUserInfo = async () => {
                try {
                    const userInfoRes = await apiClient.get(`/user/user-info?username=${user.username}`);
                    dispatch(setUser(userInfoRes.data));
                    console.log("User info updated after new board post:", userInfoRes.data);
                } catch (userInfoError) {
                    console.error("Failed to fetch user info after new board post:", userInfoError);
                }
            };
            fetchUserInfo();
            // --- 추가된 부분 끝 ---

        } catch (error) {
            console.error('글 작성 실패:', error);
            alert('글 작성에 실패했습니다. ' + (error.response?.data?.message || error.message));
        }
    }

>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780
    return (
        <>
            <form onSubmit={(e) => {
                e.preventDefault();
<<<<<<< HEAD
                handleNewBoard(); // 설문조사 게시판이든 아니든 여기서 API 호출
=======
                handleNewBoard();
>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780
            }}>
                {/* 게시판 선택 드롭다운 */}
                <select id="boardSelect" onChange={(e) => setBoardId(e.target.value)}>
                    <option value="">게시판을 선택</option>
                    {user.role === "ROLE_ADMIN" && (
                        <option value="notice">공지 사항</option>
                    )}
                    <option value="free">자유 게시판</option>
                    <option value="food">먹거리 게시판</option>
                    <option value="info">정보 게시판</option>
                    <option value="qna">Q&A 게시판</option>
                    <option value="used">중고 게시판</option>
                    <option value="quiz">퀴즈 게시판</option>
                    <option value="survey">설문조사 게시판</option>
                    <option value="voting">OX게시판</option>
                </select>
                <br />

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
                    <>
                        <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                        <select id='quizSelect' onChange={(e) => setQuizId(e.target.value)}>
                            <option value="MULTI">객관식</option>
                            <option value="SUBJECTIVE">주관식</option>
                            <option value="OX">OX</option>
                        </select>
                        <input type='text' placeholder='퀴즈 문제' value={quizQuestion} onChange={(e) => setQuizQuestion(e.target.value)}></input> <br />
                        <input type='text' placeholder='퀴즈 정답' value={quizAnswer} onChange={(e) => setQuizAnswer(e.target.value)}></input>
                    </>
                ) : boardId === "voting" ? (
                    <>
                        <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                        <input type='text' placeholder='OX 문제' value={oxVotingQuestion} onChange={(e) => setOxVotingQuestion(e.target.value)}></input> <br />
                        <div>
                            <label>
                                <input
                                    type="radio"
                                    name="oxAnswer"
                                    value="O"
                                    checked={oxVotingCorrectAnswer === 'O'}
                                    onChange={(e) => setOxVotingCorrectAnswer(e.target.value)}
                                />
                                O
                            </label>
                            <label style={{ marginLeft: '10px' }}>
                                <input
                                    type="radio"
                                    name="oxAnswer"
                                    value="X"
                                    checked={oxVotingCorrectAnswer === 'X'}
                                    onChange={(e) => setOxVotingCorrectAnswer(e.target.value)}
                                />
                                X
                            </label>
                        </div>
                    </>
<<<<<<< HEAD
                ) : (
                    <>
                        <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                        <textarea placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea>
                    </>
                )
=======
                ) : boardId === 'used' ? (
                    <>
                        <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                        <textarea placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea><br></br>
                      <input type='text' placeholder='가격' value={price} onChange={(e) => setPrice(e.target.value)} /><br />
                        <input
                            type='file'
                            accept="image/*"
                            onChange={handleImageChange}
                        /><br />
                        {imagePreviewUrl && (
                            <div>
                                <img src={imagePreviewUrl} alt="Image Preview" style={{ maxWidth: '200px', maxHeight: '200px', marginTop: '10px' }} />
                            </div>
                        )}
                    </>
                )

                    : (
                        <>
                            <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                            <textarea placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea><br></br>
                            <input
                                type='file'
                                accept="image/*"
                                onChange={handleImageChange}
                            /><br />
                            {imagePreviewUrl && (
                                <div>
                                    <img src={imagePreviewUrl} alt="Image Preview" style={{ maxWidth: '200px', maxHeight: '200px', marginTop: '10px' }} />
                                </div>
                            )}
                        </>
                    )
>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780
                }
                <input type="submit" value="전송" />
            </form>
        </>
    )
}

<<<<<<< HEAD
export default NewBoardForm
=======
export default NewBoardForm;
>>>>>>> 2422581d9c642c9b19c9bf40394aaee9f4fdc780
