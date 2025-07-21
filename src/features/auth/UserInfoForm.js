import apiClient from "../../api/apiClient";
import {useState, useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {setUser} from "./userSlice";

const UserInfoForm = ({ onClose }) => { // onClose prop 추가
    const dispatch = useDispatch();
    const userInfo = useSelector(state => state.user);
    const [userData, setUserData] = useState({
        username: userInfo.username,
        nickname: userInfo.nickname,
        phone: userInfo.phone,
        address: userInfo.address,
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

    return(
        <>
            <form onSubmit={handleSubmit}>
                <div>
                    <input name="nickname" value={userData.nickname} onChange={handleChange} placeholder="닉네임"/>
                </div>
                <div>
                    <input name="phone" value={userData.phone} onChange={handleChange} placeholder="전화번호"/>
                </div>
                <div>
                    <input name="address" value={userData.address} onChange={handleChange} placeholder="주소"/>
                </div>
                <button type="submit">정보 수정</button>
            </form>
        </>
    );
}

export default UserInfoForm;