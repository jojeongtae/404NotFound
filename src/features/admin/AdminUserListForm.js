import React, { useEffect, useState } from 'react';
import apiClient from '../../api/apiClient';

const AdminUserListForm = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await apiClient.get('/admin/users'); // 백엔드 API 경로 확인
        setUsers(response.data);
        console.log('유저 목록:', response.data);
      } catch (err) {
        console.error('유저 목록 불러오기 실패:', err);
        setError('유저 목록을 불러오는 데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  if (loading) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>유저 목록을 불러오는 중...</div>;
  }

  if (error) {
    return <div style={{ textAlign: 'center', padding: '50px', color: 'red' }}>{error}</div>;
  }

  if (users.length === 0) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>등록된 유저가 없습니다.</div>;
  }
  const handleStatusChange = async(username) =>{
    let result = prompt("상태 입력(1.VISIBLE,2.SUSPENDED,3.BANNED,4.INACTIVE)");
    switch(result){
      case "1":
        result = "ACTIVE";
        break; 
        case "2":
        result = "SUSPENDED";
        break; 
        case "3":
        result = "BANNED";
        break; 
        case "4":
        result = "INACTIVE";
        break;
        default:
          alert("잘못된 입력값");
          return;
    }

    try {
      const res = await apiClient.patch(`/admin/user-status/${username}?status=${result}`);
      console.log(res);
      console.log("변경완료")
    } catch (error) {
      console.log(error);
    }
  }


  return (
    <div style={{ padding: '20px' }}>
      <h2>관리자 - 유저 목록</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
        <thead>
          <tr style={{ backgroundColor: '#f2f2f2' }}>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>아이디</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>닉네임</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>등급</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>전화번호</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>포인트</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>상태</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>주소</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>경고</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', textAlign: 'center' }}>상태 변경</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <>
              <tr key={user.username}>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.username}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.nickname}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.grade}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.phone}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.point}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.status}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.address}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>{user.warning}</td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}><button onClick={()=>handleStatusChange(user.username)}>상태 변경</button></td>
              </tr>
            </>
          ))}
        </tbody>
      </table>

    </div>
  );
};

export default AdminUserListForm;