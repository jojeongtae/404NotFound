
//OX게시판
const VotingBoardForm = () => {
    return(
        <div>
            <form>
                <input type="text" placeholder="질문을 입력하세요."/>
                <button type="submit">추가</button>
            </form>
            <form>
                <button type="submit" value="O">⭕</button>
                <button type="submit" value="X">❌</button>
            </form>
        </div>
    );
}

export default VotingBoardForm;