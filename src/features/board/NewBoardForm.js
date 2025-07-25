import { useState } from 'react'
import apiClient from '../../api/apiClient';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom'; // useNavigate 임포트
import SurveyCreationForm from './SurveyCreationForm'; // SurveyCreationForm 임포트

const NewBoardForm = () => {
    const [title, setTitle] = useState("");
    const [textBody, setTextBody] = useState("");
    const [quizQuestion, setQuizQuestion] = useState(""); // 퀴즈 문제 상태 추가
    const [quizAnswer, setQuizAnswer] = useState("");     // 퀴즈 정답 상태 추가
    // 설문조사 관련 상태 추가
    const [surveyQuestion, setSurveyQuestion] = useState('');
    const [surveyColumn1, setSurveyColumn1] = useState('');
    const [surveyColumn2, setSurveyColumn2] = useState('');
    const [surveyColumn3, setSurveyColumn3] = useState('');
    const [surveyColumn4, setSurveyColumn4] = useState('');
    const [surveyColumn5, setSurveyColumn5] = useState('');
    // OX 게시판 관련 상태 추가
    const [oxVotingQuestion, setOxVotingQuestion] = useState('');
    const [oxVotingCorrectAnswer, setOxVotingCorrectAnswer] = useState('');

    // 이미지 업로드 관련 상태 추가
    const [selectedImage, setSelectedImage] = useState(null);
    const [imagePreviewUrl, setImagePreviewUrl] = useState('');

    const user = useSelector(state => state.user);
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [boardId, setBoardId] = useState("");
    const [quizId, setQuizId] = useState("");

    // 이미지 파일 변경 핸들러
    const handleImageChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            setSelectedImage(file);
            // 이미지 미리보기 URL 생성
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
        let imageUrl = null; // 이미지 URL을 저장할 변수

        try {
            // 1. 이미지 업로드 단계
            // if (selectedImage) {
            //     const imageFormData = new FormData();
            //     imageFormData.append('file', selectedImage); // 백엔드에서 'file' 필드명으로 받도록 가정

            //     console.log("이미지 업로드 시작...");
            //     const imageUploadRes = await apiClient.post('/upload', imageFormData, {
            //         headers: {
            //             'Content-Type': 'multipart/form-data'
            //         }
            //     });
            //     console.log("이미지 업로드 응답:", imageUploadRes.data);

            //     // 백엔드 응답에서 이미지 URL 추출 (실제 응답 구조에 따라 수정 필요)
            //     // 예: imageUploadRes.data.url 또는 imageUploadRes.data.filePath
            //     let cleanedPath = imageUploadRes.data.filePath.replace(/\\/g, '/'); // 모든 백슬래시를 슬래시로 변경
            //     // 'uploads/'로 시작한다면 해당 부분을 제거합니다.
            //     if (cleanedPath.startsWith('uploads/')) {
            //         cleanedPath = cleanedPath.substring('uploads/'.length);
            //     }
            //     // 최종적으로 '/upload/' 접두사를 붙입니다.
            //     imageUrl = '/resources/' + cleanedPath;
            //     if (!imageUrl) {
            //         throw new Error("이미지 URL을 받아오지 못했습니다.");
            //     }
            // }

            // 2. 게시글 작성 단계
            const boardFormData = new FormData();
            const boardDTO = {
                'author': user.username,
                'title': title,
                'body': textBody,
            }
            // boardFormData.append('author', user.username);
            // boardFormData.append('title', title);
            // boardFormData.append('body', textBody);
            // boardFormData.append('imgsrc', selectedImage)
            // // 이미지 URL이 있다면 게시글 데이터에 추가
            // if (imageUrl) {
            //     boardFormData.append('imgsrc', imageUrl); // 'imgsrc'는 게시글 모델의 이미지 필드명
            // }

            // 퀴즈 게시판일 경우 문제와 정답 추가
            if (boardId === 'quiz') {
                boardFormData.append('question', quizQuestion);
                boardFormData.append('answer', quizAnswer.toLocaleUpperCase());
                boardFormData.append('type', quizId);
            } else if (boardId === 'survey') { // 설문조사 게시판일 경우 데이터 추가
                boardFormData.append('question', surveyQuestion);
                boardFormData.append('column1', surveyColumn1);
                boardFormData.append('column2', surveyColumn2);
                boardFormData.append('column3', surveyColumn3);
                boardFormData.append('column4', surveyColumn4);
                boardFormData.append('column5', surveyColumn5);
                boardFormData.append('category', boardId);
            } else if (boardId === 'voting') { // OX 게시판일 경우 데이터 추가
                boardFormData.append('question', oxVotingQuestion);
                boardFormData.append('answer', oxVotingCorrectAnswer);
                boardFormData.append('category', boardId);
            }
            if (selectedImage) {
                boardFormData.append("file", selectedImage);
            }
            boardFormData.append(
                "boardDTO",
                new Blob([JSON.stringify(boardDTO)], { type: "application/json" })
            );
            console.log("게시글 데이터 전송 시작...");
            const res = await apiClient.post(`/${boardId}/new`, boardFormData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log("게시글 작성 응답:", res.data);

            alert('글이 성공적으로 작성되었습니다!'); // 성공 알림
            navigate(`/board/${boardId}`); // 예)'free' 게시판으로 이동

        } catch (error) {
            console.error('글 작성 실패:', error);
            alert('글 작성에 실패했습니다. ' + (error.response?.data?.message || error.message)); // 실패 알림
        }
    }

    return (
        <>
            <form onSubmit={(e) => {
                e.preventDefault();
                handleNewBoard(); // 설문조사 게시판이든 아니든 여기서 API 호출
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
                        column1={surveyColumn1} setColumn1={surveyColumn1}
                        column2={surveyColumn2} setColumn2={surveyColumn2}
                        column3={surveyColumn3} setColumn3={surveyColumn3}
                        column4={surveyColumn4} setColumn4={surveyColumn4}
                        column5={surveyColumn5} setColumn5={surveyColumn5}
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
                ) : (
                    <>
                        <input type='text' placeholder='제목' value={title} onChange={(e) => setTitle(e.target.value)}></input> <br />
                        <textarea placeholder='내용' value={textBody} onChange={(e) => setTextBody(e.target.value)}></textarea><br></br>
                        <input
                            type='file'
                            accept="image/*" // 이미지 파일만 선택 가능하도록
                            onChange={handleImageChange}
                        /><br />
                        {imagePreviewUrl && ( // 이미지 미리보기
                            <div>
                                <img src={imagePreviewUrl} alt="Image Preview" style={{ maxWidth: '200px', maxHeight: '200px', marginTop: '10px' }} />
                            </div>
                        )}
                    </>
                )
                }
                <input type="submit" value="전송" />
            </form>
        </>
    )
}

export default NewBoardForm;