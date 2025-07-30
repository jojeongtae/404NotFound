import React, { useState } from "react";
import CaptchaCanvas from "./CaptchaCanvas";
import SignUpForm from "./SignUpForm";

function SignUpWithCaptcha({ onClose }) {
  const [isCaptchaVerified, setIsCaptchaVerified] = useState(false);
  const [captchaCode, setCaptchaCode] = useState("");
  const [input, setInput] = useState("");

  // 캡차 확인 함수
  const handleCaptcha = () => {
    if (input.toUpperCase() === captchaCode.toUpperCase()) {
      alert("인증 성공!");
      setIsCaptchaVerified(true);
    } else {
      alert("인증 실패! 다시 시도하세요.");
    }
  };

  return (
    <>
      {!isCaptchaVerified ? (
        <div style={{ textAlign: "center" }}>
          <h3>로봇이 아닙니다 확인</h3>
          <CaptchaCanvas onChange={setCaptchaCode} />
          <input
            type="text"
            placeholder="코드를 입력하세요"
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
          <button onClick={handleCaptcha}>확인</button>
        </div>
      ) : (
        <SignUpForm onClose={onClose} />
      )}
    </>
  );
}

export default SignUpWithCaptcha;
