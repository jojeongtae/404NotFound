import React from 'react';
import ReactDOM from 'react-dom';

const Modal = ({ children, onClose }) => {
  return ReactDOM.createPortal(
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <button className="close" onClick={onClose}>❌</button>
        {children}
      </div>
    </div>,
    document.getElementById('modal-root') || document.body // modal-root 엘리먼트가 없으면 body에 추가
  );
};

export default Modal;
