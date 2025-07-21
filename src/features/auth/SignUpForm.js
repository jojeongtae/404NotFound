import React, { useState } from 'react';
import apiClient from '../../api/apiClient'; // apiClient 임포트

const SignUpForm = ({ onClose }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    nickname: '',
    phone: '',
    address: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  // 아이디 중복 확인 함수
  const handleCheckUsername = async () => {
    if (!formData.username) {
      alert("아이디를 입력해주세요.");
      return;
    }
    try {
      // /user/check-username/{username} 경로 사용 (경로 변수)
      const response = await apiClient.get(`/user/check-username/${formData.username}`);
      // 응답이 성공적으로 왔다면 (200 OK), 해당 아이디가 존재한다는 의미
      if (response.data === true) {
        alert("이미 사용 중인 아이디입니다.");
        setFormData(prev => ({
          ...prev,
          username: ""
        }));

      } else {
        alert("사용 가능한 아이디입니다.");
      }


    } catch (error) {
      // 오류가 발생했고, 404 Not Found라면 사용 가능한 아이디
      if (error.response && error.response.status === 404) {
        alert("사용 가능한 아이디입니다.");
      } else {
        console.error("상세 오류 (아이디 중복 확인):", error);
        alert("아이디 중복 확인 중 오류가 발생했습니다.");
      }
    }
  };

  const handleCheckNickname = async () => {
    if (!formData.nickname) {
      alert("닉네임을 입력해주세요.");
      return;
    }
    try {
      const response = await apiClient.get(`/user/check-nickname/${formData.nickname}`);
      if (response.data === true) {
        alert("이미 사용 중인 닉네임입니다.");
        setFormData(prev => ({
          ...prev,
          nickname: ""
        }));
      } else {
        alert("사용 가능한 닉네임입니다.");
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        alert("사용 가능한 닉네임입니다.");
      } else {
        console.error("상세 오류 (닉네임 중복 확인):", error); // 괄호 닫기
        alert("닉네임 중복 확인 중 오류가 발생했습니다.");
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const authData = {
      username: formData.username,
      password: formData.password,
      nickname: formData.nickname,
      phone: formData.phone,
      address: formData.address,
    };

    try {
      const authResponse = await apiClient.post('/join', authData);
      console.log('1. 인증 서버로 전송 성공:', authResponse.data);
      alert('회원가입이 완료되었습니다!');
      onClose();
    } catch (error) {
      console.error('회원가입 중 오류 발생:', error);
      const errorMessage = error.response?.data?.message || error.message || '알 수 없는 오류';
      alert(`회원가입에 실패했습니다: ${errorMessage}. 다시 시도해주세요.`);
    }
  };

  return (
    <>
      <form onSubmit={handleSubmit}>
        <h2>회원가입</h2>
        <div>
          <input type='text' name='username' placeholder='아이디 입력칸' value={formData.username} onChange={handleChange} />
          <button type="button" onClick={handleCheckUsername}>아이디 중복확인</button>
        </div>
        <div>
          <input type='text' name='nickname' placeholder='닉네임 입력칸' value={formData.nickname} onChange={handleChange} />
          <button type="button" onClick={handleCheckNickname}>닉네임 중복확인</button>
        </div>
        <div>
          <input type='password' name='password' placeholder='비밀번호 입력칸' value={formData.password} onChange={handleChange} />
        </div>
        <div>
          <input type='text' name='phone' placeholder='핸드폰 번호 입력칸' value={formData.phone} onChange={handleChange} />
        </div>
        <div>
          <input type='text' name='address' placeholder='주소지 입력칸' value={formData.address} onChange={handleChange} />
        </div>
        <div>
          <input type='submit' value='회원가입' />
        </div>
      </form>
    </>
  );
};

export default SignUpForm;
