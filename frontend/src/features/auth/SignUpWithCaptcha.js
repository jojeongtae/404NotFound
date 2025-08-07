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
    <div className="captcha-modal">
      {!isCaptchaVerified ? (
        <form onSubmit={handleCaptcha}>
          <p className="title">🤖 로봇이 아님을 확인해주세요.</p>
          <CaptchaCanvas onChange={setCaptchaCode} />
          <div className="captcha-response">
            <input type="text" placeholder="코드를 입력하세요" value={input} onChange={(e) => setInput(e.target.value)} />
            <button type="submit" className="btn type2">확인</button>
          </div>
        </form>
      ) : (
        <SignUpForm onClose={onClose} />
      )}
    </div>
  );
}

export default SignUpWithCaptcha;
