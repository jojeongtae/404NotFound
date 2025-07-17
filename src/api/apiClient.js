import axios from "axios";
import store from "../store/store";
import { setToken } from "../features/auth/tokenSlice";

const apiClient = axios.create({
    // baseURL: "/api",
    baseURL:"http://localhost:8080/api",
    headers:{
        "Content-Type":"application/json"
    },
    withCredentials:true
});

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
    if(error.response?.status === 456 && !originalRequest._retry){
        originalRequest._retry = true;
        try{
            const res = await axios.post("http://localhost:8080/api/reissue",null,{
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