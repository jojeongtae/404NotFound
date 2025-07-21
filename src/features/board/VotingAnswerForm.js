import {useState} from "react";
import {useDispatch} from "react-redux";

const VotingAnswerForm = () => {
    const [selectedAnswer, setSelectedAnswer] = useState("");
    const dispatch = useDispatch();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {

        }catch (error){

        }
    }
    return(
        <div>
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="answer" value={selectedAnswer} />
                <label>정답 제출: </label>
                <button type="submit" onClick={() => setSelectedAnswer("O")}>⭕</button>
                <button type="submit" onClick={() => setSelectedAnswer("X")}>❌</button>
            </form>
        </div>
    );
}

export default VotingAnswerForm;    ``