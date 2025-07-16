import { createSlice } from '@reduxjs/toolkit';

const userSlice = createSlice({
  name: 'user',
  initialState: {
    username: null,
    role: null,
    // 여기에 필요한 다른 사용자 정보 필드를 추가할 수 있습니다.
  },
  reducers: {
    setUser: (state, action) => {
      state.username = action.payload.username;
      state.role = action.payload.role;
      // 필요한 다른 필드도 여기서 설정합니다.
    },
    clearUser: (state) => {
      state.username = null;
      state.role = null;
      // 필요한 다른 필드도 여기서 초기화합니다.
    },
  },
});

export const { setUser, clearUser } = userSlice.actions;
export default userSlice.reducer;
