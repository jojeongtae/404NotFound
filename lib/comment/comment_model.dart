class Comment {
  final int? id; // id를 nullable로 변경
  final String content;
  final String authorNickname;
  final String createdAt;
  final int? parentId; // 대댓글을 위한 parentId 추가

  Comment({
    this.id, // id를 optional로 변경
    required this.content,
    required this.authorNickname,
    required this.createdAt,
    this.parentId,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      id: json['id'] as int?, // id 파싱 시 nullable int로 캐스팅
      content: json['content'],
      authorNickname: json['authorNickname'],
      createdAt: json['createdAt'],
      parentId: json['parentId'], // parentId 파싱
    );
  }
}
