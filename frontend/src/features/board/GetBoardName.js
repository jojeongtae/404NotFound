export const GetBoardName = (category) => {
  const mapping = {
    free: '자유 게시판',
    qna: 'Q&A 게시판',
    info: '정보 게시판',
    used: '중고거래 게시판',
    food: '먹거리 게시판',
  };

  return mapping[category] || category; // 매핑 없으면 그대로 반환
};