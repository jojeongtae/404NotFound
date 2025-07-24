//OX정답 풀이
const VotingAnswerForm = ({ quiz }) => {
    const handleAnswer = (e) => {
        e.preventDefault();
        const userAnswer = e.target.answer.value;
        if(userAnswer.trim() === quiz.answer) {
            alert("정답입니당~!🥳");
        }else {
            alert("땡! 틀렸어요😢");
        }
    }
    return(
        <div>
            <form onSubmit={handleAnswer}>
                <h2>{quiz.title}</h2>
                <p>{quiz.question}</p>
                <input name="answer" placeholder="당신의 답은?" />
                <button type="submit">정답 제출</button>
            </form>
        </div>
    );
}

export default VotingAnswerForm;