import 'package:flutter/material.dart';

class UserInfo extends ChangeNotifier {
  String? username;
  String? password;
  String? address;
  String? phone;
  String? nickname;
  String? grade; // ← 등급 문자열 (예: "500 Internal Server Error (운영진)")
  int? point;
  String? role; // 역할 필드 추가

  UserInfo({
    this.username,
    this.password,
    this.address,
    this.phone,
    this.nickname,
    this.grade,
    this.point,
    this.role,
  });

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

  // JSON → 객체
  factory UserInfo.fromJson(Map<String, dynamic> json) {
    return UserInfo(
      username: json['username'],
      password: json['password'],
      address: json['address'],
      phone: json['phone'],
      nickname: json['nickname'],
      grade: json['grade'],
      point: json['point'],
      role: json['role'],
    );
  }

  // 객체 → JSON
  Map<String, dynamic> toJson() => {
    'username': username,
    'password': password,
    'address': address,
    'phone': phone,
    'nickname': nickname,
    'grade': grade,
    'point': point,
    'role': role,
  };

  // 전체 JSON으로 업데이트
  void updateFromJson(Map<String, dynamic> json) {
    username = json['username'] ?? username;
    address = json['address'] ?? address;
    phone = json['phone'] ?? phone;
    nickname = json['nickname'] ?? nickname;
    grade = json['grade'] ?? grade;
    point = json['point'] ?? point;

    // user-info 응답에는 role이 없으므로, 로그인 시 수신한 role이 null로 덮어쓰여지는 것을 방지
    if (json.containsKey('role')) {
      role = json['role'];
    }
    notifyListeners();
  }

  // ✨ 부분 업데이트 메서드 추가
  void update({String? username, String? role}) {
    if (username != null) this.username = username;
    if (role != null) this.role = role;
    notifyListeners();
  }

  // 초기화
  void clear() {
    username = "";
    password = "";
    address = "";
    phone = "";
    nickname = "";
    grade = "";
    point = null;
    role = "";
    notifyListeners();
  }
}
