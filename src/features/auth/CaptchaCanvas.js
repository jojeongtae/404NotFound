import { useRef, useState, useEffect } from "react";

function CaptchaCanvas({ length = 5, onChange }) {
  const canvasRef = useRef(null);
  const [code, setCode] = useState("");

  // 랜덤 문자열 생성
  const generateCode = () => {
    const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 혼동되는 I,O,1,0 제외
    let result = "";
    for (let i = 0; i < length; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
  };

  // 캔버스에 문자열 렌더링
  const drawCaptcha = (text) => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");
    const width = canvas.width;
    const height = canvas.height;

    // 배경색 랜덤
    ctx.fillStyle = `rgb(${200 + Math.random() * 55}, ${200 + Math.random() * 55}, ${200 + Math.random() * 55})`;
    ctx.fillRect(0, 0, width, height);

    // 랜덤 노이즈 선
    for (let i = 0; i < 5; i++) {
      ctx.strokeStyle = `rgb(${Math.random()*255},${Math.random()*255},${Math.random()*255})`;
      ctx.beginPath();
      ctx.moveTo(Math.random() * width, Math.random() * height);
      ctx.lineTo(Math.random() * width, Math.random() * height);
      ctx.stroke();
    }

    // 텍스트 그리기
    ctx.font = "30px Arial";
    ctx.textBaseline = "middle";
    for (let i = 0; i < text.length; i++) {
      const x = 20 + i * 30;
      const y = height / 2 + (Math.random() * 10 - 5); // 살짝 흔들기
      ctx.fillStyle = `rgb(${Math.random()*150},${Math.random()*150},${Math.random()*150})`;
      ctx.fillText(text[i], x, y);
    }
  };

  // 새 CAPTCHA 생성
  const refreshCaptcha = () => {
    const newCode = generateCode();
    setCode(newCode);
    drawCaptcha(newCode);
    if (onChange) onChange(newCode);
  };

  useEffect(() => {
    refreshCaptcha();
  }, []);

  return (
    <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
      <canvas ref={canvasRef} width={200} height={60} style={{ border: "1px solid #ccc" }} />
      <button type="button" onClick={refreshCaptcha}>새로고침</button>
    </div>
  );
}

export default CaptchaCanvas;
