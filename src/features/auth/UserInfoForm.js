import apiClient from "../../api/apiClient";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setUser } from "./userSlice";

const UserInfoForm = ({ onClose }) => { // onClose prop 추가
    const dispatch = useDispatch();
    const userInfo = useSelector(state => state.user);
    const [userData, setUserData] = useState({
        username: userInfo.username,
        nickname: userInfo.nickname,
        phone: userInfo.phone,
        address: userInfo.address,
        point: userInfo.point,
    });

    useEffect(() => {
        setUserData({
            username: userInfo.username,
            nickname: userInfo.nickname,
            phone: userInfo.phone,
            address: userInfo.address,
        });
    }, [userInfo]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const userResponse = await apiClient.put("/user/user-info", userData);
            console.log("정보 수정 성공: ", userResponse.data);
            dispatch(setUser(userData));
            onClose(); // navigate(-1) 대신 onClose 호출
        } catch (error) {
            console.error("정보 수정 에러: ", error);
        }
    }

    return (
        <div className="tab-container user-info">
            <form onSubmit={handleSubmit}>
                <ul>
                    <li>
                        <label>
                            <span>닉네임</span>
                            <input type="text" name="nickname" value={userData.nickname} onChange={handleChange} placeholder="닉네임"/>
                        </label>
                    </li>
                    <li>
                        <label>
                            <span>전화번호</span>
                            <input type="text" name="phone" value={userData.phone} onChange={handleChange} placeholder="전화번호"/>
                        </label>
                    </li>
                    <li>
                        <label>
                            <span>주소</span>
                            <input type="text" name="address" value={userData.address} onChange={handleChange} placeholder="주소"/>
                        </label>
                    </li>
                    <li>
                        <label>
                            <span>포인트</span>
                            <input type="text" name="point" readOnly={true} placeholder={userInfo.point}/>
                        </label>
                    </li>
                </ul>
                <div className="btn_wrap">
                    <button type="submit" className="btn type2 large">정보 수정</button>
                </div>
            </form>
        </div>
    );
}

export default UserInfoForm;