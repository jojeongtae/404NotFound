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

  const handleSubmit = async (e) => {
    e.preventDefault(); // 폼의 기본 제출 동작 방지

    // 1. 데이터를 두 부분으로 분리
    const authData = {
      username: formData.username,
      password: formData.password,
    };
    const profileData = {
      username: formData.username, 
      nickname: formData.nickname,
      phone: formData.phone,
      address: formData.address,
    };

    try {


      const authResponse = await apiClient.post('/join', authData);

      console.log('1. 인증 서버로 전송 성공:', authResponse.data);


      // const profileResponse = await apiClient.post('/join', profileData);

      // console.log('2. 프로필 서버로 전송 성공:', profileResponse.data);
      alert('회원가입이 완료되었습니다!');
      onClose(); // 모달 닫기

    } catch (error) {
      // 4. 에러 처리
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
          <button type="button">아이디 중복확인</button>
        </div>
        <div>
          <input type='text' name='nickname' placeholder='닉네임 입력칸' value={formData.nickname} onChange={handleChange} />
          <button type="button">닉네임 중복확인</button>
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
