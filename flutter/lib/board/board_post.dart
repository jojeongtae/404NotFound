class BoardPost {
  final int id;
  final String title;
  final String body;
  final String? imgSrc;
  final String author;
  final String authorNickname;
  final int recommend;
  final int views;
  final String createdAt;
  final String grade;
  final int? price;

  // 등급 문자열 → 이모지 + 코드 매핑
  static const Map<String, String> _gradeDisplayMap = {
    '500 Internal Server Error (운영진)': '👑 500',
    '404 Not Found (신규)': '🐣 404',
    '200 OK (일반 회원)': '👍 200',
    '202 Accepted (활동 회원)': '🚀 202',
    '403 Forbidden (우수 회원)': '💎 403',
    '401 Unauthorized (손님)': '👻 401',
  };

  // 이모지 등급 표시
  String get displayGrade => _gradeDisplayMap[grade] ?? grade ?? '등급 없음';

  BoardPost({
    required this.id,
    required this.title,
    required this.body,
    this.imgSrc,
    required this.author,
    required this.authorNickname,
    required this.recommend,
    required this.views,
    required this.createdAt,
    required this.grade,
    this.price,
  });

  factory BoardPost.fromJson(Map<String, dynamic> json) {
    return BoardPost(
      id: json['id'],
      title: json['title'],
      body: json['body'],
      imgSrc: json['imgsrc'],
      author: json['author'],
      authorNickname: json['authorNickname'],
      recommend: json['recommend'] ?? 0,
      views: json['views'] ?? 0,
      createdAt: json['createdAt'],
      grade: json['grade'],
      price: json['price'] as int?,
    );
  }
}
