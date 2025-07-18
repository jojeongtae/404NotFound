import React from 'react'
import NewBoardForm from '../features/board/NewBoardForm';

const NewBoardPage = () => {
  return (
    <>
    <NewBoardForm></NewBoardForm>
    <form>
        <input type='text' placeholder='제목'></input> <br></br>
        <textarea placeholder='내용'></textarea>
        <input type='submit' placeholder='전송'></input>
    </form>
    </>
)
}

export default NewBoardPage