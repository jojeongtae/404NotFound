import { createSlice } from '@reduxjs/toolkit';

const userSlice = createSlice({
  name: 'user',
  initialState: {
    username: null,
    role: null,
    nickname: null,
    phone: null,
    address: null,
    point: null,
    status:null,
    grade:null,
    // 여기에 필요한 다른 사용자 정보 필드를 추가할 수 있습니다.
  },
  reducers: {
    setUser: (state, action) => {
      // action.payload에 담겨 오는 필드들만 기존 state에 병합합니다.
      Object.assign(state, action.payload);
    },
    clearUser: (state) => {
      state.username = null;
      state.role = null;
      state.address = null;
      state.nickname = null;
      state.phone = null;
      state.point = null;
      state.status =null;
      state.grade = null
      
      // 필요한 다른 필드도 여기서 초기화합니다.
    },
  },
});

export const { setUser, clearUser } = userSlice.actions;
export default userSlice.reducer;
