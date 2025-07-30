class Report {
  final int id;
  final String reporter;
  final String reported;
  final String reason;
  final String? description;
  final String? targetTable;
  final int? targetId;
  final String status;
  final String createdAt;
  final String? updatedAt;

  Report({
    required this.id,
    required this.reporter,
    required this.reported,
    required this.reason,
    this.description,
    this.targetTable,
    this.targetId,
    required this.status,
    required this.createdAt,
    this.updatedAt,
  });

  factory Report.fromJson(Map<String, dynamic> json) {
    return Report(
      id: json['id'],
      reporter: json['reporter'] ?? 'N/A',
      reported: json['reported'] ?? 'N/A',
      reason: json['reason'] ?? 'N/A',
      description: json['description'],
      targetTable: json['targetTable'],
      targetId: json['targetId'],
      status: json['status'] ?? 'N/A',
      createdAt: json['createdAt'] ?? 'N/A',
      updatedAt: json['updatedAt'],
    );
  }
}
