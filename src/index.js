import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Provider } from 'react-redux';
import { store, persistor } from './store/store';
import { PersistGate } from 'redux-persist/integration/react';
import apiClient from './api/apiClient'; // apiClient 임포트

const onBeforeLift = () => {
  // Redux 스토어가 완전히 재수화된 후에 실행됩니다.
  const jwtToken = store.getState().token?.token;
  if (jwtToken) {
    apiClient.defaults.headers.common["authorization"] = jwtToken;
    console.log("PersistGate: 토큰 설정 완료");
  }
};

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor} onBeforeLift={onBeforeLift}> {/* onBeforeLift 추가 */}
        <App />
      </PersistGate>
    </Provider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
