import React from 'react';

const SurveyCreationForm = ({
  title, setTitle,
  question, setQuestion,
  column1, setColumn1,
  column2, setColumn2,
  column3, setColumn3,
  column4, setColumn4,
  column5, setColumn5,
}) => 
  {
  return (
    <div className="new-board-survey">
      <input
          type="text"
          className="survey-title"
          placeholder="설문조사 제목"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
      />
      <input
          type="text"
          className="survey-question"
          placeholder="설문조사 질문"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          required
      />
        <p className="notice">📢 옵션은 선택입니다.</p>
      {[
        { value: column1, setter: setColumn1, placeholder: "옵션 1" },
        { value: column2, setter: setColumn2, placeholder: "옵션 2" },
        { value: column3, setter: setColumn3, placeholder: "옵션 3" },
        { value: column4, setter: setColumn4, placeholder: "옵션 4" },
        { value: column5, setter: setColumn5, placeholder: "옵션 5" },
      ].map((col, index) => (
          <React.Fragment key={index}>
            <input
                type="text"
                placeholder={col.placeholder}
                value={col.value}
                onChange={(e) => col.setter(e.target.value)}
            />
          </React.Fragment>

      ))}

    </div>
  );
};

export default SurveyCreationForm;
