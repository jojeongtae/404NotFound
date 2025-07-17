import React from 'react'

const NewBoardPage = () => {
    
  return (
    <>
    <form>
        <input type='text' placeholder='제목'></input> <br></br>
        <textarea placeholder='내용'></textarea>
        <input type='submit' placeholder='전송'></input>
    </form>
    </>
)
}

export default NewBoardPage