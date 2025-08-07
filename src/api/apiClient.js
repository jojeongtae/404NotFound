import axios from "axios";
import {store} from "../store/store";
import { setToken } from "../features/auth/tokenSlice";

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

const apiClient = axios.create({
    baseURL: `${API_BASE_URL}/api`,
    // baseURL:"http://localhost:8080/api",
    headers:{
        "Content-Type":"application/json"
    },
    withCredentials:true
});
console.log("✅ API_BASE_URL:", API_BASE_URL); // 값 확인

apiClient.interceptors.request.use(
    (config) =>{
        if(config.data && config.data instanceof URLSearchParams){
            config.headers["Content-Type"] = "application/x-www-form-urlencoded"
        }
        const jwtToken = store.getState().token?.token;
        if(jwtToken){
            config.headers["authorization"] = jwtToken;
        }
        return config;
    },
    (err) => Promise.reject(err)
);
apiClient.interceptors.response.use(
 (response) => response,
 async (error) =>{
    const originalRequest = error.config;

    // 403 Forbidden 에러 처리
    if (error.response?.status === 403) {
      alert("정지처리된 상태입니다.");
      return Promise.reject(error); // 에러를 계속 전파하여 호출한 곳에서 추가 처리할 수 있도록 함
    }

    if(error.response?.status === 456 && !originalRequest._retry){
        originalRequest._retry = true;
        try{
            const res = await axios.post("/api/reissue",null,{
                withCredentials: true
            });
            const newAccessToken = res.headers["authorization"];
            if(newAccessToken){
                store.dispatch(setToken(newAccessToken));
                originalRequest.headers["authorization"] = newAccessToken;
                return apiClient(originalRequest);
            }else{
                console.log("토큰 재발급 실패: 응답에 토큰이 없음 ");
            }
        }catch(reissueErr){
            console.log("토큰 재발급 중 오류 발생 : " + reissueErr);
            return Promise.reject(reissueErr);
        }
    }
    return Promise.reject(error)
 }   
);

export default apiClient;
