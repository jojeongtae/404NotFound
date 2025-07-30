import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:provider/provider.dart';
import 'package:notfound_flutter/auth/token.dart';

class User {
  final String username;
  final String nickname;
  final String? grade;
  final String? phone;
  final int? point;
  final String? status;
  final String? address;
  final int? warning;
  final String role;

  User({
    required this.username,
    required this.nickname,
    this.grade,
    this.phone,
    this.point,
    this.status,
    this.address,
    this.warning,
    required this.role,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      username: json['username'] ?? 'N/A',
      nickname: json['nickname'] ?? 'N/A',
      grade: json['grade'],
      phone: json['phone'],
      point: json['point'],
      status: json['status'],
      address: json['address'],
      warning: json['warning'],
      role: json['role'] ?? 'N/A',
    );
  }
}

class UserListProvider with ChangeNotifier {
  List<User> _users = [];
  bool _isLoading = false;
  String? _errorMessage;

  List<User> get users => _users;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> fetchUsers(String token) async {
    _isLoading = true;
    notifyListeners();

    try {
      final url = Uri.parse('http://192.168.0.26:8080/api/admin/users');
      final response = await http.get(
        url,
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> responseData = json.decode(utf8.decode(response.bodyBytes));
        _users = responseData.map((data) => User.fromJson(data)).toList();
        _errorMessage = null;
      } else {
        _errorMessage = 'Failed to load users: ${response.statusCode}';
      }
    } catch (e) {
      _errorMessage = 'An error occurred: $e';
    }

    _isLoading = false;
    notifyListeners();
  }
}
