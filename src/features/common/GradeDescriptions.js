const gradeDescriptions = {
    "500 Internal Server Error (운영진)": "👑 500 ",
    "404 Not Found (신규)": "🐣 404",
    "200 OK (일반 회원)": "👍 200 ",
    "202 Accepted (활동 회원)": "🚀 202",
    "403 Forbidden (우수 회원)": "💎 403 ",
    "401 Unauthorized (손님)": "👻 401"
};

export const getFullGradeDescription = (shortGrade) => {
    return gradeDescriptions[shortGrade] || shortGrade; // 매핑된 값이 없으면 짧은 등급 그대로 반환
};
