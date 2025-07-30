class Board {
  final int id;
  final String title;
  final String content;
  final String author;
  // Add other fields as needed, e.g., createdAt, views, etc.

  Board({
    required this.id,
    required this.title,
    required this.content,
    required this.author,
  });

  factory Board.fromJson(Map<String, dynamic> json) {
    return Board(
      id: json['id'],
      title: json['title'],
      content: json['content'],
      author: json['author'],
    );
  }
}
