import { configureStore, combineReducers } from '@reduxjs/toolkit';
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage'; // localStorage 사용

import userReducer from '../features/auth/userSlice';
import tokenReducer from '../features/auth/tokenSlice';
import boardReducer from '../features/board/boardSlice'; 

const rootReducer = combineReducers({
  user: userReducer,
  token: tokenReducer,
  board: boardReducer, 
});

const persistConfig = {
  key: 'root', // localStorage에 저장될 키
  storage, // 사용할 저장소 (localStorage)
  whitelist: ['user', 'token','board'], // 유지할 Redux 슬라이스 지정 (user와 token만 유지)
};

const persistedReducer = persistReducer(persistConfig, rootReducer);
  
export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE'],
      },
    }),
});

export const persistor = persistStore(store);
