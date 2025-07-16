import { configureStore } from "@reduxjs/toolkit";
import tokenReducer from '../features/auth/tokenSlice';
import userReducer from '../features/auth/userSlice'; // userSlice의 리듀서 임포트

const store = configureStore({
  reducer: {
    token: tokenReducer,
    user: userReducer, // user 슬라이스를 스토어에 추가
    // 여기에 다른 슬라이스 리듀서들을 추가할 수 있습니다.
  },
});

export default store;