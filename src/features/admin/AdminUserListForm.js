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
    <div className="admin-user">
      <h3>관리자 - 유저 목록</h3>
      <table className="admin-table">
        <colgroup>
          <col style={{width:"14%"}}/>
          <col/>
          <col style={{width:"19%"}}/>
          <col/>
          <col/>
          <col/>
          <col style={{width:"17%"}}/>
          <col/>
          <col/>
        </colgroup>
        <thead>
        <tr>
          <th>아이디</th>
          <th>닉네임</th>
          <th>등급</th>
          <th>전화번호</th>
          <th>포인트</th>
          <th>상태</th>
          <th>주소</th>
          <th>경고</th>
          <th>상태 변경</th>
        </tr>
        </thead>
        <tbody>
        {users.map((user) => (
            <>
              <tr key={user.username}>
                <td>{user.username}</td>
                <td>{user.nickname}</td>
                <td>{user.grade}</td>
                <td>{user.phone}</td>
                <td>{user.point}</td>
                <td>{user.status}</td>
                <td>{user.address}</td>
                <td>{user.warning}</td>
                <td>
                  <button onClick={() => handleStatusChange(user.username)}>상태변경</button>
                </td>
              </tr>
            </>
        ))}
        </tbody>
      </table>

    </div>
  );
};

export default AdminUserListForm;